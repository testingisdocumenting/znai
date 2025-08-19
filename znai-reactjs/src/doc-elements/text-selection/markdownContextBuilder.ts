/*
 * Copyright 2025 znai maintainers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

export function buildContext(): string {
  const selection = window.getSelection();
  if (!selection || !selection.rangeCount || selection.isCollapsed) {
    return "";
  }

  const range = selection.getRangeAt(0);

  const codeBlock = findCodeBlock(range);
  if (codeBlock) {
    return buildCodeSnippetContext();
  }

  return buildParagraphContext();
}

function buildCodeSnippetContext(): string {
  const selection = window.getSelection();
  if (!selection || !selection.rangeCount || selection.isCollapsed) {
    return "";
  }

  const range = selection.getRangeAt(0);

  const codeBlock = findCodeBlock(range);
  if (!codeBlock) {
    return "";
  }

  const lineElements = codeBlock.querySelectorAll(".znai-code-line");

  const lines: string[] = [];
  const selectedLineIndices = new Set<number>();

  lineElements.forEach((lineElement, index) => {
    const lineText = lineElement.textContent ?? "";
    lines.push(lineText);

    if (isLineIntersectingSelection(lineElement as HTMLElement, range)) {
      selectedLineIndices.add(index);
    }
  });

  return buildCodeSnippetMarkdownOutput(lines, selectedLineIndices, range, lineElements);
}

function buildParagraphContext(): string {
  const selection = window.getSelection();
  if (!selection || !selection.rangeCount || selection.isCollapsed) {
    return "";
  }

  const range = selection.getRangeAt(0);
  const selectedText = range.toString();

  if (!selectedText.trim()) {
    return "";
  }

  const paragraphs = collectSelectedParagraphs(range);
  if (paragraphs.length === 0) {
    return "";
  }

  return buildParagraphOutput(paragraphs, selectedText);
}

function collectSelectedParagraphs(range: Range): HTMLElement[] {
  const paragraphs: HTMLElement[] = [];

  // Find all elements that contain part of the selection
  let startContainer = range.startContainer;
  let endContainer = range.endContainer;

  // Find paragraph elements containing start and end
  const startParagraph = findContainingParagraph(startContainer);
  const endParagraph = findContainingParagraph(endContainer);

  if (startParagraph) {
    paragraphs.push(startParagraph);
  }

  if (endParagraph && endParagraph !== startParagraph) {
    paragraphs.push(endParagraph);
  }

  return paragraphs;
}

function findContainingParagraph(node: Node): HTMLElement | null {
  let element: Node | null = node;

  while (element) {
    if (element.nodeType === Node.ELEMENT_NODE) {
      const htmlElement = element as HTMLElement;
      if (["P", "DIV", "LI", "H1", "H2", "H3", "H4", "H5", "H6"].includes(htmlElement.tagName)) {
        return htmlElement;
      }
    }
    element = element.parentNode;
  }

  return null;
}

function buildParagraphOutput(paragraphs: HTMLElement[], selectedText: string): string {
  const result: string[] = [];
  const selection = window.getSelection();
  const range = selection ? selection.getRangeAt(0) : null;

  paragraphs.forEach((paragraph) => {
    const paragraphText = paragraph.textContent ?? "";
    const context = addContextAroundParagraph(paragraph, paragraphText);
    
    // Calculate the position of the selection within the paragraph
    let selectionPosition = -1;
    if (range && paragraph.contains(range.startContainer)) {
      const tempRange = document.createRange();
      tempRange.selectNodeContents(paragraph);
      tempRange.setEnd(range.startContainer, range.startOffset);
      selectionPosition = tempRange.toString().length;
    }
    
    const highlightedText = highlightSelectedTextInParagraph(context, selectedText, selectionPosition);
    result.push(highlightedText);
  });

  return result.join("\n\n");
}

function addContextAroundParagraph(paragraph: HTMLElement, paragraphText: string): string {
  const CONTEXT_LENGTH = 50;

  if (paragraphText.length >= CONTEXT_LENGTH * 2) {
    return paragraphText;
  }

  let contextBefore = "";
  let contextAfter = "";

  // Get text before
  let prevElement = paragraph.previousElementSibling;
  while (prevElement && contextBefore.length < CONTEXT_LENGTH) {
    const prevText = prevElement.textContent ?? "";
    const neededLength = CONTEXT_LENGTH - contextBefore.length;

    if (prevText.length <= neededLength) {
      contextBefore = prevText + (contextBefore ? " " : "") + contextBefore;
    } else {
      contextBefore = "..." + prevText.slice(-neededLength) + (contextBefore ? " " : "") + contextBefore;
      break;
    }

    prevElement = prevElement.previousElementSibling;
  }

  // Get text after
  let nextElement = paragraph.nextElementSibling;
  while (nextElement && contextAfter.length < CONTEXT_LENGTH) {
    const nextText = nextElement.textContent ?? "";
    const neededLength = CONTEXT_LENGTH - contextAfter.length;

    if (nextText.length <= neededLength) {
      contextAfter = contextAfter + (contextAfter ? " " : "") + nextText;
    } else {
      contextAfter = contextAfter + (contextAfter ? " " : "") + nextText.slice(0, neededLength) + "...";
      break;
    }

    nextElement = nextElement.nextElementSibling;
  }

  return (contextBefore + (contextBefore ? " " : "") + paragraphText + (contextAfter ? " " : "") + contextAfter).trim();
}

function highlightSelectedTextInParagraph(text: string, selectedText: string, selectionPosition: number = -1): string {
  const trimmedSelection = selectedText.trim();

  if (!trimmedSelection) {
    return text;
  }

  // Try exact match first
  if (text.includes(trimmedSelection)) {
    if (selectionPosition >= 0) {
      // Find the actual position of the trimmed selection in the text
      const actualPosition = text.indexOf(trimmedSelection, Math.max(0, selectionPosition - 10)); // Allow some flexibility
      if (actualPosition >= 0) {
        const before = text.substring(0, actualPosition);
        const after = text.substring(actualPosition + trimmedSelection.length);
        return before + `**${trimmedSelection}**` + after;
      }
    }
    
    // Fallback to first occurrence
    const escapedSelection = trimmedSelection.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    return text.replace(new RegExp(escapedSelection), `**${trimmedSelection}**`);
  }

  return text;
}

function findCodeBlock(range: Range): HTMLElement | null {
  let element: Node | null = range.commonAncestorContainer;

  // Walk up the DOM tree to find a code block
  while (element) {
    // Check if it's an element node (nodeType 1)
    if (element.nodeType === 1) {
      const htmlElement = element as HTMLElement;
      if (htmlElement.tagName === "PRE" || htmlElement.classList.contains("snippet")) {
        return htmlElement;
      }
    }
    element = element.parentElement;
  }

  return null;
}

function isLineIntersectingSelection(lineElement: HTMLElement, range: Range): boolean {
  const lineRange = document.createRange();
  lineRange.selectNodeContents(lineElement);

  try {
    const selectionStartComparison = range.compareBoundaryPoints(Range.START_TO_END, lineRange);
    const selectionEndComparison = range.compareBoundaryPoints(Range.END_TO_START, lineRange);

    return selectionStartComparison >= 0 && selectionEndComparison <= 0;
  } catch (error) {
    console.warn("Failed to compare ranges for line intersection:", error);
    return false;
  }
}

function buildCodeSnippetMarkdownOutput(
  lines: string[],
  selectedLineIndices: Set<number>,
  range: Range,
  lineElements: NodeListOf<Element>
): string {
  const result: string[] = [];

  lines.forEach((line, index) => {
    if (selectedLineIndices.has(index)) {
      const lineElement = lineElements[index] as HTMLElement;
      const selectedText = getSelectionInLine(lineElement, range);
      const selectionPosition = getSelectionPositionInLine(lineElement, range);

      const highlightedLine = createHighlightedLine(line, selectedText, selectionPosition);
      result.push(highlightedLine.trimEnd() + " <----");
    } else {
      result.push(line.trimEnd());
    }
  });

  return result.join("\n");
  // return "```\n" + result.join("\n") + "```";
}

function createHighlightedLine(line: string, selectedText: string, selectionPosition: number = -1): string {
  const trimmedSelection = selectedText.trim();

  if (trimmedSelection && line.includes(trimmedSelection)) {
    // If we have a specific position, highlight at that position
    if (selectionPosition >= 0) {
      // Find the actual position of the trimmed selection in the line
      const actualPosition = line.indexOf(trimmedSelection, selectionPosition);
      if (actualPosition >= 0) {
        const before = line.substring(0, actualPosition);
        const after = line.substring(actualPosition + trimmedSelection.length);
        return before + `**${trimmedSelection}**` + after;
      }
    }
    
    // Fallback to first occurrence
    const escapedSelection = trimmedSelection.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
    return line.replace(new RegExp(escapedSelection), `**${trimmedSelection}**`);
  }

  return `**${line}**`;
}

function getSelectionPositionInLine(lineElement: HTMLElement, range: Range): number {
  try {
    const lineText = lineElement.textContent || "";
    const selectedText = getSelectionInLine(lineElement, range).trim();
    
    if (!selectedText) {
      return -1;
    }

    // Create a range from the start of the line to the start of the selection
    const tempRange = document.createRange();
    tempRange.selectNodeContents(lineElement);
    
    if (lineElement.contains(range.startContainer)) {
      tempRange.setEnd(range.startContainer, range.startOffset);
      const textBeforeSelection = tempRange.toString();
      return textBeforeSelection.length;
    }
    
    // If selection starts outside this line, it starts at the beginning
    return 0;
  } catch (error) {
    console.warn("Failed to get selection position in line:", error);
    return -1;
  }
}

function getSelectionInLine(lineElement: HTMLElement, range: Range): string {
  try {
    const lineRange = document.createRange();
    lineRange.selectNodeContents(lineElement);

    const intersectionRange = document.createRange();

    const startContainer = lineElement.contains(range.startContainer) ? range.startContainer : lineRange.startContainer;
    const startOffset = lineElement.contains(range.startContainer) ? range.startOffset : lineRange.startOffset;

    const endContainer = lineElement.contains(range.endContainer) ? range.endContainer : lineRange.endContainer;
    const endOffset = lineElement.contains(range.endContainer) ? range.endOffset : lineRange.endOffset;

    intersectionRange.setStart(startContainer, startOffset);
    intersectionRange.setEnd(endContainer, endOffset);

    return intersectionRange.toString();
  } catch (error) {
    console.warn("Failed to get selection in line:", error);
    return "";
  }
}

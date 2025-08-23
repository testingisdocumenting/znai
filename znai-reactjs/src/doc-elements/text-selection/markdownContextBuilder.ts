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
    return buildCodeSnippetContext(range, codeBlock);
  }

  return buildParagraphContext(range);
}

function buildCodeSnippetContext(range: Range, codeBlock: HTMLElement): string {
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

function buildParagraphContext(range: Range): string {
  const selectedText = range.toString();

  if (!selectedText.trim()) {
    return "";
  }

  // Find the paragraph containing the start of the selection
  const paragraph = findContainingParagraph(range.startContainer);
  if (!paragraph) {
    return "";
  }

  return buildParagraphOutput(paragraph, selectedText, range);
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

function buildParagraphOutput(paragraph: HTMLElement, selectedText: string, range: Range): string {
  const paragraphText = paragraph.textContent ?? "";

  // Calculate the position of the selection within the paragraph
  let selectionPosition = -1;
  if (paragraph.contains(range.startContainer)) {
    const tempRange = document.createRange();
    tempRange.selectNodeContents(paragraph);
    tempRange.setEnd(range.startContainer, range.startOffset);
    selectionPosition = tempRange.toString().length;
  }

  const highlightedText = highlightSelectedTextInParagraph(paragraphText, selectedText, selectionPosition);
  // Handle multi-line content with proper markdown quoting
  return "> " + highlightedText.replace(/\n/g, "\n> ");
}


function highlightSelectedTextInParagraph(text: string, selectedText: string, selectionPosition: number = -1): string {
  return highlightText(text, selectedText, selectionPosition, false);
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

  return "```\n" + result.join("\n") + "\n```";
}

function createHighlightedLine(line: string, selectedText: string, selectionPosition: number = -1): string {
  return highlightText(line, selectedText, selectionPosition, true);
}

function highlightText(
  text: string,
  selectedText: string,
  selectionPosition: number = -1,
  isCodeLine: boolean = false
): string {
  const trimmedSelection = selectedText.trim();

  if (!trimmedSelection) {
    return isCodeLine ? `**${text}**` : text;
  }

  if (text.includes(trimmedSelection)) {
    if (selectionPosition >= 0) {
      // Find the actual position of the trimmed selection in the text
      const searchStart = isCodeLine ? selectionPosition : Math.max(0, selectionPosition - 10);
      const actualPosition = text.indexOf(trimmedSelection, searchStart);
      if (actualPosition >= 0) {
        const before = text.substring(0, actualPosition);
        const after = text.substring(actualPosition + trimmedSelection.length);
        return before + `**${trimmedSelection}**` + after;
      }
    }

    // Fallback to first occurrence
    const escapedSelection = escapeRegex(trimmedSelection);
    return text.replace(new RegExp(escapedSelection), `**${trimmedSelection}**`);
  }

  return isCodeLine ? `**${text}**` : text;
}

function escapeRegex(str: string): string {
  return str.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
}

function getSelectionPositionInLine(lineElement: HTMLElement, range: Range): number {
  try {
    if (!lineElement.contains(range.startContainer)) {
      // If selection starts outside this line, it starts at the beginning
      return 0;
    }

    // Create a range from the start of the line to the start of the selection
    const beforeRange = document.createRange();
    beforeRange.selectNodeContents(lineElement);
    beforeRange.setEnd(range.startContainer, range.startOffset);

    // The length of text before selection is the position
    return beforeRange.toString().length;
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

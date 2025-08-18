export function buildContext(_container: HTMLElement): string {
  const selection = window.getSelection();
  if (!selection || !selection.rangeCount || selection.isCollapsed) {
    return "";
  }

  const range = selection.getRangeAt(0);
  
  const codeBlock = findCodeBlock(range);
  if (!codeBlock) {
    return "";
  }

  const lineElements = codeBlock.querySelectorAll('.znai-code-line');

  const lines: string[] = [];
  const selectedLineIndices = new Set<number>();
  
  lineElements.forEach((lineElement, index) => {
    const lineText = lineElement.textContent ?? '';
    lines.push(lineText);
    
    if (isLineIntersectingSelection(lineElement as HTMLElement, range)) {
      selectedLineIndices.add(index);
    }
  });

  return buildMarkdownOutput(lines, selectedLineIndices, range, lineElements);
}

function findCodeBlock(range: Range): HTMLElement | null {
  let element: Node | null = range.commonAncestorContainer;
  
  // Walk up the DOM tree to find a code block
  while (element) {
    // Check if it's an element node (nodeType 1)
    if (element.nodeType === 1) {
      const htmlElement = element as HTMLElement;
      if (htmlElement.tagName === 'PRE' || htmlElement.classList.contains('snippet')) {
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
    console.warn('Failed to compare ranges for line intersection:', error);
    return false;
  }
}

function buildMarkdownOutput(
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
      
      const highlightedLine = createHighlightedLine(line, selectedText);
      result.push(highlightedLine + ' <----');
    } else {
      result.push(line);
    }
  });
  
  return result.join('\n');
}

function createHighlightedLine(line: string, selectedText: string): string {
  const trimmedSelection = selectedText.trim();
  
  if (trimmedSelection && line.includes(trimmedSelection)) {
    const escapedSelection = trimmedSelection.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
    return line.replace(new RegExp(escapedSelection, 'g'), `**${trimmedSelection}**`);
  }
  
  return `**${line}**`;
}

function getSelectionInLine(lineElement: HTMLElement, range: Range): string {
  try {
    const lineRange = document.createRange();
    lineRange.selectNodeContents(lineElement);
    
    const intersectionRange = document.createRange();
    
    const startContainer = lineElement.contains(range.startContainer) 
      ? range.startContainer 
      : lineRange.startContainer;
    const startOffset = lineElement.contains(range.startContainer) 
      ? range.startOffset 
      : lineRange.startOffset;
    
    const endContainer = lineElement.contains(range.endContainer) 
      ? range.endContainer 
      : lineRange.endContainer;
    const endOffset = lineElement.contains(range.endContainer) 
      ? range.endOffset 
      : lineRange.endOffset;
    
    intersectionRange.setStart(startContainer, startOffset);
    intersectionRange.setEnd(endContainer, endOffset);
    
    return intersectionRange.toString();
  } catch (error) {
    console.warn('Failed to get selection in line:', error);
    return '';
  }
}


export function buildContext(_container: HTMLElement): string {
  const selection = window.getSelection();
  if (!selection || !selection.rangeCount || selection.isCollapsed) {
    return "";
  }

  const range = selection.getRangeAt(0);
  
  // Find the code block containing the selection
  const codeBlock = findCodeBlock(range);
  if (!codeBlock) {
    return "";
  }

  // Extract lines and determine which are selected
  const lineElements = codeBlock.querySelectorAll('.znai-code-line');
  
  if (lineElements.length === 0) {
    // Handle code blocks without line spans
    return handleCodeBlockWithoutLines(codeBlock, range);
  }

  // Handle code blocks with line spans
  const lines: string[] = [];
  const selectedLines = new Set<number>();
  
  lineElements.forEach((lineElement, index) => {
    const lineText = lineElement.textContent || '';
    lines.push(lineText);
    
    // Check if this line intersects with the selection
    if (isLineIntersectingSelection(lineElement as HTMLElement, range)) {
      selectedLines.add(index);
    }
  });

  return buildOutput(lines, selectedLines, range, lineElements);
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
  // Check if any part of the selection intersects with this line
  const lineRange = document.createRange();
  lineRange.selectNodeContents(lineElement);
  
  try {
    // Check if ranges intersect
    const selectionStart = range.compareBoundaryPoints(Range.START_TO_END, lineRange);
    const selectionEnd = range.compareBoundaryPoints(Range.END_TO_START, lineRange);
    
    // Ranges intersect if selection doesn't end before line starts
    // and doesn't start after line ends
    return selectionStart >= 0 && selectionEnd <= 0;
  } catch {
    return false;
  }
}

function buildOutput(
  lines: string[], 
  selectedLines: Set<number>,
  range: Range,
  lineElements: NodeListOf<Element>
): string {
  const result: string[] = [];
  
  lines.forEach((line, index) => {
    if (selectedLines.has(index)) {
      const lineElement = lineElements[index] as HTMLElement;
      const selectedText = getSelectionInLine(lineElement, range);
      
      let highlightedLine;
      if (selectedText && selectedText.trim() && line.includes(selectedText.trim())) {
        const trimmedSelection = selectedText.trim();
        highlightedLine = line.replace(trimmedSelection, `**${trimmedSelection}**`);
      } else {
        highlightedLine = `**${line}**`;
      }
      
      result.push(highlightedLine + ' <----');
    } else {
      result.push(line);
    }
  });
  
  return result.join('\n');
}

function getSelectionInLine(lineElement: HTMLElement, range: Range): string {
  try {
    // Create intersection of line and selection
    const lineRange = document.createRange();
    lineRange.selectNodeContents(lineElement);
    
    const intersectionRange = document.createRange();
    
    // Find intersection start
    if (lineElement.contains(range.startContainer)) {
      intersectionRange.setStart(range.startContainer, range.startOffset);
    } else {
      intersectionRange.setStart(lineRange.startContainer, lineRange.startOffset);
    }
    
    // Find intersection end
    if (lineElement.contains(range.endContainer)) {
      intersectionRange.setEnd(range.endContainer, range.endOffset);
    } else {
      intersectionRange.setEnd(lineRange.endContainer, lineRange.endOffset);
    }
    
    return intersectionRange.toString();
  } catch {
    return '';
  }
}

function handleCodeBlockWithoutLines(codeBlock: HTMLElement, range: Range): string {
  const text = codeBlock.textContent || '';
  const lines = text.split('\n').filter(line => line.trim() || text.endsWith('\n'));
  
  // For simplicity, mark first line as selected
  const result: string[] = [];
  lines.forEach((line, index) => {
    if (index === 0) {
      const selectedText = range.toString();
      if (selectedText && line.includes(selectedText)) {
        const highlightedLine = line.replace(selectedText, `**${selectedText}**`);
        result.push(highlightedLine + ' <----');
      } else {
        result.push(`**${line}** <----`);
      }
    } else {
      result.push(line);
    }
  });
  
  return result.join('\n');
}
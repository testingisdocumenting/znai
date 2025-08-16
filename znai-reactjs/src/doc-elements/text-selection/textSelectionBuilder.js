// Constants
const MIN_CONTEXT_LENGTH = 10;
const MAX_CONTEXT_EXPANSION = 100;
const MAX_SEARCH_LENGTH = 100;

/**
 * Efficient text node navigator using TreeWalker.
 */
class TextNodeNavigator {
  /**
   * @param {Node} root - Root container to navigate within
   */
  constructor(root) {
    this.root = root;
    this.walker = document.createTreeWalker(root, NodeFilter.SHOW_TEXT, null, false);
  }

  /**
   * Gets the previous text node relative to the given node.
   * @param {Node} node - Current node
   * @returns {Node|null} Previous text node or null if not found
   */
  getPreviousTextNode(node) {
    this.walker.currentNode = node;
    return this.walker.previousNode();
  }

  /**
   * Gets the next text node relative to the given node.
   * @param {Node} node - Current node
   * @returns {Node|null} Next text node or null if not found
   */
  getNextTextNode(node) {
    this.walker.currentNode = node;
    return this.walker.nextNode();
  }
}

/**
 * Builds full text exactly like TextHighlighter does.
 * @param {HTMLElement} container - The container element
 * @returns {string} The full text as seen by TextHighlighter
 */
function buildFullTextUsingTreeWalker(container) {
  const textNodes = [];
  const walker = document.createTreeWalker(container, NodeFilter.SHOW_TEXT, null, false);

  let textNode;
  while ((textNode = walker.nextNode())) {
    textNodes.push(textNode);
  }

  return textNodes.map((node) => node.nodeValue).join("");
}

/**
 * Extracts text from selection range using TreeWalker method - same as TextHighlighter
 * @param {Range} range - The selection range
 * @param {HTMLElement} container - The container to search within
 * @returns {string} Text as TreeWalker sees it
 */
function extractTextFromRangeUsingTreeWalker(range, container) {
  // Get all text nodes in the container (same as TextHighlighter)
  const textNodes = [];
  const walker = document.createTreeWalker(container, NodeFilter.SHOW_TEXT, null, false);

  let textNode;
  while ((textNode = walker.nextNode())) {
    textNodes.push(textNode);
  }

  // Find which text nodes are affected by the range
  let result = "";

  for (const node of textNodes) {
    if (range.intersectsNode && range.intersectsNode(node)) {
      // This text node intersects with our range
      if (node === range.startContainer && node === range.endContainer) {
        // Selection is entirely within this single text node
        result += node.nodeValue.substring(range.startOffset, range.endOffset);
      } else if (node === range.startContainer) {
        // This is the starting text node
        result += node.nodeValue.substring(range.startOffset);
      } else if (node === range.endContainer) {
        // This is the ending text node
        result += node.nodeValue.substring(0, range.endOffset);
      } else {
        // This text node is completely within the selection
        result += node.nodeValue;
      }
    }
  }

  return result;
}

export function findPrefixSuffixAndMatch(container) {
  const selection = window.getSelection();

  if (!selection.rangeCount || selection.isCollapsed) {
    return null;
  }

  const range = selection.getRangeAt(0);
  const selectedText = extractTextFromRangeUsingTreeWalker(range, container).trim();

  if (!selectedText) {
    return null;
  }

  const fullText = buildFullTextUsingTreeWalker(container);

  const selectionIndex = fullText.indexOf(selectedText);

  if (selectionIndex === -1) {
    // Enhanced debugging for the real browser issue
    console.group("textSelectionBuilder: DEBUG INFO FOR NULL RESULT");
    console.warn("Could not find selected text in TreeWalker full text");
    console.log("Selected text length:", selectedText.length);
    console.log("Selected text preview:", JSON.stringify(selectedText.substring(0, 100)));
    console.log(
      "Selected text char codes:",
      Array.from(selectedText.substring(0, 20)).map((c) => `${c}(${c.charCodeAt(0)})`)
    );

    console.log("TreeWalker full text length:", fullText.length);
    console.log("TreeWalker full text preview:", JSON.stringify(fullText.substring(0, 200)));
    console.log(
      "TreeWalker char codes:",
      Array.from(fullText.substring(0, 20)).map((c) => `${c}(${c.charCodeAt(0)})`)
    );

    // Check if we can find the selected text with different approaches
    const normalizedSelection = selectedText.replace(/\s+/g, " ");
    const normalizedFullText = fullText.replace(/\s+/g, " ");
    const normalizedFound = normalizedFullText.indexOf(normalizedSelection);
    console.log("Found with normalized whitespace:", normalizedFound !== -1, "at index:", normalizedFound);

    // Check if we can find the first few words
    const firstWords = selectedText.split(/\s+/).slice(0, 3).join(" ");
    const lastWords = selectedText.split(/\s+/).slice(-3).join(" ");
    console.log("First words found:", fullText.indexOf(firstWords) !== -1, "text:", JSON.stringify(firstWords));
    console.log("Last words found:", fullText.indexOf(lastWords) !== -1, "text:", JSON.stringify(lastWords));

    // Show selection range info
    const range = selection.getRangeAt(0);
    console.log("Selection range info:");
    console.log("  startContainer:", range.startContainer.nodeName, range.startContainer.nodeType);
    console.log("  endContainer:", range.endContainer.nodeName, range.endContainer.nodeType);
    console.log("  startOffset:", range.startOffset);
    console.log("  endOffset:", range.endOffset);
    console.log("  commonAncestor:", range.commonAncestorContainer.nodeName);

    // Show what browser selection.toString() vs what TreeWalker sees
    console.log("Browser selection.toString():", JSON.stringify(selection.toString()));
    console.log("TreeWalker would see as text:", JSON.stringify(fullText.substring(0, 200)));

    console.groupEnd();
    return null;
  }

  // Extract prefix and suffix from the same full text that highlighter uses
  const actualPrefix = fullText.substring(Math.max(0, selectionIndex - MAX_CONTEXT_EXPANSION), selectionIndex);
  const actualSuffix = fullText.substring(
    selectionIndex + selectedText.length,
    selectionIndex + selectedText.length + MAX_CONTEXT_EXPANSION
  );

  // Check if the text is unique in the highlighter's full text
  const isUniqueText = isUniqueOccurrence(fullText, selectedText);

  if (isUniqueText) {
    return {
      text: selectedText,
      prefix: actualPrefix.slice(-MIN_CONTEXT_LENGTH) || actualPrefix,
      suffix: actualSuffix.slice(0, MIN_CONTEXT_LENGTH) || actualSuffix,
    };
  }

  // Find minimal prefix/suffix combination that makes the selection unique
  const result = findMinimalUniqueContext(fullText, selectedText, actualPrefix, actualSuffix);

  if (result) {
    return result;
  }

  // Fallback to minimum context if no unique combination found
  return {
    text: selectedText,
    prefix: actualPrefix.slice(-Math.max(MIN_CONTEXT_LENGTH, actualPrefix.length)),
    suffix: actualSuffix.slice(0, Math.max(MIN_CONTEXT_LENGTH, actualSuffix.length)),
  };
}

function isUniqueOccurrence(text, substring) {
  let count = 0;
  let idx = 0;

  for (;;) {
    idx = text.indexOf(substring, idx);
    if (idx === -1) {
      break;
    }

    idx = idx + substring.length;
    count++;
  }

  return count === 1;
}

/**
 * Finds the minimal prefix and suffix combination that uniquely identifies the selected text.
 * @param {string} fullText - The full text to search within
 * @param {string} selectedText - The selected text
 * @param {string} actualPrefix - The actual prefix around the selection
 * @param {string} actualSuffix - The actual suffix around the selection
 * @returns {Object|null} Object with text, prefix, and suffix, or null if not found
 */
function findMinimalUniqueContext(fullText, selectedText, actualPrefix, actualSuffix) {
  // Try different combinations starting with minimum length
  for (let totalLen = MIN_CONTEXT_LENGTH * 2; totalLen <= MAX_SEARCH_LENGTH; totalLen++) {
    for (
      let prefixLen = MIN_CONTEXT_LENGTH;
      prefixLen <= totalLen - MIN_CONTEXT_LENGTH && prefixLen <= actualPrefix.length;
      prefixLen++
    ) {
      const suffixLen = totalLen - prefixLen;
      if (suffixLen < MIN_CONTEXT_LENGTH || suffixLen > actualSuffix.length) {
        continue;
      }

      const testPrefix = actualPrefix.slice(-prefixLen);
      const testSuffix = actualSuffix.slice(0, suffixLen);
      const pattern = testPrefix + selectedText + testSuffix;

      if (isUniqueOccurrence(fullText, pattern)) {
        return {
          text: selectedText,
          prefix: testPrefix,
          suffix: testSuffix,
        };
      }
    }
  }

  return null;
}

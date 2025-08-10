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
 * Expands a text range by a specified number of characters in both directions.
 * @param {Range} range - The range to expand
 * @param {number} charsBefore - Number of characters to expand backward
 * @param {number} charsAfter - Number of characters to expand forward
 * @param {TextNodeNavigator} navigator - Text node navigator instance
 * @returns {string} The text content of the expanded range
 */
function getExpandedContext(range, charsBefore, charsAfter, navigator) {
  const expandedRange = range.cloneRange();

  // Expand backwards
  expandRange(expandedRange, charsBefore, navigator, true);

  // Expand forwards
  expandRange(expandedRange, charsAfter, navigator, false);

  return expandedRange.toString();
}

/**
 * Expands a range in a specific direction.
 * @param {Range} range - The range to expand
 * @param {number} charsToMove - Number of characters to expand
 * @param {TextNodeNavigator} navigator - Text node navigator instance
 * @param {boolean} isBackward - True to expand backward, false for forward
 */
function expandRange(range, charsToMove, navigator, isBackward) {
  try {
    let moved = 0;
    let currentContainer = isBackward ? range.startContainer : range.endContainer;
    let currentOffset = isBackward ? range.startOffset : range.endOffset;

    while (moved < charsToMove) {
      if (currentContainer.nodeType !== Node.TEXT_NODE) {
        break;
      }

      const availableChars = isBackward
        ? currentOffset
        : currentContainer.textContent.length - currentOffset;

      const toMove = Math.min(charsToMove - moved, availableChars);

      if (isBackward) {
        range.setStart(currentContainer, currentOffset - toMove);
      } else {
        range.setEnd(currentContainer, currentOffset + toMove);
      }

      moved += toMove;

      if (moved >= charsToMove) {
        break;
      }

      // Move to adjacent text node
      const adjacentNode = isBackward
        ? navigator.getPreviousTextNode(currentContainer)
        : navigator.getNextTextNode(currentContainer);

      if (!adjacentNode) {
        break;
      }

      currentContainer = adjacentNode;
      currentOffset = isBackward ? adjacentNode.textContent.length : 0;
    }
  } catch (e) {
    // Silently handle range expansion errors
  }
}

/**
 * Finds minimal unique prefix and suffix for the selected text within the container.
 * @param {HTMLElement} container - The container element to search within
 * @returns {Object|null} Object with text, prefix, and suffix properties, or null if error
 */
export function findPrefixSuffixAndMatch(container) {
  const selection = window.getSelection();
  const selectedText = selection.toString().trim();

  if (!selectedText) {
    return null;
  }

  const range = selection.getRangeAt(0);
  const navigator = new TextNodeNavigator(container);

  // Get a large context around the selection to identify it uniquely
  const largeContext = getExpandedContext(
    range,
    MAX_CONTEXT_EXPANSION,
    MAX_CONTEXT_EXPANSION,
    navigator
  );

  const selectionStartInContext = largeContext.indexOf(selectedText);

  if (selectionStartInContext === -1) {
    // Try with normalized whitespace in case of formatting differences
    const normalizedContext = largeContext.replace(/\s+/g, ' ').trim();
    const normalizedSelection = selectedText.replace(/\s+/g, ' ').trim();
    const normalizedIndex = normalizedContext.indexOf(normalizedSelection);
    
    if (normalizedIndex === -1) {
      console.warn('textSelectionBuilder: Could not find selected text in expanded context', {
        selectedText: selectedText.substring(0, 50) + '...',
        contextLength: largeContext.length,
        contextPreview: largeContext.substring(0, 100) + '...'
      });
      return null;
    }
    
    // If normalized version found, use the original context with best effort
    const actualPrefix = largeContext.substring(0, Math.min(largeContext.length, normalizedIndex));
    const actualSuffix = largeContext.substring(Math.min(largeContext.length, normalizedIndex + selectedText.length));
    
    return {
      text: selectedText,
      prefix: actualPrefix.slice(-MIN_CONTEXT_LENGTH) || actualPrefix,
      suffix: actualSuffix.slice(0, MIN_CONTEXT_LENGTH) || actualSuffix,
    };
  }

  // Extract the actual prefix and suffix from the large context
  const actualPrefix = largeContext.substring(0, selectionStartInContext);
  const actualSuffix = largeContext.substring(selectionStartInContext + selectedText.length);

  // Get the full text to test uniqueness
  const fullText = container.innerText;

  // Check if the selected text is unique
  const isUniqueText = countOccurrences(fullText, selectedText) === 1;

  if (isUniqueText) {
    return {
      text: selectedText,
      prefix: actualPrefix.slice(-MIN_CONTEXT_LENGTH) || actualPrefix,
      suffix: actualSuffix.slice(0, MIN_CONTEXT_LENGTH) || actualSuffix,
    };
  }

  // Find minimal prefix/suffix combination that makes the selection unique
  const result = findMinimalUniqueContext(
    fullText,
    selectedText,
    actualPrefix,
    actualSuffix
  );

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

/**
 * Counts occurrences of a substring in a string.
 * @param {string} text - The text to search in
 * @param {string} substring - The substring to count
 * @returns {number} Number of occurrences
 */
function countOccurrences(text, substring) {
  let count = 0;
  let idx = text.indexOf(substring);
  while (idx !== -1) {
    count++;
    idx = text.indexOf(substring, idx + 1);
  }
  return count;
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

      if (countOccurrences(fullText, pattern) === 1) {
        let finalPrefix = testPrefix;

        // Try to clean up to word boundary if possible
        finalPrefix = cleanToWordBoundary(
          finalPrefix,
          selectedText,
          testSuffix,
          fullText
        );

        return {
          text: selectedText,
          prefix: finalPrefix,
          suffix: testSuffix,
        };
      }
    }
  }

  return null;
}

/**
 * Attempts to clean the prefix to a word boundary while maintaining uniqueness.
 * @param {string} prefix - The prefix to clean
 * @param {string} selectedText - The selected text
 * @param {string} suffix - The suffix
 * @param {string} fullText - The full text to verify uniqueness
 * @returns {string} The cleaned prefix or original if cleaning breaks uniqueness
 */
function cleanToWordBoundary(prefix, selectedText, suffix, fullText) {
  if (prefix.length <= MIN_CONTEXT_LENGTH + 5) {
    return prefix;
  }

  const lastSpace = prefix.lastIndexOf(" ");
  if (lastSpace <= MIN_CONTEXT_LENGTH) {
    return prefix;
  }

  const cleanPrefix = prefix.substring(lastSpace + 1);
  if (cleanPrefix.length < MIN_CONTEXT_LENGTH) {
    return prefix;
  }

  // Verify the cleaned prefix still results in unique pattern
  const cleanPattern = cleanPrefix + selectedText + suffix;
  if (countOccurrences(fullText, cleanPattern) === 1) {
    return cleanPrefix;
  }

  return prefix;
}
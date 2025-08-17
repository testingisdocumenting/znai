// Constants
import { createSelectionExpander, getSelectionText } from "./selectionUtils.js";

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

export function findPrefixSuffixAndMatch(container) {
  let result = {
    prefix: "",
    selection: "",
    suffix: "",
  };
  const selection = window.getSelection();

  if (!selection.rangeCount || selection.isCollapsed) {
    return result;
  }

  const fullText = buildFullTextUsingTreeWalker(container);

  const expandSelection = createSelectionExpander(container);
  for (let i = 0; i < 10; i++) {
    result = expandSelection();
    const maybeUniqueText = result.prefix + result.selection + result.suffix;

    if (isUniqueOccurrence(fullText, maybeUniqueText)) {
      break;
    }
  }

  return result;
}

function isUniqueOccurrence(text, substring) {
  if (substring.length === 0) {
    return false;
  }

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

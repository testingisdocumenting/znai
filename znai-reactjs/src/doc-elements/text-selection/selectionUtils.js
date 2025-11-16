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

/**
 * Finds the first or last text node within a given node.
 */
function findTextNode(node, findLast = false) {
  if (node.nodeType === Node.TEXT_NODE) {
    return node;
  }

  const walker = document.createTreeWalker(node, NodeFilter.SHOW_TEXT);

  if (findLast) {
    let lastTextNode = null;
    while (walker.nextNode()) {
      lastTextNode = walker.currentNode;
    }
    return lastTextNode;
  }

  return walker.nextNode();
}

/**
 * Finds a text node before the given node by searching siblings and ancestors.
 */
function findPrecedingTextNode(node) {
  // Search previous siblings
  let sibling = node.previousSibling;
  while (sibling) {
    const textNode = findTextNode(sibling, true);
    if (textNode) {
      return textNode;
    }
    sibling = sibling.previousSibling;
  }

  // Search parent's previous siblings
  if (node.parentNode) {
    let parentSibling = node.parentNode.previousSibling;
    while (parentSibling) {
      const textNode = findTextNode(parentSibling, true);
      if (textNode) {
        return textNode;
      }
      parentSibling = parentSibling.previousSibling;
    }
  }

  // Last resort: search all preceding nodes in ancestor
  let ancestor = node.parentNode;
  while (ancestor) {
    const walker = document.createTreeWalker(
      ancestor,
      NodeFilter.SHOW_TEXT,
      {
        acceptNode: (textNode) => {
          const position = node.compareDocumentPosition(textNode);
          return position & Node.DOCUMENT_POSITION_PRECEDING
            ? NodeFilter.FILTER_ACCEPT
            : NodeFilter.FILTER_SKIP;
        },
      }
    );

    let lastTextNode = null;
    while (walker.nextNode()) {
      lastTextNode = walker.currentNode;
    }

    if (lastTextNode) {
      return lastTextNode;
    }

    ancestor = ancestor.parentNode;
  }

  return null;
}

/**
 * Normalizes a range boundary point to always be a text node.
 * When triple-clicking, browsers often create ranges with element nodes as containers.
 * This function converts such boundaries to text node boundaries.
 *
 * @param {Node} node - The container node (text or element)
 * @param {number} offset - The offset within the container
 * @param {boolean} isEnd - Whether this is an end boundary (affects navigation for element nodes)
 * @returns {{node: Node, offset: number}|null} Normalized boundary with text node, or null if no text node found
 */
function normalizeRangeBoundary(node, offset, isEnd = false) {
  // Already a text node - return as-is
  if (node.nodeType === Node.TEXT_NODE) {
    return { node, offset };
  }

  // Not an element node - can't normalize
  if (node.nodeType !== Node.ELEMENT_NODE) {
    return null;
  }

  // Special case: end boundary at offset 0 means selection ends before this element
  if (isEnd && offset === 0) {
    const textNode = findPrecedingTextNode(node);
    return textNode ? { node: textNode, offset: textNode.nodeValue.length } : null;
  }

  // Boundary at element's edge (start at 0, end at childNodes.length)
  if (offset === 0 || offset === node.childNodes.length) {
    const findLast = isEnd && offset === node.childNodes.length;
    const textNode = findTextNode(node, findLast);
    if (!textNode) {
      return null;
    }
    return { node: textNode, offset: findLast ? textNode.nodeValue.length : 0 };
  }

  // Boundary points to a specific child
  const targetChild = isEnd ? node.childNodes[offset - 1] : node.childNodes[offset];
  if (!targetChild) {
    return null;
  }

  // Child is a text node - use it directly
  if (targetChild.nodeType === Node.TEXT_NODE) {
    return { node: targetChild, offset: isEnd ? targetChild.nodeValue.length : 0 };
  }

  // Child is an element - find text node within it
  const textNode = findTextNode(targetChild, isEnd);
  if (!textNode) {
    return null;
  }
  return { node: textNode, offset: isEnd ? textNode.nodeValue.length : 0 };
}

export function getSelectionText(range) {
  const start = normalizeRangeBoundary(range.startContainer, range.startOffset, false);
  const end = normalizeRangeBoundary(range.endContainer, range.endOffset, true);

  // If we can't normalize the boundaries, fall back to empty string
  if (!start || !end) {
    return "";
  }

  const walker = document.createTreeWalker(range.commonAncestorContainer, NodeFilter.SHOW_TEXT);

  walker.currentNode = start.node;

  let text = "";

  if (start.node === end.node) {
    return start.node.nodeValue.substring(start.offset, end.offset);
  }

  text += start.node.nodeValue.substring(start.offset);

  while (walker.nextNode()) {
    const node = walker.currentNode;
    if (node === end.node) {
      text += node.nodeValue.substring(0, end.offset);
      break;
    }
    text += node.nodeValue;
  }

  return text;
}

class TextExpander {
  constructor(node, offset, forward, containerElement) {
    this.container = containerElement;
    this.walker = document.createTreeWalker(this.container, NodeFilter.SHOW_TEXT);
    this.walker.currentNode = node;
    this.currentNode = node;
    this.currentOffset = offset;
    this.forward = forward;
    this.accumulated = "";
    this.exhausted = false;

    // If we're at a node boundary, move to the next node immediately
    if (forward && node.nodeValue && offset === node.nodeValue.length) {
      this.moveToNextNode();
    } else if (!forward && offset === 0) {
      this.moveToPreviousNode();
    }
  }

  moveToNextNode() {
    this.walker.currentNode = this.currentNode;
    if (this.walker.nextNode()) {
      this.currentNode = this.walker.currentNode;
      this.currentOffset = 0;
      return true;
    } else {
      this.exhausted = true;
      return false;
    }
  }

  moveToPreviousNode() {
    this.walker.currentNode = this.currentNode;
    if (this.walker.previousNode()) {
      this.currentNode = this.walker.currentNode;
      this.currentOffset = this.currentNode.nodeValue.length;
      return true;
    } else {
      this.exhausted = true;
      return false;
    }
  }

  expand(chars = 10) {
    if (this.exhausted || !this.currentNode) return "";

    let text = "";
    let remaining = chars;

    while (remaining > 0 && this.currentNode) {
      const nodeText = this.currentNode.nodeValue;
      const available = this.forward ? (nodeText ? nodeText.length - this.currentOffset : 0) : this.currentOffset;

      if (available === 0) {
        // We're at node boundary, move to next node
        const moved = this.forward ? this.moveToNextNode() : this.moveToPreviousNode();

        if (!moved) {
          break;
        } else {
          continue;
        }
      }

      const availableText = this.forward
        ? nodeText.substring(this.currentOffset)
        : nodeText.substring(0, this.currentOffset);

      if (availableText.length <= remaining) {
        // Take all available
        text = this.forward ? text + availableText : availableText + text;
        remaining -= availableText.length;

        // Update offset to indicate we've consumed this node
        this.currentOffset = this.forward ? nodeText.length : 0;
      } else {
        // Take partial
        const chunk = this.forward
          ? availableText.substring(0, remaining)
          : availableText.substring(availableText.length - remaining);

        text = this.forward ? text + chunk : chunk + text;
        this.currentOffset += this.forward ? remaining : -remaining;
        remaining = 0;
      }
    }

    this.accumulated = this.forward ? this.accumulated + text : text + this.accumulated;

    return text;
  }

  getAccumulated() {
    return this.accumulated;
  }
}

export function createSelectionExpander(container) {
  const selection = window.getSelection();
  if (!selection.rangeCount) {
    return function () {
      return {
        prefix: "",
        selection: "",
        suffix: "",
      };
    };
  }

  const range = selection.getRangeAt(0);

  // Normalize range boundaries to handle triple-click selections with element nodes
  let start = normalizeRangeBoundary(range.startContainer, range.startOffset, false);
  let end = normalizeRangeBoundary(range.endContainer, range.endOffset, true);

  // Fallback: if normalization fails but the original boundaries are text nodes, use them directly
  if (!start && range.startContainer.nodeType === Node.TEXT_NODE) {
    start = { node: range.startContainer, offset: range.startOffset };
  }
  if (!end && range.endContainer.nodeType === Node.TEXT_NODE) {
    end = { node: range.endContainer, offset: range.endOffset };
  }

  // If we still can't get valid boundaries, return empty result
  if (!start || !end) {
    return function () {
      return {
        prefix: "",
        selection: "",
        suffix: "",
      };
    };
  }

  const selectionText = getSelectionText(range);
  const prefixExpander = new TextExpander(start.node, start.offset, false, container);
  const suffixExpander = new TextExpander(end.node, end.offset, true, container);

  return function () {
    prefixExpander.expand(10);
    suffixExpander.expand(10);

    return {
      prefix: prefixExpander.getAccumulated(),
      selection: selectionText,
      suffix: suffixExpander.getAccumulated(),
    };
  };
}

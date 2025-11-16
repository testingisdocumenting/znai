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
 * Normalizes element node boundaries to text nodes.
 *
 * When a browser provides an element node as a range boundary with offset N,
 * it represents a position between the element's children (between child N-1 and N).
 *
 * For start boundaries: Find the first text node at or after this position
 * For end boundaries: Find the last text node before this position
 */
function normalizeRangeBoundary(node, offset, isEnd = false) {
  // Already a text node - return as-is
  if (node.nodeType === Node.TEXT_NODE) {
    return { node, offset };
  }

  // Only handle element nodes
  if (node.nodeType !== Node.ELEMENT_NODE) {
    return null;
  }

  try {
    if (isEnd) {
      // End boundary: find the last text node before position 'offset'
      // This means looking backward from offset
      return findTextNodeBackward(node, offset);
    } else {
      // Start boundary: find the first text node at or after position 'offset'
      // This means looking forward from offset
      return findTextNodeForward(node, offset);
    }
  } catch (e) {
    return null;
  }
}

/**
 * Finds the first text node at or after the given offset position in an element.
 * Used for start boundaries.
 */
function findTextNodeForward(element, offset) {
  // First, try to find text within children starting from offset
  for (let i = offset; i < element.childNodes.length; i++) {
    const child = element.childNodes[i];

    if (child.nodeType === Node.TEXT_NODE) {
      return { node: child, offset: 0 };
    }

    if (child.nodeType === Node.ELEMENT_NODE) {
      const textNode = findFirstTextNode(child);
      if (textNode) return textNode;
    }
  }

  // No text node found among this element's children
  return null;
}

/**
 * Finds the last text node before the given offset position in an element.
 * Used for end boundaries.
 */
function findTextNodeBackward(element, offset) {
  // If offset is 0, need to look before this element entirely
  if (offset === 0) {
    return findPrecedingTextNode(element);
  }

  // Search backward through children before offset
  for (let i = offset - 1; i >= 0; i--) {
    const child = element.childNodes[i];

    if (child.nodeType === Node.TEXT_NODE) {
      return { node: child, offset: child.nodeValue.length };
    }

    if (child.nodeType === Node.ELEMENT_NODE) {
      const textNode = findLastTextNode(child);
      if (textNode) return textNode;
    }
  }

  // No text node found among this element's children
  return null;
}

/**
 * Finds the first text node within an element.
 */
function findFirstTextNode(element) {
  const walker = document.createTreeWalker(element, NodeFilter.SHOW_TEXT);
  const first = walker.nextNode();
  return first ? { node: first, offset: 0 } : null;
}

/**
 * Finds the last text node within an element.
 */
function findLastTextNode(element) {
  const walker = document.createTreeWalker(element, NodeFilter.SHOW_TEXT);
  let last = null;
  while (walker.nextNode()) {
    last = walker.currentNode;
  }
  return last ? { node: last, offset: last.nodeValue.length } : null;
}

/**
 * Finds the text node that precedes a given element in document order.
 * Used for end boundaries that point to the start of an element.
 */
function findPrecedingTextNode(element) {
  // Check previous sibling and its descendants
  let sibling = element.previousSibling;
  while (sibling) {
    if (sibling.nodeType === Node.TEXT_NODE && sibling.nodeValue.length > 0) {
      return { node: sibling, offset: sibling.nodeValue.length };
    }
    if (sibling.nodeType === Node.ELEMENT_NODE) {
      const textNode = findLastTextNode(sibling);
      if (textNode) return textNode;
    }
    sibling = sibling.previousSibling;
  }

  // Check parent's previous siblings and ancestors
  let parent = element.parentNode;
  while (parent && parent.nodeType === Node.ELEMENT_NODE) {
    let parentSibling = parent.previousSibling;
    while (parentSibling) {
      if (parentSibling.nodeType === Node.TEXT_NODE && parentSibling.nodeValue.length > 0) {
        return { node: parentSibling, offset: parentSibling.nodeValue.length };
      }
      if (parentSibling.nodeType === Node.ELEMENT_NODE) {
        const textNode = findLastTextNode(parentSibling);
        if (textNode) return textNode;
      }
      parentSibling = parentSibling.previousSibling;
    }
    parent = parent.parentNode;
  }

  return null;
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

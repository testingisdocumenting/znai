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
 * Handles cases like triple-click selections where browsers use element nodes as boundaries.
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
    // Strategy 1: Check if the child at offset is a text node
    const childAtOffset = node.childNodes[offset];
    if (childAtOffset?.nodeType === Node.TEXT_NODE) {
      return { node: childAtOffset, offset: 0 };
    }

    // Strategy 2: Check the previous child (for end boundaries or when offset > 0)
    if (offset > 0) {
      const prevChild = node.childNodes[offset - 1];
      if (prevChild?.nodeType === Node.TEXT_NODE) {
        return { node: prevChild, offset: prevChild.nodeValue.length };
      }

      // If previous child is an element, find text node within it
      if (prevChild?.nodeType === Node.ELEMENT_NODE) {
        const textNode = findTextNodeInElement(prevChild, isEnd);
        if (textNode) return textNode;
      }
    }

    // Strategy 3: Special case for end boundary at offset 0
    // This happens when selection ends right before an element starts
    if (isEnd && offset === 0) {
      return findPrecedingTextNode(node);
    }

    // Strategy 4: If child at offset is an element, find text node within it
    if (childAtOffset?.nodeType === Node.ELEMENT_NODE) {
      const textNode = findTextNodeInElement(childAtOffset, isEnd);
      if (textNode) return textNode;
    }

    // Strategy 5: Fallback - search within the element itself
    return findTextNodeInElement(node, isEnd);

  } catch (e) {
    return null;
  }
}

/**
 * Finds a text node within an element, either first or last depending on position.
 */
function findTextNodeInElement(element, preferLast = false) {
  const walker = document.createTreeWalker(element, NodeFilter.SHOW_TEXT);

  if (preferLast) {
    let last = null;
    while (walker.nextNode()) {
      last = walker.currentNode;
    }
    return last ? { node: last, offset: last.nodeValue.length } : null;
  }

  const first = walker.nextNode();
  return first ? { node: first, offset: 0 } : null;
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
      const textNode = findTextNodeInElement(sibling, true);
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
        const textNode = findTextNodeInElement(parentSibling, true);
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

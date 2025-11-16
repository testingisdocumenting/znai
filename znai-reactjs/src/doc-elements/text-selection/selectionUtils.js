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
  // If already a text node, return as-is
  if (node.nodeType === Node.TEXT_NODE) {
    console.debug("normalizeRangeBoundary: already text node", { isEnd });
    return { node, offset };
  }

  // Element node - the offset refers to child node index
  if (node.nodeType === Node.ELEMENT_NODE) {
    console.debug("normalizeRangeBoundary: element node", {
      nodeName: node.nodeName,
      offset,
      isEnd,
      childNodesLength: node.childNodes.length,
    });

    // For start boundary at offset 0, get first text node in the element
    if (offset === 0 && !isEnd) {
      const walker = document.createTreeWalker(node, NodeFilter.SHOW_TEXT);
      const firstTextNode = walker.nextNode();
      console.debug("normalizeRangeBoundary: start at 0", { found: !!firstTextNode });
      if (!firstTextNode) {
        return null;
      }
      return { node: firstTextNode, offset: 0 };
    }

    // For end boundary at childNodes.length, get last text node in the element
    if (isEnd && offset === node.childNodes.length) {
      const walker = document.createTreeWalker(node, NodeFilter.SHOW_TEXT);
      let lastTextNode = null;
      while (walker.nextNode()) {
        lastTextNode = walker.currentNode;
      }
      console.debug("normalizeRangeBoundary: end at childNodes.length", { found: !!lastTextNode });
      if (!lastTextNode) {
        return null;
      }
      return { node: lastTextNode, offset: lastTextNode.nodeValue.length };
    }

    // For other offsets, the offset refers to a position among direct children
    // offset N means: for start, before child N; for end, after child N-1
    let targetChild;
    if (isEnd) {
      // For end boundary, we want the last text node before this offset
      // offset N means after child N-1
      targetChild = node.childNodes[offset - 1];
    } else {
      // For start boundary, we want the first text node at/after this offset
      // offset N means before child N
      targetChild = node.childNodes[offset];
    }

    console.debug("normalizeRangeBoundary: other offset", {
      targetChild: targetChild?.nodeName,
      targetChildType: targetChild?.nodeType,
    });

    if (!targetChild) {
      console.debug("normalizeRangeBoundary: no target child found");
      return null;
    }

    // If the target child is already a text node, use it directly
    if (targetChild.nodeType === Node.TEXT_NODE) {
      console.debug("normalizeRangeBoundary: target child is text node");
      if (isEnd) {
        return { node: targetChild, offset: targetChild.nodeValue.length };
      } else {
        return { node: targetChild, offset: 0 };
      }
    }

    // Otherwise, find the appropriate text node within this child element
    const walker = document.createTreeWalker(targetChild, NodeFilter.SHOW_TEXT);

    if (isEnd) {
      // Get last text node in this child
      let lastTextNode = null;
      while (walker.nextNode()) {
        lastTextNode = walker.currentNode;
      }
      console.debug("normalizeRangeBoundary: found last text in child", { found: !!lastTextNode });
      if (!lastTextNode) {
        return null;
      }
      return { node: lastTextNode, offset: lastTextNode.nodeValue.length };
    } else {
      // Get first text node in this child
      const firstTextNode = walker.nextNode();
      console.debug("normalizeRangeBoundary: found first text in child", { found: !!firstTextNode });
      if (!firstTextNode) {
        return null;
      }
      return { node: firstTextNode, offset: 0 };
    }
  }

  // For other node types, return null (can't normalize)
  console.debug("normalizeRangeBoundary: unknown node type", { nodeType: node.nodeType });
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
    console.warn("Normalization failed but startContainer is text node, using original");
    start = { node: range.startContainer, offset: range.startOffset };
  }
  if (!end && range.endContainer.nodeType === Node.TEXT_NODE) {
    console.warn("Normalization failed but endContainer is text node, using original");
    end = { node: range.endContainer, offset: range.endOffset };
  }

  // If we still can't get valid boundaries, return empty result
  if (!start || !end) {
    console.warn("Failed to normalize range boundaries", {
      startContainer: range.startContainer,
      startOffset: range.startOffset,
      endContainer: range.endContainer,
      endOffset: range.endOffset,
      startNormalized: start,
      endNormalized: end,
    });
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

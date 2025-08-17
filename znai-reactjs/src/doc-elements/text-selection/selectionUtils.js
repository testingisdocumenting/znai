export function getSelectionText(range) {
  const walker = document.createTreeWalker(range.commonAncestorContainer, NodeFilter.SHOW_TEXT);

  walker.currentNode = range.startContainer;

  let text = "";

  if (range.startContainer === range.endContainer) {
    return range.startContainer.nodeValue.substring(range.startOffset, range.endOffset);
  }

  text += range.startContainer.nodeValue.substring(range.startOffset);

  while (walker.nextNode()) {
    const node = walker.currentNode;
    if (node === range.endContainer) {
      text += node.nodeValue.substring(0, range.endOffset);
      break;
    }
    text += node.nodeValue;
  }

  return text;
}

class TextExpander {
  constructor(node, offset, forward) {
    this.walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
    this.walker.currentNode = node;
    this.currentNode = node;
    this.currentOffset = offset;
    this.forward = forward;
    this.accumulated = "";
    this.exhausted = false;

    // If we're at a node boundary, move to the next node immediately
    if (forward && offset === node.nodeValue.length) {
      if (this.walker.nextNode()) {
        this.currentNode = this.walker.currentNode;
        this.currentOffset = 0;
      } else {
        this.exhausted = true;
      }
    } else if (!forward && offset === 0) {
      if (this.walker.previousNode()) {
        this.currentNode = this.walker.currentNode;
        this.currentOffset = this.currentNode.nodeValue.length;
      } else {
        this.exhausted = true;
      }
    }
  }

  expand(chars = 10) {
    if (this.exhausted || !this.currentNode) return "";

    let text = "";
    let remaining = chars;

    while (remaining > 0 && this.currentNode) {
      const nodeText = this.currentNode.nodeValue;
      const available = this.forward ? nodeText.length - this.currentOffset : this.currentOffset;

      if (available === 0) {
        // We're at node boundary, move to next node
        this.walker.currentNode = this.currentNode;
        const moved = this.forward ? this.walker.nextNode() : this.walker.previousNode();

        if (moved) {
          this.currentNode = this.walker.currentNode;
          this.currentOffset = this.forward ? 0 : this.currentNode.nodeValue.length;
          continue;
        } else {
          this.exhausted = true;
          break;
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

export function createSelectionExpander() {
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

  const selectionText = getSelectionText(range);
  const prefixExpander = new TextExpander(range.startContainer, range.startOffset, false);
  const suffixExpander = new TextExpander(range.endContainer, range.endOffset, true);

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

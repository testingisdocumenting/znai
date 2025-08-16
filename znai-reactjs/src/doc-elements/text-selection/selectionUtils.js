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
  }

  expand(chars = 10) {
    if (this.exhausted) return "";

    let text = "";
    let remaining = chars;

    while (remaining > 0 && this.currentNode) {
      const nodeText = this.currentNode.nodeValue;
      const available = this.forward ? nodeText.length - this.currentOffset : this.currentOffset;

      if (available > 0) {
        const availableText = this.forward
          ? nodeText.substring(this.currentOffset)
          : nodeText.substring(0, this.currentOffset);

        if (availableText.length <= remaining) {
          // Take all available
          text += this.forward ? availableText : availableText + text;
          remaining -= availableText.length;

          // Move to next node
          this.walker.currentNode = this.currentNode;
          const moved = this.forward ? this.walker.nextNode() : this.walker.previousNode();

          if (moved) {
            this.currentNode = this.walker.currentNode;
            this.currentOffset = this.forward ? 0 : this.currentNode.nodeValue.length;
          } else {
            this.currentNode = null;
            this.exhausted = true;
          }
        } else {
          // Take partial
          const chunk = this.forward
            ? availableText.substring(0, remaining)
            : availableText.substring(availableText.length - remaining);

          text = this.forward ? text + chunk : chunk + text;
          this.currentOffset += this.forward ? remaining : -remaining;
          remaining = 0;
        }
      } else {
        // Current node has no more text, shouldn't happen
        break;
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
  // TODO error selection handler
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

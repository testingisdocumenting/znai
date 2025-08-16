import "./textHighlighter.css";

export class TextHighlighter {
  constructor(container) {
    this.container = container;
    this.highlights = [];
  }

  getTextNodes(node) {
    const textNodes = [];
    const walker = document.createTreeWalker(node, NodeFilter.SHOW_TEXT, null, false);

    let textNode;
    while ((textNode = walker.nextNode())) {
      textNodes.push(textNode);
    }
    return textNodes;
  }

  findMatches(searchText, prefix, suffix) {
    if (!searchText) return [];

    prefix = prefix || "";
    suffix = suffix || "";

    const textNodes = this.getTextNodes(this.container);
    const matches = [];

    // Build node map
    let currentPos = 0;
    const nodeMap = textNodes.map((node) => {
      const start = currentPos;
      const end = currentPos + node.nodeValue.length;
      currentPos = end;
      return { node, start, end, text: node.nodeValue };
    });

    const fullText = nodeMap.map((item) => item.text).join("");

    // Build a pattern
    let pattern = "";
    if (prefix) {
      pattern += "(" + this.escapeRegex(prefix) + ")\\s*";
    }
    pattern += "(" + this.escapeRegex(searchText) + ")";
    if (suffix) {
      pattern += "\\s*(" + this.escapeRegex(suffix) + ")";
    }

    try {
      const regex = new RegExp(pattern, "is"); // Only need first match since it should be unique
      const match = regex.exec(fullText);

      if (match) {
        let searchGroupIndex = prefix ? 2 : 1;
        let matchStart = match.index;

        if (prefix || suffix) {
          let groupStart = 0;
          for (let i = 1; i < searchGroupIndex; i++) {
            groupStart += match[i] ? match[i].length : 0;
          }
          matchStart = match.index + groupStart;
        }

        const matchEnd = matchStart + match[searchGroupIndex].length;

        const affectedNodes = [];
        nodeMap.forEach((nodeInfo) => {
          if (nodeInfo.end > matchStart && nodeInfo.start < matchEnd) {
            affectedNodes.push({
              node: nodeInfo.node,
              start: Math.max(0, matchStart - nodeInfo.start),
              end: Math.min(nodeInfo.text.length, matchEnd - nodeInfo.start),
              text: nodeInfo.text,
            });
          }
        });

        if (affectedNodes.length > 0) {
          matches.push({
            nodes: affectedNodes,
            fullMatch: match[0],
            searchMatch: match[searchGroupIndex],
          });
        }
      }
    } catch (e) {
      console.error("Regex error:", e);
    }

    return matches;
  }

  escapeRegex(str) {
    str = str.replace(/\\n/g, "\n");
    str = str.replace(/\\s/g, "\\s");
    return str.replace(/[.*+?^${}()|[\]\\]/g, "\\$&");
  }

  highlight(searchText, prefix, suffix, onClick) {
    this.clearHighlights();

    const matches = this.findMatches(searchText, prefix, suffix);

    matches.forEach((match, matchIndex) => {
      const highlightGroup = [];

      const handleMouseEnter = () => {
        highlightGroup.forEach((span) => {
          span.style.backgroundColor = "#fdd835";
        });
      };

      const handleMouseLeave = () => {
        highlightGroup.forEach((span) => {
          span.style.backgroundColor = "";
        });
      };

      const handleClick = (e) => {
        e.stopPropagation();
        highlightGroup.forEach((span) => {
          span.classList.add("clicked");
          setTimeout(() => span.classList.remove("clicked"), 500);
        });

        if (onClick) {
          onClick(e, {
            matchIndex,
            text: searchText,
            prefix,
            suffix,
            fullMatch: match.fullMatch,
            searchMatch: match.searchMatch,
          });
        }
      };

      match.nodes.forEach((nodeInfo, nodeIndex) => {
        const parent = nodeInfo.node.parentNode;
        if (!parent) return;

        const beforeText = nodeInfo.node.nodeValue.substring(0, nodeInfo.start);
        const highlightText = nodeInfo.node.nodeValue.substring(nodeInfo.start, nodeInfo.end);
        const afterText = nodeInfo.node.nodeValue.substring(nodeInfo.end);

        const highlightSpan = document.createElement("span");
        highlightSpan.className = "highlight";
        highlightSpan.textContent = highlightText;

        if (match.nodes.length === 1) {
          highlightSpan.classList.add("highlight-single");
        } else if (nodeIndex === 0) {
          highlightSpan.classList.add("highlight-start");
        } else if (nodeIndex === match.nodes.length - 1) {
          highlightSpan.classList.add("highlight-end");
        } else {
          highlightSpan.classList.add("highlight-middle");
        }

        const fragment = document.createDocumentFragment();
        if (beforeText) fragment.appendChild(document.createTextNode(beforeText));
        fragment.appendChild(highlightSpan);
        if (afterText) fragment.appendChild(document.createTextNode(afterText));

        parent.replaceChild(fragment, nodeInfo.node);
        highlightGroup.push(highlightSpan);
      });

      highlightGroup.forEach((span) => {
        span.addEventListener("mouseenter", handleMouseEnter);
        span.addEventListener("mouseleave", handleMouseLeave);
        span.addEventListener("click", handleClick);
      });

      this.highlights.push(highlightGroup);
    });

    return this.highlights.length;
  }

  clearHighlights() {
    this.highlights.forEach((group) => {
      group.forEach((span) => {
        const parent = span.parentNode;
        if (parent) {
          parent.replaceChild(document.createTextNode(span.textContent), span);
        }
      });
    });
    this.container.normalize();
    this.highlights = [];
  }
}

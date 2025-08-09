import "./textHighlighter.css";

export class TextHighlighter {
  constructor(container) {
    this.container = container;
    this.highlights = [];
  }

  // Find all text nodes in the container
  getTextNodes(node) {
    const textNodes = [];
    const walker = document.createTreeWalker(node, NodeFilter.SHOW_TEXT, {
      acceptNode: (node) => {
        return NodeFilter.FILTER_ACCEPT;
      },
    });

    let textNode;
    while ((textNode = walker.nextNode())) {
      textNodes.push(textNode);
    }
    return textNodes;
  }

  // Find matches with optional prefix/suffix context
  findMatches(searchText, prefix = "", suffix = "") {
    const textNodes = this.getTextNodes(this.container);
    const matches = [];

    // Build a map of text positions
    let currentPos = 0;
    const nodeMap = textNodes.map((node) => {
      const start = currentPos;
      const end = currentPos + node.nodeValue.length;
      currentPos = end;
      return { node, start, end, text: node.nodeValue };
    });

    // Get the full text
    const fullText = nodeMap.map((item) => item.text).join("");

    // Helper function to escape regex special chars but preserve \n and \s
    const escapeRegex = (str) => {
      try {
        // Replace literal \n with newline and \s with whitespace pattern
        let result = str.replace(/\\n/g, "\n");
        result = result.replace(/\\s/g, "\\s");

        // Escape special regex characters
        result = result.replace(/[.*+?^${}()|[\]\\]/g, (match, offset) => {
          // Check if it's a backslash followed by 's'
          if (match === "\\" && offset + 1 < result.length && result[offset + 1] === "s") {
            return match;
          }
          return "\\" + match;
        });

        return result;
      } catch (e) {
        console.error("Error in escapeRegex:", e);
        return str;
      }
    };

    // Build the search pattern with optional prefix/suffix
    let pattern = "";
    try {
      if (prefix) {
        pattern += "(" + escapeRegex(prefix) + ")\\s*";
      }
      pattern += "(" + escapeRegex(searchText) + ")";
      if (suffix) {
        pattern += "\\s*(" + escapeRegex(suffix) + ")";
      }

      // Use 's' flag to make . match newlines
      const regex = new RegExp(pattern, "gis");
      let match;

      while ((match = regex.exec(fullText)) !== null) {
        // Determine which group contains the actual search text
        let searchGroupIndex = prefix ? 2 : 1;
        let matchStart = match.index;
        let matchEnd = matchStart + match[0].length;

        // If we have prefix/suffix, we only want to highlight the actual search text
        if (prefix || suffix) {
          let groupStart = 0;
          for (let i = 1; i < searchGroupIndex; i++) {
            groupStart += match[i] ? match[i].length : 0;
          }
          matchStart = match.index + groupStart;
          matchEnd = matchStart + match[searchGroupIndex].length;

          // Account for any spaces between prefix and search text
          const beforeSearchText = fullText.substring(match.index, matchStart);
          const spaceMatch = beforeSearchText.match(/\s*$/);
          if (spaceMatch) {
            matchStart = match.index + beforeSearchText.length - spaceMatch[0].length + spaceMatch[0].length;
          }
        }

        // Find which nodes this match spans
        const affectedNodes = [];

        nodeMap.forEach((nodeInfo, index) => {
          if (nodeInfo.end > matchStart && nodeInfo.start < matchEnd) {
            const nodeStart = Math.max(0, matchStart - nodeInfo.start);
            const nodeEnd = Math.min(nodeInfo.text.length, matchEnd - nodeInfo.start);

            affectedNodes.push({
              node: nodeInfo.node,
              start: nodeStart,
              end: nodeEnd,
              text: nodeInfo.text.substring(nodeStart, nodeEnd),
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
      console.error("Error in regex matching:", e);
      console.error("Pattern:", pattern);
    }

    return matches;
  }

  // Highlight the matched text
  highlight(searchText, prefix = "", suffix = "", onClick) {
    this.clearHighlights();

    if (!searchText) return 0;

    const matches = this.findMatches(searchText, prefix, suffix);

    matches.forEach((match, matchIndex) => {
      const highlightGroup = [];

      // Create handlers for the entire group
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

        // Add clicked class to all spans in the group
        highlightGroup.forEach((span) => {
          span.classList.add("clicked");
          setTimeout(() => {
            span.classList.remove("clicked");
          }, 500);
        });

        if (onClick) {
          onClick(e, {
            matchIndex,
            text: searchText,
            prefix,
            suffix,
            element: e.target,
            highlights: highlightGroup,
            fullMatch: match.fullMatch,
            searchMatch: match.searchMatch,
          });
        }
      };

      match.nodes.forEach((nodeInfo, nodeIndex) => {
        const { node, start, end } = nodeInfo;
        const parent = node.parentNode;

        // Skip if the node has already been processed or removed from DOM
        if (!parent) {
          console.warn("Skipping node - no parent found");
          return;
        }

        // Split the text node if necessary
        const beforeText = node.nodeValue.substring(0, start);
        const highlightText = node.nodeValue.substring(start, end);
        const afterText = node.nodeValue.substring(end);

        const highlightSpan = document.createElement("span");
        highlightSpan.className = "highlight";
        highlightSpan.textContent = highlightText;
        highlightSpan.dataset.matchIndex = matchIndex;
        highlightSpan.dataset.nodeIndex = nodeIndex;

        // Add position-specific classes for visual continuity
        if (match.nodes.length === 1) {
          highlightSpan.classList.add("highlight-single");
        } else if (nodeIndex === 0) {
          highlightSpan.classList.add("highlight-start");
        } else if (nodeIndex === match.nodes.length - 1) {
          highlightSpan.classList.add("highlight-end");
        } else {
          highlightSpan.classList.add("highlight-middle");
        }

        // Replace the text node with the new structure
        const fragment = document.createDocumentFragment();

        if (beforeText) {
          fragment.appendChild(document.createTextNode(beforeText));
        }
        fragment.appendChild(highlightSpan);
        if (afterText) {
          fragment.appendChild(document.createTextNode(afterText));
        }

        try {
          parent.replaceChild(fragment, node);
          highlightGroup.push(highlightSpan);
        } catch (e) {
          console.error("Error replacing node:", e);
        }
      });

      // Add event listeners to all spans in the group
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
    this.highlights.forEach((highlightGroup) => {
      highlightGroup.forEach((span) => {
        const parent = span.parentNode;
        if (!parent) {
          return;
        }

        const text = span.textContent;
        const textNode = document.createTextNode(text);

        try {
          parent.replaceChild(textNode, span);
        } catch (e) {
          console.error("Error clearing highlight:", e);
        }
      });
    });

    try {
      this.container.normalize();
    } catch (e) {
      console.error("Error normalizing container:", e);
    }

    this.highlights = [];
  }
}

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

  findMatches(selection, prefix, suffix) {
    if (!selection) {
      return [];
    }

    const textNodes = this.getTextNodes(this.container);
    const matches = [];

    let currentPos = 0;
    const nodeMap = textNodes.map((node) => {
      const start = currentPos;
      const end = currentPos + node.nodeValue.length;
      currentPos = end;
      return { node, start, end, text: node.nodeValue };
    });

    const fullText = nodeMap.map((item) => item.text).join("");
    const textToFind = prefix + selection + suffix;

    const matchIdx = fullText.indexOf(textToFind);

    if (matchIdx !== -1) {
      let matchStart = matchIdx + prefix.length;
      const matchEnd = matchStart + selection.length;

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
        });
      }
    }

    return matches;
  }

  highlight(searchText, prefix, suffix, onClick) {
    this.clearHighlights();

    const matches = this.findMatches(searchText, prefix, suffix);

    matches.forEach((match, matchIndex) => {
      const highlightGroup = [];

      const handleMouseEnter = () => {
        if (!onClick) {
          return;
        }

        highlightGroup.forEach((span) => {
          span.classList.add("znai-highlight-hover");
        });
      };

      const handleMouseLeave = () => {
        if (!onClick) {
          return;
        }

        highlightGroup.forEach((span) => {
          span.classList.remove("znai-highlight-hover");
        });
      };

      const handleClick = (e) => {
        e.stopPropagation();

        if (onClick) {
          onClick();
        }
      };

      const handleDoubleClick = (e) => {
        if (onClick) {
          e.stopPropagation();
        }
      };

      match.nodes.forEach((nodeInfo, nodeIndex) => {
        const parent = nodeInfo.node.parentNode;
        if (!parent) return;

        const beforeText = nodeInfo.node.nodeValue.substring(0, nodeInfo.start);
        const highlightText = nodeInfo.node.nodeValue.substring(nodeInfo.start, nodeInfo.end);
        const afterText = nodeInfo.node.nodeValue.substring(nodeInfo.end);

        const highlightSpan = document.createElement("span");
        highlightSpan.className = "znai-highlight";
        highlightSpan.textContent = highlightText;

        if (match.nodes.length === 1) {
          highlightSpan.classList.add("single");
        } else if (nodeIndex === 0) {
          highlightSpan.classList.add("start");
        } else if (nodeIndex === match.nodes.length - 1) {
          highlightSpan.classList.add("end");
        } else {
          highlightSpan.classList.add("middle");
        }

        const fragment = document.createDocumentFragment();
        if (beforeText) {
          fragment.appendChild(document.createTextNode(beforeText));
        }
        fragment.appendChild(highlightSpan);
        if (afterText) {
          fragment.appendChild(document.createTextNode(afterText));
        }

        parent.replaceChild(fragment, nodeInfo.node);
        highlightGroup.push(highlightSpan);
      });

      highlightGroup.forEach((span) => {
        span.addEventListener("mouseenter", handleMouseEnter);
        span.addEventListener("mouseleave", handleMouseLeave);
        span.addEventListener("click", handleClick);
        span.addEventListener("dblclick", handleDoubleClick);
      });

      this.highlights.push(highlightGroup);
    });

    return this.highlights.flat();
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

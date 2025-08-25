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

import { createSelectionExpander } from "./selectionUtils.js";

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

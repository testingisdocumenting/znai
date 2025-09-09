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

import { JSDOM } from "jsdom";

export function setupDOM(htmlContent) {
  const dom = new JSDOM(
    `
      <!DOCTYPE html>
      <html lang="en">
        <body>${htmlContent}</body>
      </html>
    `,
    { pretendToBeVisual: true }
  );

  const document = dom.window.document;
  const window = dom.window;

  global.window = window;
  global.document = document;
  global.Node = window.Node;
  global.NodeFilter = window.NodeFilter;

  const container = document.body;

  return { document, window, container };
}

export function selectText(startNode, startOffset, endNode, endOffset) {
  const range = global.document.createRange();
  range.setStart(startNode, startOffset);
  range.setEnd(endNode, endOffset);

  const selection = global.window.getSelection();
  selection.removeAllRanges();
  selection.addRange(range);
}

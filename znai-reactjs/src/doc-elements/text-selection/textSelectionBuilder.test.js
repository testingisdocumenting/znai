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

import { describe, it, expect } from "vitest";
import { JSDOM } from "jsdom";
import { findPrefixSuffixAndMatch } from "./textSelectionBuilder.js";

// Helper function to match TextHighlighter's text building
function buildFullTextLikeHighlighter(container) {
  const textNodes = [];
  const walker = container.ownerDocument.createTreeWalker(container, global.NodeFilter.SHOW_TEXT, null, false);
  let textNode;
  while ((textNode = walker.nextNode())) {
    textNodes.push(textNode);
  }
  return textNodes.map((node) => node.nodeValue).join("");
}

describe("textSelectionBuilder", () => {
  function setupDOM(htmlContent) {
    const dom = new JSDOM(
      `
      <!DOCTYPE html>
      <html lang="en">
        <body>
          ${htmlContent}
        </body>
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

  function selectText(startNode, startOffset, endNode, endOffset) {
    const range = global.document.createRange();
    range.setStart(startNode, startOffset);
    range.setEnd(endNode, endOffset);

    const selection = global.window.getSelection();
    selection.removeAllRanges();
    selection.addRange(range);
  }

  describe("findPrefixSuffixAndMatch", () => {
    it("should handle unique text with minimal context when multiple entries are present", () => {
      const { container } = setupDOM(`
        <div>
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p id="content">The sample text is useful for testing.</p>
        </div>
      `);

      const lastP = document.getElementById("content");
      const textNode = lastP.firstChild;

      selectText(textNode, 4, textNode, 10);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("sample");
      expect(result.prefix).toBe("      The ");
      expect(result.suffix).toBe(" text is u");
    });

    it("should handle selection across multiple nodes", () => {
      const { container } = setupDOM(`
        <div>
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const paragraphs = container.querySelectorAll("p");
      const firstTextNode = paragraphs[0].firstChild;
      const secondTextNode = paragraphs[1].firstChild;

      selectText(firstTextNode, 37, secondTextNode, 4);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("ords.\n          This");
      expect(result.prefix).toBe("multiple w");
      expect(result.suffix).toBe(" text cont");
    });

    it("should handle selection at the beginning of container", () => {
      const { container } = setupDOM(`
        <div>
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      selectText(textNode, 0, textNode, 7);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("This is");
      // TODO handle boundaries of the container properly
      expect(result.prefix).toBe("          ");
      expect(result.suffix).toBe(" a sample ");
    });

    it("should handle selection at the end of container", () => {
      const { container } = setupDOM(`
        <div>
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const lastP = container.querySelectorAll("p")[2];
      const textNode = lastP.firstChild;
      const text = textNode.textContent;

      selectText(textNode, text.length - 8, textNode, text.length);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("testing.");
      expect(result.prefix).toBe("seful for ");
      expect(result.suffix).toBe("");
    });

    it("should handle selection across code snippet span elements", () => {
      const { container } = setupDOM(`
        <div>
          <pre><code>
            <span class="keyword">function</span> <span class="function-name">calculateSum</span>(<span class="parameter">a</span>, <span class="parameter">b</span>) {
              <span class="keyword">return</span> <span class="variable">a</span> + <span class="variable">b</span>;
            }
            <span class="keyword">const</span> <span class="variable">result</span> = <span class="function-name">calculateSum</span>(<span class="number">5</span>, <span class="number">3</span>);
          </code></pre>
        </div>
      `);

      const functionSpan = container.querySelector(".keyword");
      const parameterSpan = container.querySelector(".parameter");

      selectText(functionSpan.firstChild, 0, parameterSpan.firstChild, 1);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("function calculateSum(a");
      expect(result.prefix).toBe("          ");
      expect(result.suffix).toBe(", b) {\n   ");
    });
  });
});

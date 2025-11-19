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
import { findPrefixSuffixAndMatch } from "./textSelectionBuilder.js";
import { selectText, setupDOM } from "./selectionTestUtils.js";

describe("textSelectionBuilder", () => {
  describe("findPrefixSuffixAndMatch", () => {
    describe("basic text node selections", () => {
      it("should handle unique text with minimal context when multiple entries are present", () => {
        const { container } = setupDOM(`
          <div>
            <p>This is a sample text with multiple words.</p>
            <p>This text contains repeated phrases.</p>
            <p id="content">The sample text is useful for testing.</p>
          </div>
        `);

        const textNode = document.getElementById("content").firstChild;
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
        selectText(paragraphs[0].firstChild, 37, paragraphs[1].firstChild, 4);

        const result = findPrefixSuffixAndMatch(container);

        expect(result.selection).toBe("ords.\n            This");
        expect(result.prefix).toBe("multiple w");
        expect(result.suffix).toBe(" text cont");
      });

      it("should handle selection at the beginning of container", () => {
        const { container } = setupDOM(`
          <div id="boundary"><p>This is a sample text with multiple words.</p>
            <p>This text contains repeated phrases.</p>
            <p>The sample text is useful for testing.</p></div>
        `);

        const boundaryContainer = document.getElementById("boundary");
        const textNode = container.querySelector("p").firstChild;
        selectText(textNode, 0, textNode, 7);

        const result = findPrefixSuffixAndMatch(boundaryContainer);

        expect(result.selection).toBe("This is");
        expect(result.prefix).toBe("");
        expect(result.suffix).toBe(" a sample ");
      });

      it("should handle selection at the end of container", () => {
        const { container } = setupDOM(`
          <div id="boundary">
            <p>This is a sample text with multiple words.</p>
            <p>This text contains repeated phrases.</p>
            <p>The sample text is useful for testing.</p></div>
        `);

        const boundaryContainer = document.getElementById("boundary");
        const textNode = container.querySelectorAll("p")[2].firstChild;
        const text = textNode.textContent;
        selectText(textNode, text.length - 8, textNode, text.length);

        const result = findPrefixSuffixAndMatch(boundaryContainer);

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

    describe("triple-click selections with element node boundaries", () => {
      it("should handle triple-click on middle paragraph", () => {
        const { container } = setupDOM(`
          <div>
            <p>First paragraph with some text.</p>
            <p id="target">Second paragraph to triple click.</p>
            <p>Third paragraph with more text.</p>
          </div>
        `);

        const targetP = document.getElementById("target");
        selectText(targetP, 0, targetP, targetP.childNodes.length);

        const result = findPrefixSuffixAndMatch(container);

        expect(result.selection).toBe("Second paragraph to triple click.");
        expect(result.prefix.length).toBeGreaterThan(0);
        expect(result.suffix.length).toBeGreaterThan(0);
      });

      const bulletTestCases = [
        { position: "first", id: "first-li", text: "First bullet point item.", expectPrefix: false },
        { position: "second", id: "second-li", text: "Second bullet point item.", expectPrefix: true },
        { position: "last", id: "last-li", text: "Third bullet point item.", expectPrefix: true },
      ];

      bulletTestCases.forEach(({ position, id, text, expectPrefix }) => {
        it(`should handle triple-click on ${position} bullet point`, () => {
          const { container } = setupDOM(`
            <div>
              <ul id="list">
                <li id="first-li">First bullet point item.</li>
                <li id="second-li">Second bullet point item.</li>
                <li id="last-li">Third bullet point item.</li>
              </ul>
            </div>
          `);

          const ul = document.getElementById("list");
          const targetLi = document.getElementById(id);
          const childIndex = Array.from(ul.childNodes).indexOf(targetLi);
          selectText(ul, childIndex, ul, childIndex + 1);

          const result = findPrefixSuffixAndMatch(container);

          expect(result.selection).toBe(text);
          if (expectPrefix) {
            expect(result.prefix.length).toBeGreaterThan(0);
          }
          expect(result.suffix.length).toBeGreaterThan(0);
        });
      });

      it("should not include following paragraphs when selecting a bullet point", () => {
        const { container } = setupDOM(`
          <div>
            <ul>
              <li>First bullet point item.</li>
              <li id="target-li">Second bullet point item.</li>
              <li>Third bullet point item.</li>
            </ul>
            <p>Following paragraph that should not be included.</p>
          </div>
        `);

        const ul = container.querySelector("ul");
        const targetLi = document.getElementById("target-li");
        const childIndex = Array.from(ul.childNodes).indexOf(targetLi);
        selectText(ul, childIndex, ul, childIndex + 1);

        const result = findPrefixSuffixAndMatch(container);

        expect(result.selection).toBe("Second bullet point item.");
        expect(result.selection).not.toContain("Following paragraph");
      });
    });

    describe("edge cases with complex boundary scenarios", () => {
      it("should handle end boundary outside the list", () => {
        const { container } = setupDOM(`
          <div id="parent">
            <ul id="list">
              <li>First bullet point item.</li>
              <li>Second bullet point item.</li>
              <li id="last-li">Third bullet point item.</li>
            </ul>
          </div>
        `);

        const parent = document.getElementById("parent");
        const ul = document.getElementById("list");
        const lastLi = document.getElementById("last-li");
        const ulChildIndex = Array.from(parent.childNodes).indexOf(ul);
        selectText(lastLi, 0, parent, ulChildIndex + 1);

        const result = findPrefixSuffixAndMatch(container);

        expect(result.selection.trim()).toBe("Third bullet point item.");
        expect(result.prefix.length).toBeGreaterThan(0);
      });

      it("should handle end boundary at offset 0 of next element", () => {
        const { container } = setupDOM(`
          <div>
            <ul id="list">
              <li id="last-li">Last bullet point item.</li>
            </ul>
            <div id="next-block" class="paragraph content-block">Next paragraph.</div>
          </div>
        `);

        const lastLi = document.getElementById("last-li");
        const nextBlock = document.getElementById("next-block");
        selectText(lastLi.firstChild, 0, nextBlock, 0);

        const result = findPrefixSuffixAndMatch(container);

        expect(result.selection.trim()).toBe("Last bullet point item.");
        expect(result.suffix.length).toBeGreaterThan(0);
      });

      it("should handle triple-click on paragraph where end is next paragraph at offset 0", () => {
        const { container } = setupDOM(`
          <div>
            <div class="paragraph content-block" id="first-para">First paragraph of text.</div>
            <div class="paragraph content-block" id="second-para">Second paragraph of text.</div>
          </div>
        `);

        const firstPara = document.getElementById("first-para");
        const secondPara = document.getElementById("second-para");
        selectText(firstPara.firstChild, 0, secondPara, 0);

        const result = findPrefixSuffixAndMatch(container);

        expect(result.selection.trim()).toBe("First paragraph of text.");
        expect(result.suffix.length).toBeGreaterThan(0);
      });
    });
  });
});

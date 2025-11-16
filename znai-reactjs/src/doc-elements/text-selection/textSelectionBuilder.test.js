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
        <div id="boundary"><p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p></div>
      `);

      const boundaryContainer = document.getElementById("boundary");
      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

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
      const lastP = container.querySelectorAll("p")[2];
      const textNode = lastP.firstChild;
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

    it("should handle triple-click selection with element node boundaries", () => {
      const { container } = setupDOM(`
        <div>
          <p>First paragraph with some text.</p>
          <p id="target">Second paragraph to triple click.</p>
          <p>Third paragraph with more text.</p>
        </div>
      `);

      const targetP = document.getElementById("target");

      // Triple-click typically selects with element node as container
      // startContainer = <p> element, startOffset = 0 (before first child)
      // endContainer = <p> element, endOffset = childNodes.length (after last child)
      selectText(targetP, 0, targetP, targetP.childNodes.length);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("Second paragraph to triple click.");
      expect(result.prefix.length).toBeGreaterThan(0);
      expect(result.suffix.length).toBeGreaterThan(0);
    });

    it("should handle triple-click with parent container selecting single child", () => {
      const { container } = setupDOM(`
        <div id="parent">
          <ul>
            <li>First bullet point item.</li>
            <li id="target-li">Second bullet point item.</li>
            <li>Third bullet point item.</li>
          </ul>
          <p>Following paragraph that should not be included.</p>
        </div>
      `);

      const parent = document.getElementById("parent");
      const ul = parent.querySelector("ul");
      const targetLi = document.getElementById("target-li");

      // Find the actual child index of the target <li> in the <ul>
      // (accounting for whitespace text nodes between elements)
      const childIndex = Array.from(ul.childNodes).indexOf(targetLi);

      // Triple-click on second <li> might create selection with <ul> as container
      // startContainer = <ul>, startOffset = childIndex (before target child)
      // endContainer = <ul>, endOffset = childIndex + 1 (after target child)
      selectText(ul, childIndex, ul, childIndex + 1);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("Second bullet point item.");
      // Should NOT include "Following paragraph"
      expect(result.selection).not.toContain("Following paragraph");
    });

    it("should handle triple-click on the last bullet point", () => {
      const { container } = setupDOM(`
        <div id="parent">
          <ul>
            <li>First bullet point item.</li>
            <li>Second bullet point item.</li>
            <li id="last-li">Third bullet point item.</li>
          </ul>
        </div>
      `);

      const parent = document.getElementById("parent");
      const ul = parent.querySelector("ul");
      const lastLi = document.getElementById("last-li");

      // Find the actual child index of the last <li> in the <ul>
      const childIndex = Array.from(ul.childNodes).indexOf(lastLi);

      // Triple-click on last <li> creates selection with <ul> as container
      selectText(ul, childIndex, ul, childIndex + 1);

      const result = findPrefixSuffixAndMatch(container);

      expect(result.selection).toBe("Third bullet point item.");
      expect(result.prefix.length).toBeGreaterThan(0);
      // Suffix may include trailing whitespace, which is fine
    });

    it("should handle triple-click where end boundary is outside the list", () => {
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

      // Some browsers might create selection where:
      // startContainer = <li>, startOffset = 0
      // endContainer = parent div, endOffset = 2 (after <ul>)
      const ulChildIndex = Array.from(parent.childNodes).indexOf(ul);

      selectText(lastLi, 0, parent, ulChildIndex + 1);

      const result = findPrefixSuffixAndMatch(container);

      // Selection may include trailing whitespace from the DOM
      expect(result.selection.trim()).toBe("Third bullet point item.");
      expect(result.prefix.length).toBeGreaterThan(0);
    });
  });
});

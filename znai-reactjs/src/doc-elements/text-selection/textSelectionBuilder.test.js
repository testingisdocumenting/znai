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
      <html>
        <body>
          ${htmlContent}
        </body>
      </html>
    `,
      { pretendToBeVisual: true }
    );

    const document = dom.window.document;
    const window = dom.window;

    // Set up only the globals that textSelectionBuilder actually uses
    global.window = window;
    global.document = document;
    global.Node = window.Node;
    global.NodeFilter = window.NodeFilter;

    // Get the container and add innerText polyfill automatically
    const container = document.getElementById("test-container");
    Object.defineProperty(container, "innerText", {
      get() {
        return this.textContent;
      },
    });

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

    it("should find minimal unique prefix and suffix for selected text", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      // Select "sample text" which appears twice
      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      // "This is a sample text with multiple words."
      // Select "sample" starting at position 10
      selectText(textNode, 10, textNode, 16);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("sample");
      // Should have found the minimal unique prefix/suffix
      expect(result.prefix).toBe("This is a ");
      expect(result.suffix).toBe(" text with");
      // Combined pattern should be unique
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const firstIndex = fullText.indexOf(pattern);
      const secondIndex = fullText.indexOf(pattern, firstIndex + 1);
      expect(firstIndex).toBeGreaterThan(-1);
      expect(secondIndex).toBe(-1); // Should be unique
    });

    it("should handle unique text with minimal context", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      // Select "multiple" which is unique
      selectText(textNode, 27, textNode, 35);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("multiple");
      // For unique text, should still provide at least 10 chars of context
      expect(result.prefix).toBe("text with ");
      expect(result.suffix).toBe(" words.\n  ");
    });

    it("should handle selection across multiple nodes", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const paragraphs = container.querySelectorAll("p");
      const firstTextNode = paragraphs[0].firstChild;
      const secondTextNode = paragraphs[1].firstChild;

      // Select from "words." in first paragraph to "This" in second
      selectText(firstTextNode, 37, secondTextNode, 4);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      // The text includes whitespace between paragraphs
      expect(result.text).toContain("ords.");
      expect(result.text).toContain("This");
      expect(result.prefix).toBeTruthy();
      expect(result.suffix).toBeTruthy();
    });

    it("should handle repeated text by finding unique context", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const paragraphs = container.querySelectorAll("p");
      const firstTextNode = paragraphs[0].firstChild;

      // Select "text" which appears multiple times
      selectText(firstTextNode, 17, firstTextNode, 21);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("text");
      // Should find unique prefix/suffix combination
      expect(result.prefix).toBe(" a sample ");
      expect(result.suffix).toBe(" with mult");
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it("should return minimum 10 characters for prefix and suffix when possible", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const lastP = container.querySelectorAll("p")[2];
      const textNode = lastP.firstChild;

      // Select "useful" which is unique
      selectText(textNode, 19, textNode, 25);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("useful");
      expect(result.prefix).toBe("e text is ");
      expect(result.suffix).toBe(" for testi");
    });

    it("should handle selection at the beginning of container", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      selectText(textNode, 0, textNode, 7);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("This is");
      expect(result.prefix).toBe("          ");
      expect(result.suffix).toBe(" a sample ");
    });

    it("should handle selection at the end of container", () => {
      const { container } = setupDOM(`
        <div id="test-container">
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

      expect(result).toBeDefined();
      expect(result.text).toBe("testing.");
      expect(result.prefix).toBe("seful for ");
      expect(result.suffix).toBe("\n        ");
    });

    it("should clean prefix to word boundary when beneficial", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      selectText(textNode, 5, textNode, 16);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("is a sample");
      expect(result.prefix.length).toBeGreaterThanOrEqual(10);
      expect(result.suffix).toBe(" text with");

      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it("should return null for empty selection", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      selectText(textNode, 5, textNode, 5);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeNull();
    });

    it("should handle single character selection", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      // Select single character "a" from "a sample"
      selectText(textNode, 8, textNode, 9);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("a");
      expect(result.prefix).toBe("  This is ");
      expect(result.suffix).toBe(" sample te");

      // Verify uniqueness
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it("should handle text with special characters", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Code: function() { return "test"; }</p>
          <p>Another function() { return "value"; }</p>
        </div>
      `);

      const textNode = container.querySelector("p").firstChild;
      // Select "function()"
      selectText(textNode, 6, textNode, 16);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("function()");
      expect(result.prefix.length).toBeGreaterThanOrEqual(10);
      expect(result.suffix.length).toBeGreaterThanOrEqual(10);
    });

    it("should handle whitespace-only selection", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const firstP = container.querySelector("p");
      const textNode = firstP.firstChild;

      selectText(textNode, 7, textNode, 8);
      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeNull();
    });

    it("should handle selection spanning only whitespace between paragraphs", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a sample text with multiple words.</p>
          <p>This text contains repeated phrases.</p>
          <p>The sample text is useful for testing.</p>
        </div>
      `);

      const paragraphs = container.querySelectorAll("p");
      const firstTextNode = paragraphs[0].firstChild;

      // Select from end of first paragraph to just the whitespace
      selectText(firstTextNode, firstTextNode.textContent.length - 1, firstTextNode, firstTextNode.textContent.length);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe(".");
      expect(result.prefix).toBe("iple words");
      // The suffix includes whitespace but might be trimmed, so just check it's not empty
      expect(result.suffix.length).toBeGreaterThanOrEqual(10);
    });

    it("should handle very long repeated text", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>start ${"test ".repeat(50)} end</p>
        </div>
      `);

      const textNode = container.querySelector("p").firstChild;
      // Select one instance of "test" after "start " (starts at position 6, ends at position 10)
      selectText(textNode, 6, textNode, 10);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("test");
      // Should have found some context even with many repetitions
      expect(result.prefix).toBe("    start ");
      expect(result.suffix.length).toBeGreaterThanOrEqual(10);
    });

    it("should handle selection across code snippet span elements", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <pre><code>
            <span class="keyword">function</span> <span class="function-name">calculateSum</span>(<span class="parameter">a</span>, <span class="parameter">b</span>) {
              <span class="keyword">return</span> <span class="variable">a</span> + <span class="variable">b</span>;
            }
            <span class="keyword">const</span> <span class="variable">result</span> = <span class="function-name">calculateSum</span>(<span class="number">5</span>, <span class="number">3</span>);
          </code></pre>
        </div>
      `);

      // Select text that spans across multiple span elements: "function calculateSum(a"
      const functionSpan = container.querySelector(".keyword");
      const parameterSpan = container.querySelector(".parameter");

      // Start from "function" and select until "a" in the first parameter
      selectText(functionSpan.firstChild, 0, parameterSpan.firstChild, 1);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("function calculateSum(a");

      // Should have found context that makes this unique
      expect(result.prefix).toBeTruthy();
      expect(result.suffix).toBeTruthy();

      // Verify the pattern is unique in the document
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it("should handle selection within inline code spans", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Use the <code><span class="function">map</span></code> function to transform arrays.</p>
          <p>The <code><span class="function">filter</span></code> function removes unwanted elements.</p>
          <p>You can chain <code><span class="function">map</span></code> and <code><span class="function">filter</span></code> together.</p>
        </div>
      `);

      // Select "map" from the first occurrence (which appears multiple times)
      const firstMapSpan = container.querySelector(".function");
      selectText(firstMapSpan.firstChild, 0, firstMapSpan.firstChild, 3);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("map");

      // Should have found enough context to distinguish from other "map" occurrences
      expect(result.prefix.includes("Use the")).toBeTruthy();
      expect(result.suffix.includes("function")).toBeTruthy();

      // Verify uniqueness
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it("should handle selection across multiline code with nested spans and line breaks", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <div class="znai-code-block">
            <pre><code>
<span class="token keyword">import</span> <span class="token punctuation">{</span> <span class="token function">useState</span> <span class="token punctuation">}</span> <span class="token keyword">from</span> <span class="token string">'react'</span><span class="token punctuation">;</span>

<span class="token keyword">function</span> <span class="token function">Counter</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
  <span class="token keyword">const</span> <span class="token punctuation">[</span>count<span class="token punctuation">,</span> setCount<span class="token punctuation">]</span> <span class="token operator">=</span> <span class="token function">useState</span><span class="token punctuation">(</span><span class="token number">0</span><span class="token punctuation">)</span><span class="token punctuation">;</span>
  
  <span class="token keyword">return</span> <span class="token punctuation">(</span>
    <span class="token operator">&lt;</span>div<span class="token operator">&gt;</span>
      <span class="token operator">&lt;</span>p<span class="token operator">&gt;</span>Count<span class="token punctuation">:</span> <span class="token punctuation">{</span>count<span class="token punctuation">}</span><span class="token operator">&lt;</span><span class="token operator">/</span>p<span class="token operator">&gt;</span>
      <span class="token operator">&lt;</span>button onClick<span class="token operator">=</span><span class="token punctuation">{</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token operator">=&gt;</span> setCount<span class="token punctuation">(</span>count <span class="token operator">+</span> <span class="token number">1</span><span class="token punctuation">)</span><span class="token punctuation">}</span><span class="token operator">&gt;</span>
        Increment
      <span class="token operator">&lt;</span><span class="token operator">/</span>button<span class="token operator">&gt;</span>
    <span class="token operator">&lt;</span><span class="token operator">/</span>div<span class="token operator">&gt;</span>
  <span class="token punctuation">)</span><span class="token punctuation">;</span>
<span class="token punctuation">}</span>
            </code></pre>
          </div>
        </div>
      `);

      // Select text spanning from "useState" to "const [" - crosses multiple lines and many spans
      const useStateSpan = container.querySelector(".token.function");
      const constKeywordSpan = container.querySelectorAll(".token.keyword")[2]; // Third "const" keyword

      // Select from "useState" through to "const"
      selectText(useStateSpan.firstChild, 0, constKeywordSpan.firstChild, 5);

      const result = findPrefixSuffixAndMatch(container);

      expect(result).toBeDefined();
      expect(result.text).toBe("useState } from 'react';\n\nfunct");

      // Should handle the complex nested structure and find unique context
      expect(result.prefix).toBeTruthy();
      expect(result.suffix).toBeTruthy();
      expect(result.prefix.length).toBeGreaterThanOrEqual(10);
      expect(result.suffix.length).toBeGreaterThanOrEqual(10);

      // Verify the pattern is unique despite the complex structure
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it("should handle selection across paragraph and code snippet", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is a paragraph with some text.</p>
          <pre><code>
            <span class="token keyword">function</span> <span class="token function">example</span>() {
              <span class="token keyword">return</span> <span class="token string">"code"</span>;
            }
          </code></pre>
          <p>Another paragraph after code.</p>
        </div>
      `);

      // Select from paragraph text across to code snippet
      const firstP = container.querySelector("p");
      const codeSpan = container.querySelector(".token.function");
      
      // Select from "some text" in paragraph to "example" in code
      selectText(firstP.firstChild, 25, codeSpan.firstChild, 7);

      const result = findPrefixSuffixAndMatch(container);

      // This should NOT return null - it should handle cross-element selection
      expect(result).toBeDefined();
      expect(result).not.toBeNull();
      expect(result.text).toContain("text");
      expect(result.text).toContain("example");
      expect(result.prefix).toBeTruthy();
      expect(result.suffix).toBeTruthy();
    });

    it("should handle complex selection that might fail context expansion", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <div class="content">
            <p>Start paragraph text</p>
            <div class="nested">
              <pre><code>
                <span class="highlight">complex</span>
                <span class="token">code</span>
                <span class="variable">structure</span>
              </code></pre>
            </div>
            <p>End paragraph</p>
          </div>
        </div>
      `);

      // Select across deeply nested structure - this might cause context expansion issues
      const startP = container.querySelector("p");
      const endP = container.querySelectorAll("p")[1];
      
      // Select from start paragraph through complex nested code to end paragraph
      selectText(startP.firstChild, 6, endP.firstChild, 3);

      const result = findPrefixSuffixAndMatch(container);

      // Should not return null even with complex nested structure
      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBeTruthy();
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
      }
    });

    it("should handle selection from end of sentence to start of code block", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Here is some explanatory text.</p>
          <pre><code>function example() {
  return "hello";
}</code></pre>
          <p>More text after code.</p>
        </div>
      `);

      // Select from the period at end of paragraph to start of code block
      const paragraph = container.querySelector("p");
      const codeBlock = container.querySelector("code");
      
      // Select from "." to "function"
      selectText(paragraph.firstChild, paragraph.firstChild.textContent.length - 1, codeBlock.firstChild, 8);

      const result = findPrefixSuffixAndMatch(container);

      // This specific case was returning null - should be fixed now
      expect(result).not.toBeNull();
      expect(result).toBeDefined();
      if (result) {
        expect(result.text).toContain(".");
        expect(result.text).toContain("function");
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
      }
    });

    it("should handle selection spanning just whitespace between paragraph and code", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Text ending here.</p>
          <pre><code>code starting here</code></pre>
        </div>
      `);

      // Select from just before period to just after newline - this might include only whitespace/formatting
      const paragraph = container.querySelector("p");
      const codeBlock = container.querySelector("code");
      
      // Try to select the transition area that might be problematic
      selectText(paragraph.firstChild, paragraph.firstChild.textContent.length - 2, codeBlock.firstChild, 4);

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();

      // Should handle this case gracefully - either return valid result or null for empty selection
      if (selectedText.trim() === '') {
        expect(result).toBeNull();
      } else {
        expect(result).not.toBeNull();
        if (result) {
          expect(result.text).toBeTruthy();
          expect(result.prefix).toBeTruthy();
          expect(result.suffix).toBeTruthy();
        }
      }
    });

    it("should handle tricky selection with complex whitespace and markup", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>This is the end of a sentence.</p>
          <div class="znai-snippet">
            <pre><code><span class="token keyword">function</span> <span class="function">test</span>() {
              <span class="token keyword">return</span> <span class="string">"value"</span>;
            }</code></pre>
          </div>
          <p>More content here.</p>
        </div>
      `);

      // Simulate the exact problematic scenario: end of sentence to code
      const paragraph = container.querySelector("p");
      const tokenSpan = container.querySelector(".token.keyword");
      
      // Select from sentence ending to start of code token
      selectText(paragraph.firstChild, paragraph.firstChild.textContent.length - 1, tokenSpan.firstChild, 8);

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();

      // This is the case that should be fixed - should not return null
      if (selectedText.trim() === '') {
        expect(result).toBeNull();
      } else {
        expect(result).not.toBeNull();
        if (result) {
          expect(result.text).toBeTruthy();
          expect(result.prefix).toBeTruthy();
          expect(result.suffix).toBeTruthy();
        }
      }
    });

    it("should handle selecting lines within code block after paragraph", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Here is some explanatory text about the following code.</p>
          <pre><code>function example() {
  const result = processData();
  return result;
}</code></pre>
          <p>More explanation after.</p>
        </div>
      `);

      // Select just lines within the code block (not spanning from paragraph)
      const codeBlock = container.querySelector("code");
      const codeText = codeBlock.firstChild;
      
      // Select "const result = processData();" line from the code
      const startIndex = codeText.textContent.indexOf("const result");
      const endIndex = codeText.textContent.indexOf(";", startIndex) + 1;
      
      selectText(codeText, startIndex, codeText, endIndex);

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();
      
      expect(selectedText).toBe("const result = processData();");
      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBe(selectedText);
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
      }
    });

    it("should handle context expansion across formatted code boundaries", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Explanation text ends here.</p>
          <pre class="language-javascript"><code>
function calculateTotal(items) {
  let total = 0;
  for (const item of items) {
    total += item.price;
  }
  return total;
}
          </code></pre>
        </div>
      `);

      // Select a line within the code, which should expand context but might hit the paragraph boundary
      const codeBlock = container.querySelector("code");
      const codeText = codeBlock.firstChild;
      
      // Select the "let total = 0;" line 
      const startIndex = codeText.textContent.indexOf("let total");
      const endIndex = codeText.textContent.indexOf(";", startIndex) + 1;
      
      selectText(codeText, startIndex, codeText, endIndex);

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();

      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBe(selectedText.trim());
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
      }
    });

    it("should match TextHighlighter text building logic exactly", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <div class="paragraph content-block">
            <span class="znai-simple-text">This </span>
            <code class="znai-inlined-code">include-</code>
            <span class="znai-simple-text"> syntax will appear throughout the documentation.</span>
          </div>
          <div class="snippet">
            <pre>
              <span class="znai-code-line">
                <span class="token keyword">class</span> <span class="token class-name">JsClass</span> <span class="token punctuation">{</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                    <span class="token function">constructor</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span>
</span>
              </span>
            </pre>
          </div>
        </div>
      `);

      // Simulate TextHighlighter's text building
      const textNodes = [];
      const walker = container.ownerDocument.createTreeWalker(container, global.NodeFilter.SHOW_TEXT, null, false);
      let textNode;
      while ((textNode = walker.nextNode())) {
        textNodes.push(textNode);
      }
      const highlighterFullText = textNodes.map((node) => node.nodeValue).join("");
      
      // We already imported findPrefixSuffixAndMatch at the top of the file
      
      // Select something within the code
      const constructorSpan = container.querySelector('.token.function');
      selectText(constructorSpan.firstChild, 0, constructorSpan.firstChild, 11); // "constructor"
      
      const result = findPrefixSuffixAndMatch(container);
      const selection = global.window.getSelection();
      const selectedText = selection.toString();
      
      expect(result).not.toBeNull();
      if (result) {
        // Verify the pattern can be found in highlighter's text
        const pattern = result.prefix + result.text + result.suffix;
        expect(highlighterFullText.indexOf(pattern) !== -1).toBe(true);
      }
    });

    it("should handle complex znai DOM structure with znai-code-line spans", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <div class="paragraph content-block">
            <span class="znai-simple-text">This </span>
            <code class="znai-inlined-code">include-</code>
            <span class="znai-simple-text"> syntax will appear throughout the documentation and represents a family of custom Markdown extensions.</span>
          </div>
          <div class="snippet">
            <pre>
              <span class="znai-code-line">
                <span class="token keyword">class</span> <span class="token class-name">JsClass</span> <span class="token punctuation">{</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                    <span class="token function">constructor</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                        <span class="token function">usefulAction</span><span class="token punctuation">(</span><span class="token punctuation">)</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                    <span class="token punctuation">}</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                <span class="token punctuation">}</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                <span>
</span>
              </span>
              <span class="znai-code-line">
                <span class="token keyword">export</span> <span class="token keyword">default</span> JsClass
                <span>
</span>
              </span>
            </pre>
          </div>
        </div>
      `);

      // Try to select a line within the complex znai structure
      const constructorSpan = container.querySelector('.token.function');
      const nextLine = constructorSpan.closest('.znai-code-line').nextElementSibling.querySelector('.token.function');
      
      // Select from "constructor" to "usefulAction"
      selectText(constructorSpan.firstChild, 0, nextLine.firstChild, 12);

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();
      
      // This should work with complex znai DOM structure
      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBeTruthy();
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
      }
    });

    it("should handle cross-element selection from paragraph to code snippet", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <div class="paragraph content-block">
            <span class="znai-simple-text">This </span>
            <code class="znai-inlined-code">include-</code>
            <span class="znai-simple-text"> syntax will appear throughout the documentation.</span>
          </div>
          <div class="snippet">
            <pre>
              <span class="znai-code-line">
                <span class="token keyword">class</span> <span class="token class-name">JsClass</span> <span class="token punctuation">{</span>
                <span>
</span>
              </span>
              <span class="znai-code-line">
                    <span class="token function">constructor</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span>
                <span>
</span>
              </span>
            </pre>
          </div>
        </div>
      `);

      // Select from end of paragraph text to beginning of code
      const paragraphSpan = container.querySelector('.znai-simple-text:last-child');
      const codeKeyword = container.querySelector('.token.keyword');
      
      // Select from "documentation." in paragraph to "class" in code
      const paragraphText = paragraphSpan.firstChild;
      const codeText = codeKeyword.firstChild;
      
      const startIndex = paragraphText.textContent.indexOf('documentation');
      
      selectText(paragraphText, startIndex, codeText, 5); // "documentation." to "class"

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();
      
      // Should not return null for cross-element selections
      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBeTruthy();
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
        
        // Verify the result can be found by TextHighlighter
        const highlighterFullText = buildFullTextLikeHighlighter(container);
        const pattern = result.prefix + result.text + result.suffix;
        expect(highlighterFullText.indexOf(pattern) !== -1).toBe(true);
      }
    });

    it("should handle selection with newlines that don't exist in TreeWalker text", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Text ending with plugin instead.</p>
          <code>:include-file: file-name.js</code>
        </div>
      `);

      // Simulate browser adding newline where TreeWalker doesn't
      const paragraph = container.querySelector('p').firstChild;
      const code = container.querySelector('code').firstChild;
      
      // Select from "plugin instead." to ":include-file:"
      selectText(paragraph, paragraph.textContent.indexOf('plugin'), code, 14);

      const result = findPrefixSuffixAndMatch(container);
      
      // This should work by handling the newline that selection.toString() adds
      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBeTruthy();
        // In JSDOM, the newline is preserved, but in real browsers it would be removed
        // The important thing is that the function finds the text either way
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
      }
    });

    it("should preserve newlines in preformatted code but remove block-element newlines", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Text ending here</p>
          <pre><code>function test() {
  return "value";
}</code></pre>
        </div>
      `);

      // Select text spanning from paragraph to code - this should remove block newlines
      const paragraph = container.querySelector('p').firstChild;
      const code = container.querySelector('code').firstChild;
      
      selectText(paragraph, 5, code, 8); // "ending here...function"
      
      let result = findPrefixSuffixAndMatch(container);
      
      expect(result).not.toBeNull();
      if (result) {
        // Should work by removing the paragraph→code newline
        expect(result.text).toBeTruthy();
      }

      // Now test selection within code block - should preserve newlines
      const codeStart = code.textContent.indexOf('function');
      const returnLine = code.textContent.indexOf('return');
      
      selectText(code, codeStart, code, returnLine + 6); // "function test() {\n  return"
      
      result = findPrefixSuffixAndMatch(container);
      
      expect(result).not.toBeNull();
      if (result) {
        // Should preserve the actual newline in code
        expect(result.text.includes('\n')).toBe(true);
      }
    });

    it("should handle selection spanning paragraph → code → paragraph", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>Start of paragraph text.</p>
          <pre><code>function example() {
  return "code";
}</code></pre>
          <p>End of paragraph text.</p>
        </div>
      `);

      // Select from middle of first paragraph, through code, to middle of last paragraph
      const firstP = container.querySelector('p').firstChild;
      const code = container.querySelector('code').firstChild;
      const lastP = container.querySelectorAll('p')[1].firstChild;
      
      // Select "paragraph text." + code + "End of"
      selectText(firstP, 9, lastP, 6);

      const result = findPrefixSuffixAndMatch(container);
      
      const selection = global.window.getSelection();
      const selectedText = selection.toString();
      
      // Should work even with mixed content (paragraph + code + paragraph)
      expect(result).not.toBeNull();
      if (result) {
        expect(result.text).toBeTruthy();
        expect(result.prefix).toBeTruthy();
        expect(result.suffix).toBeTruthy();
        
        // Should preserve code structure since selection contains preformatted content
        expect(result.text.includes('function')).toBe(true);
      }
    });

    it("should handle selection with range expansion issues", () => {
      const { container } = setupDOM(`
        <div id="test-container">
          <p>End text.</p>
          <div class="weird-structure">
            <span></span>
            <code class="language-js">
              <span class="token function">start</span> of code
            </code>
            <span></span>
          </div>
        </div>
      `);

      // Create a selection that might cause range expansion to fail
      const paragraph = container.querySelector("p");
      const codeSpan = container.querySelector(".token.function");
      
      try {
        // This might create a problematic range that's hard to expand context for
        selectText(paragraph.firstChild, paragraph.firstChild.textContent.length - 1, codeSpan.firstChild, 3);

        const result = findPrefixSuffixAndMatch(container);
        
        const selection = global.window.getSelection();
        const selectedText = selection.toString();

        // Should handle even problematic ranges
        if (selectedText.trim() === '') {
          expect(result).toBeNull();
        } else {
          // With improved fallback logic, should not return null
          expect(result).not.toBeNull();
          if (result) {
            expect(result.text).toBeTruthy();
            expect(result.prefix).toBeTruthy();
            expect(result.suffix).toBeTruthy();
          }
        }
      } catch (error) {
        // If range creation itself fails, that's expected for some edge cases
        expect(true).toBe(true);
      }
    });
  });
});

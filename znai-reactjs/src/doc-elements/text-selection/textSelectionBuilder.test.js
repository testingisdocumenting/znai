/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import { describe, it, expect, beforeEach } from 'vitest';
import { JSDOM } from 'jsdom';
import { findPrefixSuffixAndMatch } from './textSelectionBuilder.js';

describe('textSelectionBuilder', () => {
  let dom;
  let document;
  let window;
  let container;

  beforeEach(() => {
    dom = new JSDOM(`
      <!DOCTYPE html>
      <html>
        <body>
          <div id="test-container">
            <p>This is a sample text with multiple words.</p>
            <p>This text contains repeated phrases.</p>
            <p>The sample text is useful for testing.</p>
          </div>
          <div id="clickInfo"></div>
        </body>
      </html>
    `, { pretendToBeVisual: true });

    document = dom.window.document;
    window = dom.window;
    
    // Set up globals for the function to use
    global.window = window;
    global.document = document;
    global.Node = window.Node;
    global.NodeFilter = window.NodeFilter;

    container = document.getElementById('test-container');
    
    // JSDOM doesn't have innerText, so we need to polyfill it
    Object.defineProperty(container, 'innerText', {
      get() {
        return this.textContent;
      }
    });
  });

  describe('findPrefixSuffixAndMatch', () => {
    function selectText(startNode, startOffset, endNode, endOffset) {
      const range = document.createRange();
      range.setStart(startNode, startOffset);
      range.setEnd(endNode, endOffset);
      
      const selection = window.getSelection();
      selection.removeAllRanges();
      selection.addRange(range);
    }

    it('should find minimal unique prefix and suffix for selected text', () => {
      // Select "sample text" which appears twice
      const firstP = container.querySelector('p');
      const textNode = firstP.firstChild;
      
      // "This is a sample text with multiple words."
      // Select "sample" starting at position 10
      selectText(textNode, 10, textNode, 16);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('sample');
      // Should have found the minimal unique prefix/suffix
      expect(result.prefix).toBe('This is a ');
      expect(result.suffix).toBe(' text with');
      // Combined pattern should be unique
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const firstIndex = fullText.indexOf(pattern);
      const secondIndex = fullText.indexOf(pattern, firstIndex + 1);
      expect(firstIndex).toBeGreaterThan(-1);
      expect(secondIndex).toBe(-1); // Should be unique
    });

    it('should handle unique text with minimal context', () => {
      const firstP = container.querySelector('p');
      const textNode = firstP.firstChild;
      
      // Select "multiple" which is unique
      selectText(textNode, 27, textNode, 35);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('multiple');
      // For unique text, should still provide at least 10 chars of context
      expect(result.prefix).toBe('text with ');
      expect(result.suffix).toBe(' words.\n  ');
    });

    it('should handle selection across multiple nodes', () => {
      const paragraphs = container.querySelectorAll('p');
      const firstTextNode = paragraphs[0].firstChild;
      const secondTextNode = paragraphs[1].firstChild;
      
      // Select from "words." in first paragraph to "This" in second
      selectText(firstTextNode, 37, secondTextNode, 4);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      // The text includes whitespace between paragraphs
      expect(result.text).toContain('ords.');
      expect(result.text).toContain('This');
      expect(result.prefix).toBeTruthy();
      expect(result.suffix).toBeTruthy();
    });

    it('should handle repeated text by finding unique context', () => {
      const paragraphs = container.querySelectorAll('p');
      const firstTextNode = paragraphs[0].firstChild;
      
      // Select "text" which appears multiple times
      selectText(firstTextNode, 17, firstTextNode, 21);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('text');
      // Should find unique prefix/suffix combination
      expect(result.prefix).toBe(' a sample ');
      expect(result.suffix).toBe(' with mult');
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });

    it('should return minimum 10 characters for prefix and suffix when possible', () => {
      const lastP = container.querySelectorAll('p')[2];
      const textNode = lastP.firstChild;
      
      // Select "useful" which is unique
      selectText(textNode, 19, textNode, 25);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('useful');
      // Should have exactly 10 chars for unique text
      expect(result.prefix).toBe('e text is ');
      expect(result.suffix).toBe(' for testi');
    });

    it('should handle selection at the beginning of container', () => {
      const firstP = container.querySelector('p');
      const textNode = firstP.firstChild;
      
      // Select "This is" at the very beginning
      selectText(textNode, 0, textNode, 7);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('This is');
      // The prefix contains whitespace from the container structure
      expect(result.prefix).toBe('          '); 
      expect(result.suffix).toBe(' a sample ');
    });

    it('should handle selection at the end of container', () => {
      const lastP = container.querySelectorAll('p')[2];
      const textNode = lastP.firstChild;
      const text = textNode.textContent;
      
      // Select "testing." at the end
      selectText(textNode, text.length - 8, textNode, text.length);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('testing.');
      expect(result.prefix).toBe('seful for ');
      // The suffix contains whitespace from the container structure
      expect(result.suffix).toBe('\n         ');
    });

    it('should clean prefix to word boundary when beneficial', () => {
      const firstP = container.querySelector('p');
      const textNode = firstP.firstChild;
      
      // Select "is a sample"
      selectText(textNode, 5, textNode, 16);
      
      const result = findPrefixSuffixAndMatch(container);
      
      expect(result).toBeDefined();
      expect(result.text).toBe('is a sample');
      // Should have cleaned the prefix if beneficial
      expect(result.prefix.length).toBeGreaterThanOrEqual(10);
      expect(result.suffix).toBe(' text with');
      
      // Check if the pattern is unique
      const fullText = container.innerText;
      const pattern = result.prefix + result.text + result.suffix;
      const occurrences = fullText.split(pattern).length - 1;
      expect(occurrences).toBe(1);
    });
  });
});
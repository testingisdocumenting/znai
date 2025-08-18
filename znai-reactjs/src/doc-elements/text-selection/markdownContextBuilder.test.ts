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
import { buildContext } from "./markdownContextBuilder.js";
import { selectText, setupDOM } from "./selectionTestUtils.js";

describe("markdownContextBuilder", () => {
  describe("buildContext", () => {
    it("should extract code block and highlight selected lines across two lines", () => {
      const { container } = setupDOM(`
        <div class="snippet">
          <pre>
            <span class="znai-code-line"><span class="token keyword">class</span> <span class="token class-name">JsClass</span> <span class="token punctuation">{</span></span>
            <span class="znai-code-line">    <span class="token function">constructor</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span></span>
            <span class="znai-code-line">        <span class="token function">usefulAction</span><span class="token punctuation">(</span><span class="token punctuation">)</span></span>
            <span class="znai-code-line">    <span class="token punctuation">}</span></span>
            <span class="znai-code-line"><span class="token punctuation">}</span></span>
            <span class="znai-code-line"></span>
            <span class="znai-code-line"><span class="token keyword">export</span> <span class="token keyword">default</span> JsClass</span>
          </pre>
        </div>
      `);

      const lines = container.querySelectorAll('.znai-code-line');
      const constructorLine = lines[1]; // constructor line
      const usefulActionLine = lines[2]; // usefulAction line
      
      // Select from "constructor" to end of "usefulAction()"
      const constructorText = constructorLine.querySelector('.token.function');
      const usefulActionCloseParen = usefulActionLine.lastElementChild;
      
      selectText(
        constructorText.firstChild, 
        0, // start of "constructor"
        usefulActionCloseParen.firstChild,
        1  // after ")"
      );

      const result = buildContext(container);

      const expectedOutput = `class JsClass {
    **constructor() {** <----
        **usefulAction()** <----
    }
}

export default JsClass`;

      expect(result).toBe(expectedOutput);
    });

    it("should handle selection within a single line", () => {
      const { container } = setupDOM(`
        <div class="snippet">
          <pre>
            <span class="znai-code-line"><span class="token keyword">function</span> <span class="token function-name">calculateSum</span><span class="token punctuation">(</span><span class="token parameter">a</span>, <span class="token parameter">b</span><span class="token punctuation">)</span> <span class="token punctuation">{</span></span>
            <span class="znai-code-line">    <span class="token keyword">return</span> <span class="token variable">a</span> + <span class="token variable">b</span>;</span>
            <span class="znai-code-line"><span class="token punctuation">}</span></span>
          </pre>
        </div>
      `);

      const lines = container.querySelectorAll('.znai-code-line');
      const firstLine = lines[0];
      const functionName = firstLine.querySelector('.token.function-name');
      
      // Select just "calculateSum"
      selectText(functionName.firstChild, 0, functionName.firstChild, 12);

      const result = buildContext(container);

      const expectedOutput = `function **calculateSum**(a, b) { <----
    return a + b;
}`;

      expect(result).toBe(expectedOutput);
    });

    it("should return empty string when selection is outside code block", () => {
      const { container } = setupDOM(`
        <div>
          <p>Some text before code</p>
          <div class="snippet">
            <pre>
              <span class="znai-code-line">const x = 5;</span>
            </pre>
          </div>
          <p>Some text after code</p>
        </div>
      `);

      const paragraph = container.querySelector('p');
      selectText(paragraph.firstChild, 0, paragraph.firstChild, 4);

      const result = buildContext(container);

      expect(result).toBe("");
    });

    it("should return empty string when selection crosses code block boundary", () => {
      const { container } = setupDOM(`
        <div>
          <p>Text before</p>
          <div class="snippet">
            <pre>
              <span class="znai-code-line">const x = 5;</span>
            </pre>
          </div>
        </div>
      `);

      const paragraph = container.querySelector('p');
      const codeLine = container.querySelector('.znai-code-line');
      
      // Selection from paragraph to code line (crosses boundary)
      selectText(paragraph.firstChild, 0, codeLine.firstChild, 5);

      const result = buildContext(container);

      expect(result).toBe("");
    });

    it("should handle code block without znai-code-line spans", () => {
      const { container } = setupDOM(`
        <pre>function test() {
  return true;
}</pre>
      `);

      const pre = container.querySelector('pre');
      const textNode = pre.firstChild;
      
      // Select "test()"
      selectText(textNode, 9, textNode, 15);

      const result = buildContext(container);

      const expectedOutput = `function **test()** { <----
  return true;
}`;

      expect(result).toBe(expectedOutput);
    });
  });
});
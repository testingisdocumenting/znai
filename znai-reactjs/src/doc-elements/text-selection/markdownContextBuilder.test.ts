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
  describe("buildContext - code snippets", () => {
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

    it("should extract code block and highlight selected single word", () => {
      const className = container.querySelectorAll(".class-name")[0];

      selectText(className.firstChild, 0, className.firstChild, 5);

      const result = buildContext();

      const expectedOutput = `class **JsCla**ss { <----
    constructor() {
        usefulAction()
    }
}

export default JsClass`;

      expect(result).toBe(expectedOutput);
    });

    it("should extract code block and highlight selected lines across two lines", () => {
      const lines = container.querySelectorAll(".znai-code-line");
      const constructorLine = lines[1]; // constructor line
      const usefulActionLine = lines[2]; // usefulAction line

      const constructorText = constructorLine.querySelector(".token.function");
      const usefulActionCloseParen = usefulActionLine.lastElementChild;

      selectText(constructorText.firstChild, 0, usefulActionCloseParen.firstChild, 1);

      const result = buildContext();

      const expectedOutput = `class JsClass {
    **constructor() {** <----
        **usefulAction()** <----
    }
}

export default JsClass`;

      expect(result).toBe(expectedOutput);
    });
    /*
    <div class="snippet"><pre><span class="znai-code-line"><span class="token macro"><span class="token directive-hash">#</span><span class="token directive">include</span> <span class="token string">&lt;iostream&gt;</span></span><span>
</span></span><span class="znai-code-line"><span>
</span></span><span class="znai-code-line"><span class="token keyword">using</span> <span class="token keyword"><span class="znai-highlight single">namespace</span></span> std<span class="token punctuation">;</span><span>
</span></span><span class="znai-code-line"><span>
</span></span><span class="znai-code-line"><span class="token keyword">int</span> <span class="token function">main</span><span class="token punctuation">(</span><span class="token punctuation">)</span> <span class="token punctuation">{</span><span>
</span></span><span class="znai-code-line">    cout <span class="token operator">&lt;&lt;</span> <span class="token string">"hello"</span><span class="token punctuation">;</span><span>
</span></span><span class="znai-code-line"><span class="token punctuation">}</span><span>
</span></span></pre></div>
     */

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

      const lines = container.querySelectorAll(".znai-code-line");
      const firstLine = lines[0];
      const functionName = firstLine.querySelector(".token.function-name");

      // Select just "calculateSum"
      selectText(functionName.firstChild, 0, functionName.firstChild, 12);

      const result = buildContext();

      const expectedOutput = `function **calculateSum**(a, b) { <----
    return a + b;
}`;

      expect(result).toBe(expectedOutput);
    });

    it("should handle text selection outside code block as paragraph", () => {
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

      const paragraph = container.querySelector("p");
      selectText(paragraph.firstChild, 0, paragraph.firstChild, 4);

      const result = buildContext();

      expect(result).toBe("**Some** text before code \n                          const x = 5;\n          ...");
    });

    it("should handle selection crossing code block boundary as paragraph", () => {
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

      const paragraph = container.querySelector("p");
      const codeLine = container.querySelector(".znai-code-line");

      // Selection from paragraph to code line (crosses boundary)
      selectText(paragraph.firstChild, 0, codeLine.firstChild, 5);

      const result = buildContext();

      expect(result).toBe(
        "**Text** **before** \n                          **const** x = 5;\n          ...\n\n**Text** **before** \n                          **const** x = 5;"
      );
    });
  });

  describe("buildContext - paragraphs", () => {
    it("should highlight selected text within a single paragraph", () => {
      const { container } = setupDOM(`
        <div>
          <p>This is a test paragraph with some important text that should be highlighted.</p>
        </div>
      `);

      const paragraph = container.querySelector("p");
      const textNode = paragraph.firstChild;

      // Select "important text" - counting characters: "This is a test paragraph with some " = 35 chars
      selectText(textNode, 35, textNode, 49);

      const result = buildContext();

      expect(result).toBe("This is a test paragraph with some **important text** that should be highlighted.");
    });

    it("should add context before and after small paragraph", () => {
      const { container } = setupDOM(`
        <div>
          <p>Before text that provides context.</p>
          <p>Short selected.</p>
          <p>After text that provides more context.</p>
        </div>
      `);

      const paragraph = container.querySelectorAll("p")[1]; // middle paragraph
      const textNode = paragraph.firstChild;

      // Select "selected"
      selectText(textNode, 6, textNode, 14);

      const result = buildContext();

      expect(result).toBe(
        "Before text that provides context. Short **selected**. After text that provides more context."
      );
    });

    it("should handle selection across multiple paragraphs", () => {
      const { container } = setupDOM(`
        <div>
          <p>First paragraph with some text.</p>
          <p>Second paragraph with more text.</p>
        </div>
      `);

      const firstP = container.querySelectorAll("p")[0];
      const secondP = container.querySelectorAll("p")[1];

      // Select from "some text" in first to "more text" in second
      selectText(firstP.firstChild, 20, secondP.firstChild, 25);

      const result = buildContext();

      expect(result).toBe(
        "First **paragraph** **with** **some** text. **Second** **paragraph** **with** more text.\n\nFirst **paragraph** **with** **some** text. **Second** **paragraph** **with** more text."
      );
    });

    it("should return empty string for non-text selections in paragraphs", () => {
      const { container } = setupDOM(`
        <div>
          <p>Some paragraph text.</p>
        </div>
      `);

      const paragraph = container.querySelector("p");
      // Select empty range
      selectText(paragraph.firstChild, 5, paragraph.firstChild, 5);

      const result = buildContext();

      expect(result).toBe("");
    });

    it("should truncate long context text with ellipsis", () => {
      const { container } = setupDOM(`
        <div>
          <p>This is a very long paragraph that provides context before the target paragraph and should be truncated when it exceeds the fifty character limit.</p>
          <p>Target.</p>
          <p>This is another very long paragraph that provides context after the target paragraph and should also be truncated when it exceeds the limit.</p>
        </div>
      `);

      const paragraph = container.querySelectorAll("p")[1]; // middle paragraph
      const textNode = paragraph.firstChild;

      // Select "Target"
      selectText(textNode, 0, textNode, 6);

      const result = buildContext();

      expect(result).toBe(
        "...uncated when it exceeds the fifty character limit. **Target**. This is another very long paragraph that provides ..."
      );
    });
  });
});

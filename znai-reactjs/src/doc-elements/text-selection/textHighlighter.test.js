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

import { describe } from "vitest";

import { setupDOM } from "./selectionTestUtils.js";
import { TextHighlighter } from "./textHighlighter.js";

describe("textSelectionBuilder", () => {
  it("add highlight class to html text", () => {
    const { container } = setupDOM(`<div>
<p>This is a sample text with multiple words.</p>
<p>This text contains repeated phrases.</p>
<p>The sample text is useful for testing.</p></div>`);

    const highlighter = new TextHighlighter(container);
    highlighter.highlight("contains", "text ", " repeated");

    expect(container.innerHTML.trim()).toEqual(
      "<div>\n" +
        "<p>This is a sample text with multiple words.</p>\n" +
        '<p>This text <span class="znai-highlight single">contains</span> repeated phrases.</p>\n' +
        "<p>The sample text is useful for testing.</p></div>"
    );
  });

  it("add highlight class to svg text", () => {
    const { container } = setupDOM(`<svg>
<text>This is a sample text with multiple words.</text>
<text>This text contains repeated phrases.</text>
<text>The sample text is useful for testing.</text></svg>`);

    const highlighter = new TextHighlighter(container);
    highlighter.highlight("contains", "text ", " repeated");

    expect(container.innerHTML.trim()).toEqual(
      "<svg>\n" +
        "<text>This is a sample text with multiple words.</text>\n" +
        '<text>This text <tspan class="znai-highlight-svg">contains</tspan> repeated phrases.</text>\n' +
        "<text>The sample text is useful for testing.</text></svg>"
    );
  });
});

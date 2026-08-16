/*
 * Copyright 2026 znai maintainers
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
import { contentMatchesSearchSnippets } from "./searchSnippetsContentMatch";
import { DocElementContent } from "../default-elements/DocElement";

const content: DocElementContent = [
  {
    type: "Paragraph",
    content: [{ type: "SimpleText", text: "use cancel_trade to abort" } as any],
  },
  { type: "Snippet", snippet: "def my_func():\n  pass" } as any,
];

describe("contentMatchesSearchSnippets", () => {
  it("matches term inside nested text", () => {
    expect(contentMatchesSearchSnippets(content, ["cancel_trade"])).toBe(true);
    expect(contentMatchesSearchSnippets(content, ["my_func"])).toBe(true);
    expect(contentMatchesSearchSnippets(content, ["deploy", "abort"])).toBe(true);
  });

  it("ignores letters case", () => {
    expect(contentMatchesSearchSnippets(content, ["CANCEL_TRADE"])).toBe(true);
  });

  it("no match when terms are not part of content", () => {
    expect(contentMatchesSearchSnippets(content, ["deploy"])).toBe(false);
  });

  it("does not match against doc element type names", () => {
    expect(contentMatchesSearchSnippets(content, ["Paragraph"])).toBe(false);
  });

  it("no match when terms list is empty or content is missing", () => {
    expect(contentMatchesSearchSnippets(content, [])).toBe(false);
    expect(contentMatchesSearchSnippets(undefined, ["abort"])).toBe(false);
  });
});

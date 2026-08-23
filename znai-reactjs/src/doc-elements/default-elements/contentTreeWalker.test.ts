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
import { someTextValue, walkContentNodes } from "./contentTreeWalker";
import { DocElementContent } from "./DocElement";

const nestedContent: DocElementContent = [
  {
    type: "Section",
    content: [
      {
        type: "Paragraph",
        content: [{ type: "SimpleText", text: "hello" } as any],
      },
      { type: "Snippet" },
    ],
  },
  { type: "Meta" },
];

describe("walkContentNodes", () => {
  it("visits nodes depth-first passing ancestors chain", () => {
    const visited: string[] = [];
    const stopped = walkContentNodes(nestedContent, (node, ancestors) => {
      visited.push([...ancestors, node].map((n) => n.type).join(">"));
    });

    expect(stopped).toBe(false);
    expect(visited).toEqual([
      "Section",
      "Section>Paragraph",
      "Section>Paragraph>SimpleText",
      "Section>Snippet",
      "Meta",
    ]);
  });

  it("does not descend into children when visitor returns skip-children", () => {
    const visited: string[] = [];
    walkContentNodes(nestedContent, (node) => {
      visited.push(node.type);
      if (node.type === "Paragraph") {
        return "skip-children";
      }
    });

    expect(visited).toEqual(["Section", "Paragraph", "Snippet", "Meta"]);
  });

  it("ends the walk early when visitor returns stop", () => {
    const visited: string[] = [];
    const stopped = walkContentNodes(nestedContent, (node) => {
      visited.push(node.type);
      if (node.type === "Paragraph") {
        return "stop";
      }
    });

    expect(stopped).toBe(true);
    expect(visited).toEqual(["Section", "Paragraph"]);
  });

  it("handles missing content", () => {
    const stopped = walkContentNodes(undefined, () => "stop");
    expect(stopped).toBe(false);
  });
});

describe("someTextValue", () => {
  const value = {
    type: "Paragraph",
    content: [{ type: "SimpleText", text: "hello world" }],
    meta: { title: "nested title" },
  };

  it("matches strings in nested arrays and objects under any prop", () => {
    expect(someTextValue(value, (text) => text === "hello world")).toBe(true);
    expect(someTextValue(value, (text) => text === "nested title")).toBe(true);
    expect(someTextValue(value, (text) => text === "missing")).toBe(false);
  });

  it("skips doc element type names", () => {
    expect(someTextValue(value, (text) => text === "Paragraph")).toBe(false);
    expect(someTextValue(value, (text) => text === "SimpleText")).toBe(false);
  });

  it("stops scanning after the first match", () => {
    const scanned: string[] = [];
    someTextValue([{ text: "one" }, { text: "two" }], (text) => {
      scanned.push(text);
      return text === "one";
    });

    expect(scanned).toEqual(["one"]);
  });
});

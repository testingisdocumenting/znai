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

import { extractTabIds, buildContentForTab } from "./pageTabsContentUtils";

const paragraph = (text: string) => ({
  type: "Paragraph" as const,
  content: [{ type: "SimpleText" as const, text }],
});

const tabContent = (tabId: string, ...children: any[]) => ({
  type: "TabContent" as const,
  tabId,
  content: children,
});

const attentionBlock = (attentionType: string, ...content: any[]) => ({
  type: "AttentionBlock" as const,
  attentionType,
  content,
});

const section = (id: string, title: string, ...content: any[]) => ({
  type: "Section" as const,
  id,
  title,
  content,
});

describe("extractTabIds", () => {
  it("returns empty array for null content", () => {
    expect(extractTabIds(undefined)).toEqual([]);
  });

  it("extracts unique tab IDs preserving order", () => {
    const content = [
      section("s1", "Section 1", tabContent("java", paragraph("java")), tabContent("python", paragraph("python"))),
      section("s2", "Section 2", tabContent("java", paragraph("more java")), tabContent("cpp", paragraph("cpp"))),
    ];
    expect(extractTabIds(content)).toEqual(["java", "python", "cpp"]);
  });

  it("ignores non-TabContent elements", () => {
    const content = [section("s1", "Section 1", paragraph("hello"), tabContent("java", paragraph("java")))];
    expect(extractTabIds(content)).toEqual(["java"]);
  });

  it("finds TabContent nested inside container elements", () => {
    const content = [
      section(
        "s1",
        "Section 1",
        attentionBlock("note", tabContent("java", paragraph("java")), tabContent("python", paragraph("python")))
      ),
    ];
    expect(extractTabIds(content)).toEqual(["java", "python"]);
  });
});

describe("buildContentForTab", () => {
  it("keeps non-TabContent elements and keeps matching TabContent element", () => {
    const content = [
      section(
        "s1",
        "Section 1",
        paragraph("shared text"),
        tabContent("java", paragraph("java specific")),
        tabContent("python", paragraph("python specific")),
        paragraph("more shared")
      ),
    ];

    const result = buildContentForTab(content, "java");
    expect(result).toEqual([
      {
        type: "Section",
        id: "s1",
        title: "Section 1",
        content: [
          paragraph("shared text"),
          tabContent("java", paragraph("java specific")),
          paragraph("more shared"),
        ],
      },
    ]);
  });

  it("excludes non-matching tab content", () => {
    const content = [
      section(
        "s1",
        "Section 1",
        paragraph("shared"),
        tabContent("java", paragraph("java code")),
        tabContent("python", paragraph("python code"))
      ),
    ];

    const result = buildContentForTab(content, "python");
    expect(result![0].content).toEqual([paragraph("shared"), tabContent("python", paragraph("python code"))]);
  });

  it("keeps sections with empty content after filtering tab-only content", () => {
    const content = [
      section("s1", "Section 1", tabContent("java", paragraph("only java"))),
      section("s2", "Section 2", paragraph("always visible")),
    ];

    const result = buildContentForTab(content, "python");
    expect(result).toEqual([
      { type: "Section", id: "s1", title: "Section 1", content: [] },
      section("s2", "Section 2", paragraph("always visible")),
    ]);
  });

  it("keeps sections with no tab content unchanged", () => {
    const content = [section("s1", "Section 1", paragraph("always here"))];

    const result = buildContentForTab(content, "java");
    expect(result).toEqual(content);
  });

  it("handles multiple TabContent blocks with same tabId in one section", () => {
    const content = [
      section(
        "s1",
        "Section 1",
        tabContent("java", paragraph("java part 1")),
        paragraph("shared"),
        tabContent("java", paragraph("java part 2"))
      ),
    ];

    const result = buildContentForTab(content, "java");
    expect(result![0].content).toEqual([
      tabContent("java", paragraph("java part 1")),
      paragraph("shared"),
      tabContent("java", paragraph("java part 2")),
    ]);
  });

  it("filters TabContent nested inside container elements like AttentionBlock", () => {
    const content = [
      section(
        "s1",
        "Section 1",
        paragraph("shared"),
        attentionBlock(
          "note",
          tabContent("java", paragraph("java note")),
          tabContent("python", paragraph("python note"))
        )
      ),
    ];

    const result = buildContentForTab(content, "java");
    expect(result![0].content).toEqual([
      paragraph("shared"),
      attentionBlock("note", tabContent("java", paragraph("java note"))),
    ]);
  });

  it("removes container element when all nested TabContent is filtered out and container becomes empty", () => {
    const content = [
      section(
        "s1",
        "Section 1",
        paragraph("shared"),
        attentionBlock("warning", tabContent("python", paragraph("python only")))
      ),
    ];

    const result = buildContentForTab(content, "java");
    expect(result![0].content).toEqual([
      paragraph("shared"),
      attentionBlock("warning"),
    ]);
  });
});

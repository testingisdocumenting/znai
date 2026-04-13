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

import { hasTabContent, extractTabIds, buildContentForTab } from "./pageTabsContentUtils";

const paragraph = (text: string) => ({
  type: "Paragraph" as const,
  content: [{ type: "SimpleText" as const, text }],
});

const tabContent = (tabId: string, ...children: any[]) => ({
  type: "TabContent" as const,
  tabId,
  content: children,
});

const section = (id: string, title: string, ...content: any[]) => ({
  type: "Section" as const,
  id,
  title,
  content,
});

describe("hasTabContent", () => {
  it("returns false for null/empty content", () => {
    expect(hasTabContent(undefined)).toBe(false);
    expect(hasTabContent([])).toBe(false);
  });

  it("returns false when no TabContent elements", () => {
    const content = [section("s1", "Section 1", paragraph("hello"))];
    expect(hasTabContent(content)).toBe(false);
  });

  it("returns true when TabContent exists in section", () => {
    const content = [section("s1", "Section 1", paragraph("shared"), tabContent("java", paragraph("java code")))];
    expect(hasTabContent(content)).toBe(true);
  });
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
});

describe("buildContentForTab", () => {
  it("returns content as-is for null", () => {
    expect(buildContentForTab(null, "java")).toBe(null);
  });

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

  it("removes sections that become empty after filtering tab-only content", () => {
    const content = [
      section("s1", "Section 1", tabContent("java", paragraph("only java"))),
      section("s2", "Section 2", paragraph("always visible")),
    ];

    const result = buildContentForTab(content, "python");
    expect(result!.length).toBe(1);
    // @ts-ignore
    expect(result![0].id).toBe("s2");
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
});

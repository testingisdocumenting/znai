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

import { afterEach, beforeEach, describe, expect, it, vi } from "vitest";
import Search from "./Search";

const sectionOne = { type: "Section", id: "section-one", title: "Section One" };
const sectionTwo = { type: "Section", id: "section-two", title: "Section Two" };
const otherPageSection = { type: "Section", id: "other-section", title: "Other Section" };

const allPages = {
  pages: [
    {
      tocItem: { dirName: "chapter", fileName: "page" },
      content: [{ type: "Paragraph" }, sectionOne, sectionTwo],
    },
    {
      tocItem: { dirName: "chapter", fileName: "other-page" },
      content: [otherPageSection],
    },
  ],
};

function createSearch() {
  // search constructor reads pre-built index and data from window globals
  (window as any).znaiSearchIdx = {};
  (window as any).znaiSearchData = [];

  return new Search(allPages);
}

describe("Search", () => {
  beforeEach(() => {
    vi.spyOn(console, "error").mockImplementation(() => {});
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("finds section by full id", () => {
    const search = createSearch();
    const queryResult = { getSnippetsToHighlight: () => ["snippet"] };

    const previewDetails = search.previewDetails("chapter@@page@@section-two", queryResult);
    expect(previewDetails.section).toBe(sectionTwo);
    expect(previewDetails.snippets).toEqual(["snippet"]);
  });

  it("finds first section of the page when page section id is empty", () => {
    const search = createSearch();

    expect(search._findSectionById("chapter@@page@@")).toBe(sectionOne);
    expect(search._findSectionById("chapter@@other-page@@")).toBe(otherPageSection);
  });

  it("returns undefined and logs error for unknown id", () => {
    const search = createSearch();

    expect(search._findSectionById("chapter@@page@@no-such-section")).toBeUndefined();
    expect(console.error).toHaveBeenCalledWith("expected section associated with", "chapter@@page@@no-such-section");
  });

  it("builds section lookup lazily once and reuses it on repeated calls", () => {
    const search = createSearch();
    expect(search.sectionByIndexId).toBeNull();

    search._findSectionById("chapter@@page@@section-one");
    const lookup = search.sectionByIndexId;
    expect(lookup).not.toBeNull();

    search._findSectionById("chapter@@page@@section-two");
    search._findSectionById("chapter@@other-page@@");
    expect(search.sectionByIndexId).toBe(lookup);
  });
});

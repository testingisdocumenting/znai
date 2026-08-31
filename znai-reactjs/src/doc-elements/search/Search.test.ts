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
import { createLocalSearchIndex, populateLocalSearchIndexWithData } from "./flexSearch";

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

function createSearchWithData(searchData: string[][]) {
  const index = createLocalSearchIndex();
  populateLocalSearchIndexWithData(index, searchData);

  (window as any).znaiSearchIdx = index;
  (window as any).znaiSearchData = searchData;

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

  it("ranks verbatim matches first for queries with chars the token encoder strips", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Lists", "", "the list map structures overview", ""],
      ["chapter@@page@@section-two", "standard", "Streams", "", "use List.map to transform values", ""],
      ["chapter@@page@@other-section", "standard", "Guides", "", "", "List.map how to"],
    ]);

    // verbatim matches rank high priority text before standard text, token only matches go last
    const queryResult = search.search("List.map");
    expect(queryResult.getIds()).toEqual([
      "chapter@@page@@other-section",
      "chapter@@page@@section-two",
      "chapter@@page@@section-one",
    ]);

    // verbatim matched doc highlights the phrase, token matched doc highlights individual words
    expect(queryResult.getSnippetsToHighlight("chapter@@page@@section-two")).toEqual(["list.map"]);
    expect(queryResult.getSnippetsToHighlight("chapter@@page@@section-one")).toEqual(["Lists", "list", "map"]);
  });

  it("finds verbatim only queries like c++ that token search cannot represent", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Languages", "", "modern c++ guide", ""],
      ["chapter@@page@@section-two", "standard", "Languages", "", "python guide", ""],
    ]);

    const queryResult = search.search("c++");
    expect(queryResult.getIds()).toEqual(["chapter@@page@@section-one"]);
    expect(queryResult.getSnippetsToHighlight("chapter@@page@@section-one")).toEqual(["c++"]);
  });

  it("quoted query matches verbatim only without token fallback", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Combos", "", "use list map combo", ""],
      ["chapter@@page@@section-two", "standard", "Guides", "", "map list guide", ""],
    ]);

    // both docs match by tokens, only the first contains the phrase verbatim
    const unquoted = search.search("list map");
    expect(unquoted.getIds().sort()).toEqual(["chapter@@page@@section-one", "chapter@@page@@section-two"]);

    const quoted = search.search('"list map"');
    expect(quoted.getIds()).toEqual(["chapter@@page@@section-one"]);
    expect(quoted.getSnippetsToHighlight("chapter@@page@@section-one")).toEqual(["list map"]);
  });

  it("quoted query matches verbatim before the closing quote is typed", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Combos", "", "use list map combo", ""],
      ["chapter@@page@@section-two", "standard", "Guides", "", "map list guide", ""],
    ]);

    const queryResult = search.search('"list ma');
    expect(queryResult.getIds()).toEqual(["chapter@@page@@section-one"]);
    expect(queryResult.getSnippetsToHighlight("chapter@@page@@section-one")).toEqual(["list ma"]);
  });

  it("quoted query below min length returns no results instead of token noise", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Combos", "", "use list map combo", ""],
    ]);

    expect(search.search('"li').getIds()).toEqual([]);
    expect(search.search('""').getIds()).toEqual([]);
  });

  it("falls back to token matches when nothing matches verbatim", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Streams", "", "use List.map to transform values", ""],
    ]);

    const queryResult = search.search("map.");
    expect(queryResult.getIds()).toEqual(["chapter@@page@@section-one"]);
    expect(queryResult.getSnippetsToHighlight("chapter@@page@@section-one")).toEqual(["map"]);
  });

  it("builds exact match entries lazily and only for exact match queries", () => {
    const search = createSearchWithData([
      ["chapter@@page@@section-one", "standard", "Streams", "", "use List.map to transform values", ""],
    ]);
    expect(search.exactMatchEntries).toBeNull();

    search.search("transform");
    expect(search.exactMatchEntries).toBeNull();

    search.search("List.map");
    const entries = search.exactMatchEntries;
    expect(entries).not.toBeNull();

    search.search("List.filter");
    expect(search.exactMatchEntries).toBe(entries);
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

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

import { describe, expect, it, vi } from "vitest";
import { createLocalSearchIndex, encodeSearchQuery, searchIds, truncateQueryByMinLength } from "./flexSearch";
import QueryResult from "./QueryResult";

// type alias instead of interface so the implicit index signature satisfies flexsearch DocumentData
type TestDoc = {
  id: string;
  title?: string;
  content?: string;
  contentHigh?: string;
};

function createIndexAndTextById(docs: TestDoc[]) {
  const index = createLocalSearchIndex();
  const textById: Record<string, string> = {};
  docs.forEach((doc) => {
    index.add(doc);
    textById[doc.id] = [doc.title, doc.content, doc.contentHigh].filter(Boolean).join(" ");
  });

  return { index, textById };
}

function searchQueryResult(docs: TestDoc[], query: string) {
  const { index, textById } = createIndexAndTextById(docs);
  const ids = searchIds(index, query);
  return new QueryResult(ids, encodeSearchQuery(query), (id) => textById[id]);
}

describe("flex search", () => {
  it("multiple words highlight", () => {
    const queryResult = searchQueryResult(
      [
        {
          id: "id1",
          title: "some title",
          content: "running webs is future for you",
        },
        {
          id: "id2",
          title: "some title",
          content: "running fast into the future for you",
        },
        {
          id: "id3",
          title: "running future",
          content: "brown fox",
        },
      ],
      "ru future"
    );

    expect(queryResult.getIds()).toEqual(["id3", "id1", "id2"]);
    expect(queryResult.getSnippetsToHighlight("id3")).toEqual(["running", "future"]);
    expect(queryResult.getSnippetsToHighlight("id1")).toEqual(["running", "future"]);
    expect(queryResult.getSnippetsToHighlight("id2")).toEqual(["running", "future"]);
  });

  it("high content should go first", () => {
    const queryResult = searchQueryResult(
      [
        {
          id: "id1",
          title: "some title",
          content: "running webs is future for you",
        },
        {
          id: "id3",
          title: "running future",
          content: "apiCall",
        },
        {
          id: "id2",
          title: "some title",
          contentHigh: "apiCall",
        },
      ],
      "apicall"
    );

    expect(queryResult.getIds()).toEqual(["id2", "id3"]);
    // terms keep the original doc casing so downstream highlighting can locate them in the dom
    expect(queryResult.getSnippetsToHighlight("id2")).toEqual(["apiCall"]);
    expect(queryResult.getSnippetsToHighlight("id3")).toEqual(["apiCall"]);
  });

  it("min query term length", () => {
    expect(truncateQueryByMinLength("ty", 3)).toEqual("");
    expect(truncateQueryByMinLength("typing sl", 3)).toEqual("typing");
    expect(truncateQueryByMinLength("typing slo", 3)).toEqual("typing slo");
  });

  it("min query term length only counts searchable chars as the encoder strips the rest", () => {
    expect(truncateQueryByMinLength("bu-", 3)).toEqual("");
    expect(truncateQueryByMinLength("c++", 3)).toEqual("");
    expect(truncateQueryByMinLength("bu_", 3)).toEqual("bu_");
    expect(truncateQueryByMinLength("bu_id", 3)).toEqual("bu_id");
    expect(truncateQueryByMinLength("c+ typing", 3)).toEqual("typing");
  });

  it("underscore is part of code identifiers and matches by prefix", () => {
    const docs: TestDoc[] = [
      {
        id: "id1",
        title: "trading",
        content: "use bu_id to identify business unit",
      },
      {
        id: "id2",
        title: "building",
        content: "how to build and bundle",
      },
    ];

    const partialQueryResult = searchQueryResult(docs, "bu_");
    expect(partialQueryResult.getIds()).toEqual(["id1"]);
    expect(partialQueryResult.getSnippetsToHighlight("id1")).toEqual(["bu_id"]);

    const fullQueryResult = searchQueryResult(docs, "bu_id");
    expect(fullQueryResult.getIds()).toEqual(["id1"]);
    expect(fullQueryResult.getSnippetsToHighlight("id1")).toEqual(["bu_id"]);
  });

  it("code identifier with underscore matches by its start", () => {
    const docs: TestDoc[] = [
      {
        id: "id1",
        title: "config",
        content: "defines build_config for projects",
      },
    ];

    const prefixQueryResult = searchQueryResult(docs, "build");
    expect(prefixQueryResult.getIds()).toEqual(["id1"]);
    expect(prefixQueryResult.getSnippetsToHighlight("id1")).toEqual(["build_config"]);

    const underscoreQueryResult = searchQueryResult(docs, "build_c");
    expect(underscoreQueryResult.getIds()).toEqual(["id1"]);
    expect(underscoreQueryResult.getSnippetsToHighlight("id1")).toEqual(["build_config"]);
  });

  it("terms to highlight are derived lazily on demand and memoized per id", () => {
    const textToHighlightById = vi.fn((id: string) =>
      id === "id1" ? "running webs is future for you" : "running fast into the future"
    );

    const queryResult = new QueryResult(["id1", "id2"], encodeSearchQuery("ru future"), textToHighlightById);
    expect(textToHighlightById).not.toHaveBeenCalled();

    const terms = queryResult.getSnippetsToHighlight("id1");
    expect(terms).toEqual(["running", "future"]);
    expect(textToHighlightById).toHaveBeenCalledTimes(1);

    // repeated access reuses the memoized terms without re-deriving
    expect(queryResult.getSnippetsToHighlight("id1")).toBe(terms);
    expect(textToHighlightById).toHaveBeenCalledTimes(1);

    expect(queryResult.getSnippetsToHighlight("id2")).toEqual(["running", "future"]);
    expect(textToHighlightById).toHaveBeenCalledTimes(2);
  });
});

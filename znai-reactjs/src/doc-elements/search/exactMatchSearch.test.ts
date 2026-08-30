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

import { describe, expect, it } from "vitest";
import {
  buildExactMatchEntries,
  extractQuotedPhrase,
  normalizeExactMatchText,
  searchExactMatchIds,
} from "./exactMatchSearch";

function textPieces(pageTitle: string, textStandard: string, pageSection = "", textHigh = "") {
  return { pageTitle, pageSection, textStandard, textHigh };
}

describe("exact match search", () => {
  it("normalizes case and whitespace runs", () => {
    expect(normalizeExactMatchText("  List.map   fn  ")).toEqual("list.map fn");
  });

  it("extracts explicitly quoted phrase", () => {
    expect(extractQuotedPhrase('"list map"')).toEqual("list map");
    expect(extractQuotedPhrase('  "list map"  ')).toEqual("list map");
    expect(extractQuotedPhrase("“list map”")).toEqual("list map");

    expect(extractQuotedPhrase("list map")).toBeNull();
    expect(extractQuotedPhrase("")).toBeNull();
  });

  it("treats closing quote as optional so verbatim matching works while still typing", () => {
    expect(extractQuotedPhrase('"list ma')).toEqual("list ma");
    expect(extractQuotedPhrase('"')).toEqual("");
    expect(extractQuotedPhrase('""')).toEqual("");
  });

  it("matches verbatim substring ranking title, then high priority, then standard text matches", () => {
    const entries = buildExactMatchEntries({
      id1: textPieces("Streams", "use List.map to transform values", "Mapping"),
      id2: textPieces("List.map reference", "api details"),
      id3: textPieces("Lists", "a list of map structures"),
      id4: textPieces("Functional", "", "", "List.map essentials"),
    });

    expect(searchExactMatchIds(entries, "list.map")).toEqual(["id2", "id4", "id1"]);
  });

  it("does not match across title piece boundaries", () => {
    const entries = buildExactMatchEntries({
      id1: textPieces("Ends with List.", "", "map begins"),
    });

    expect(searchExactMatchIds(entries, "list. map")).toEqual([]);
  });

  it("caps results at limit keeping title matches over earlier content matches", () => {
    const entries = buildExactMatchEntries({
      c1: textPieces("no match in title", "has c++ inside"),
      t1: textPieces("c++ title", ""),
      t2: textPieces("another c++ title", ""),
    });

    expect(searchExactMatchIds(entries, "c++", 2)).toEqual(["t1", "t2"]);
    expect(searchExactMatchIds(entries, "c++")).toEqual(["t1", "t2", "c1"]);
  });

  it("returns nothing for an empty query", () => {
    const entries = buildExactMatchEntries({ id1: textPieces("title", "content") });

    expect(searchExactMatchIds(entries, "")).toEqual([]);
  });
});

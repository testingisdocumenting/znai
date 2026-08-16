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

import { Document } from "flexsearch";
import FlexSearch from "flexsearch";

// default encoder splits terms on any non alphanumeric char, keep underscore as part of terms
// so code identifiers like `bu_id` are indexed as is and can be found by typing `bu_`
const searchEncoder = new FlexSearch.Encoder({
  include: {
    letter: true,
    number: true,
    char: "_",
  },
});

export function createLocalSearchIndex() {
  return new FlexSearch.Document({
    preset: "score",
    tokenize: "forward",
    context: true,
    store: true,
    resolution: 3,
    encoder: searchEncoder,
    document: {
      id: "id",
      index: [
        {
          field: "title",
          tokenize: "forward",
        },
        {
          field: "contentHigh",
          tokenize: "forward",
        },
        {
          field: "content",
          tokenize: "forward",
          // maybe include EnglishBookPreset in future
          // encoder: EnglishBookPreset,
        },
      ],
      store: ["content", "contentHigh"],
    },
  });
}

export function populateLocalSearchIndexWithData(index: Document, data: string[][]) {
  data.forEach((e) => {
    const title = e[2] + " " + e[3];
    index.add({
      id: e[0],
      title,
      content: title + " " + e[4],
      contentHigh: title + " " + e[5],
    });
  });
}

export interface SearchResult {
  id: string;
  type?: string;
  termsToHighlight: string[];
}

const highlightRegex = /@\w+\b/g;

// highlight re-encodes the full stored content of every result, so cost per keystroke
// is linear in the number of results (flexsearch default is 100 per field)
const resultsPerFieldLimit = 30;

export function searchWithHighlight(index: Document, query: string) {
  const searchResults = index.search(query, {
    enrich: true,
    limit: resultsPerFieldLimit,
    highlight: { template: "@$1" },
  });

  const withHighlights: SearchResult[] = [];
  for (let idx = 0; idx < searchResults.length; idx++) {
    const forFieldParent = searchResults[idx];
    const results = forFieldParent.result;

    for (let resultIdx = 0; resultIdx < results.length; resultIdx++) {
      const subResult = results[resultIdx];
      let termsToHighlight: string[] = [];
      if (subResult.highlight) {
        // highlight marks every occurrence, dedupe so the same word is not highlighted multiple times downstream
        termsToHighlight = [
          ...new Set(
            (subResult.highlight.match(highlightRegex) || [])
              .map((term) => term.substring(1))
              .filter((term) => term.length > 2)
          ),
        ];
      }

      withHighlights.push({
        id: subResult.id.toString(),
        type: forFieldParent.field,
        termsToHighlight,
      });
    }
  }

  return withHighlights;
}

const nonSearchableCharsRegex = /[^\p{L}\p{N}_]+/gu;

export function truncateQueryByMinLength(query: string, minLength: number) {
  return query
    .split(" ")
    .map((e) => e.trim())
    .filter((e) => effectiveTermLength(e) >= minLength)
    .join(" ");
}

// search encoder strips chars other than alphanumeric and underscore, e.g. "c++" is searched as the one char prefix "c",
// so only searchable chars count towards the min length, otherwise "c++" bypasses the guard
// and triggers an expensive short prefix search
function effectiveTermLength(term: string) {
  return term.replace(nonSearchableCharsRegex, "").length;
}

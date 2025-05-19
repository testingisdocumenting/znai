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

import FlexSearch, { Document } from "flexsearch";

export function createLocalSearchIndex() {
  return new FlexSearch.Document({
    preset: "score",
    tokenize: "forward",
    context: true,
    store: true,
    resolution: 3,
    document: {
      id: "id",
      index: [
        {
          field: "title",
          tokenize: "forward",
        },
        // for contentHigh use custom encoder that allows underscored and symbols like semicolons(?)
        // maybe allow search from the middle as well
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
export function searchWithHighlight(index: Document, query: string) {
  const searchResults = index.search(query, { enrich: true, highlight: { template: "@$1" } });

  const withHighlights: SearchResult[] = [];
  for (let idx = 0; idx < searchResults.length; idx++) {
    const forFieldParent = searchResults[idx];
    const results = forFieldParent.result;

    for (let resultIdx = 0; resultIdx < results.length; resultIdx++) {
      const subResult = results[resultIdx];
      let termsToHighlight: string[] = [];
      if (subResult.highlight) {
        termsToHighlight = (subResult.highlight.match(highlightRegex) || [])
          .map((term) => term.substring(1))
          .filter((term) => term.length > 2);
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

export function truncateQueryByMinLength(query: string, minLength: number) {
  return query
    .split(" ")
    .map((e) => e.trim())
    .filter((e) => e.length >= minLength)
    .join(" ");
}

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

// flexsearch default is 100 results per field, exact match search (see exactMatchSearch.ts)
// shares the cap so both modes surface a similar amount of results
export const resultsPerFieldLimit = 30;

// per keystroke search fetches ids only, no enrich/highlight, so cost does not grow
// with the size of the stored content, highlight terms are derived lazily by QueryResult
// when a result is actually displayed
export function searchIds(index: Document, query: string): string[] {
  const searchResults = index.search(query, { limit: resultsPerFieldLimit });

  // union across fields keeping first occurrence, so title matches stay ranked before content matches
  const ids: string[] = [];
  const seen = new Set<string>();
  for (let idx = 0; idx < searchResults.length; idx++) {
    const forFieldResult = searchResults[idx].result;
    for (let resultIdx = 0; resultIdx < forFieldResult.length; resultIdx++) {
      const id = forFieldResult[resultIdx].toString();
      if (!seen.has(id)) {
        seen.add(id);
        ids.push(id);
      }
    }
  }

  return ids;
}

export function encodeSearchQuery(query: string): string[] {
  return searchEncoder.encode(query);
}

// encoder normalizes words (lowercase, letter dedupe: "running" -> "runing"), so encoded tokens
// can't be handed to the dom highlighter, split the raw text with the encoder's own word splitter instead,
// the splitter is not part of the public typings but is derived from the include config above
const encoderWordSplit = (searchEncoder as unknown as { split: RegExp }).split;

// queries with chars the encoder strips, e.g. "." in "List.map" or "+" in "c++", can't be fully
// represented by the token index, whitespace aside as it separates terms instead of being stripped from them,
// derived from the encoder's own split regex so the check can't drift from the include config above
export function hasCharsStrippedByEncoder(query: string): boolean {
  return query.split(encoderWordSplit).join("") !== query.replace(/\s+/g, "");
}

// index uses tokenize: "forward", so words whose encoded form prefix matches an encoded query term
// mirror what flexsearch matched, searchEncoder is the single source of truth for tokenization
export function deriveTermsToHighlight(encodedQueryTerms: string[], text: string): string[] {
  if (encodedQueryTerms.length === 0) {
    return [];
  }

  // dedupe so the same word is not highlighted multiple times downstream,
  // short terms produce too much highlight noise
  const terms = new Set<string>();
  const words = text.split(encoderWordSplit);
  for (let idx = 0; idx < words.length; idx++) {
    const word = words[idx];
    if (word.length <= 2 || terms.has(word)) {
      continue;
    }

    const encodedWord = searchEncoder.encode(word);
    if (encodedWord.some((token) => encodedQueryTerms.some((queryTerm) => token.startsWith(queryTerm)))) {
      terms.add(word);
    }
  }

  return [...terms];
}

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
  return searchEncoder.encode(term).join("").length;
}

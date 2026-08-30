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

import { resultsPerFieldLimit } from "./flexSearch";

// token based index (see flexSearch.ts) strips chars like "." and "+", so queries such as
// "List.map" or "c++" either match too broadly or not at all. queries containing stripped chars
// (see hasCharsStrippedByEncoder) are additionally matched verbatim as a case-insensitive
// substring of the raw search text.

export interface ExactMatchEntry {
  id: string;
  // texts to match against in rank order mirroring the token index fields
  // (see createLocalSearchIndex): title, high priority text, standard text
  rankedTexts: string[];
}

// named subset of the searchData columns, decoded once by mapById in Search.js
export interface SearchEntryTextPieces {
  pageTitle: string;
  pageSection: string;
  textStandard: string;
  textHigh: string;
}

// flexsearch has no quote syntax of its own, its encoder just strips quotes,
// so quotes are free to mean "match this verbatim", e.g. "list map",
// curly variants included as mobile keyboards substitute them
const quoteChars = ['"', "“", "”"];

// returns the text inside explicit quotes or null when the query is not quoted,
// the closing quote is optional so verbatim matching kicks in while the phrase is still being typed
export function extractQuotedPhrase(query: string): string | null {
  const trimmed = query.trim();
  if (!quoteChars.includes(trimmed.charAt(0))) {
    return null;
  }

  const hasClosingQuote = trimmed.length > 1 && quoteChars.includes(trimmed.charAt(trimmed.length - 1));
  return hasClosingQuote ? trimmed.slice(1, -1) : trimmed.slice(1);
}

// collapse whitespace runs and lowercase so a phrase matches regardless of casing
// and of how spaces made it into the query or the text
export function normalizeExactMatchText(text: string): string {
  return text.trim().replace(/\s+/g, " ").toLowerCase();
}

// entries are normalized once so every keystroke search is a plain substring scan,
// title pieces are joined with a newline, which never survives query normalization,
// so a phrase can't accidentally match across unrelated pieces
export function buildExactMatchEntries(searchEntryById: Record<string, SearchEntryTextPieces>): ExactMatchEntry[] {
  return Object.entries(searchEntryById).map(([id, entry]) => ({
    id,
    rankedTexts: [
      joinTextPieces(entry.pageTitle, entry.pageSection),
      normalizeExactMatchText(entry.textHigh),
      normalizeExactMatchText(entry.textStandard),
    ],
  }));
}

function joinTextPieces(...pieces: string[]): string {
  return pieces.map(normalizeExactMatchText).join("\n");
}

// native substring scan over the pre-normalized entries: v8 string search covers megabytes of text
// well within the keystroke debounce, no extra index needed and the ui thread never hangs.
// one pass per rank tier: a match in an earlier tier outranks any match in a later one
export function searchExactMatchIds(entries: ExactMatchEntry[], normalizedQuery: string, limit = resultsPerFieldLimit) {
  if (normalizedQuery.length === 0 || entries.length === 0) {
    return [];
  }

  const tiersCount = entries[0].rankedTexts.length;
  const ids: string[] = [];
  for (let tier = 0; tier < tiersCount && ids.length < limit; tier++) {
    for (let idx = 0; idx < entries.length && ids.length < limit; idx++) {
      const entry = entries[idx];
      if (matchedTierUpTo(entry.rankedTexts, normalizedQuery, tier) === tier) {
        ids.push(entry.id);
      }
    }
  }

  return ids;
}

// index of the first ranked text containing the query, scanning no further than maxTier,
// so a pass re-checks only the small earlier tiers to keep an entry in its highest matching tier
// while the bulky standard text is only ever scanned on its own pass
function matchedTierUpTo(rankedTexts: string[], query: string, maxTier: number): number {
  for (let tier = 0; tier <= maxTier; tier++) {
    if (rankedTexts[tier].includes(query)) {
      return tier;
    }
  }

  return -1;
}

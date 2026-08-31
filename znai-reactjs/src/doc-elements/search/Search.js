/*
 * Copyright 2021 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import QueryResult from "./QueryResult";
import { encodeSearchQuery, hasCharsStrippedByEncoder, searchIds, truncateQueryByMinLength } from "./flexSearch.js";
import {
  buildExactMatchEntries,
  extractQuotedPhrase,
  normalizeExactMatchText,
  searchExactMatchIds,
} from "./exactMatchSearch";

const minQueryTermLength = 3;

class Search {
  constructor(allPages) {
    this.allPages = allPages;
    this.searchIdx = window.znaiSearchIdx;
    this.searchData = window.znaiSearchData;
    this.searchDataById = mapById(this.searchData);
    // built lazily on first preview lookup, the constructor runs on doc load even if search is never used
    this.sectionByIndexId = null;
    // built lazily on first exact match query, most queries are token based
    this.exactMatchEntries = null;
  }

  static convertIndexIdToSectionCoords(indexId) {
    const [dirName, fileName, pageSectionId] = indexId.split("@@");
    return { dirName, fileName, pageSectionId };
  }

  search(term) {
    const textToHighlightById = (id) => this._textToHighlightById(id);

    // explicitly quoted queries, e.g. "list map", match verbatim only without token fallback,
    // quoting is the way to say tokens alone are not good enough; no encoded terms are needed
    // as every result is an exact match and highlights the phrase itself
    const quotedPhrase = extractQuotedPhrase(term);
    if (quotedPhrase !== null) {
      const phrase = normalizeExactMatchText(quotedPhrase);
      const exactIds = this._searchExactMatchIds(phrase);

      return new QueryResult(exactIds, [], textToHighlightById, { phrase, ids: new Set(exactIds) });
    }

    const query = truncateQueryByMinLength(term, minQueryTermLength);
    const tokenIds = searchIds(this.searchIdx, query);
    const encodedQueryTerms = encodeSearchQuery(query);

    // queries with chars the token encoder strips, e.g. "List.map" or "c++", are additionally
    // matched verbatim so docs containing the exact text rank before docs merely containing the tokens
    const phrase = normalizeExactMatchText(term);
    if (!hasCharsStrippedByEncoder(phrase)) {
      return new QueryResult(tokenIds, encodedQueryTerms, textToHighlightById);
    }

    const exactIds = this._searchExactMatchIds(phrase);
    const exactIdsSet = new Set(exactIds);
    const ids = exactIds.concat(tokenIds.filter((id) => !exactIdsSet.has(id)));

    return new QueryResult(ids, encodedQueryTerms, textToHighlightById, { phrase, ids: exactIdsSet });
  }

  findSearchEntryById(id) {
    return this.searchDataById[id];
  }

  // short phrases substring match too many docs, return nothing instead of noise while the user is still typing
  _searchExactMatchIds(phrase) {
    if (phrase.length < minQueryTermLength) {
      return [];
    }

    return searchExactMatchIds(this._exactMatchEntries(), phrase);
  }

  _exactMatchEntries() {
    if (this.exactMatchEntries === null) {
      this.exactMatchEntries = buildExactMatchEntries(this.searchDataById);
    }

    return this.exactMatchEntries;
  }

  // same text pieces the index docs are built from, see populateLocalSearchIndexWithData
  _textToHighlightById(id) {
    const searchEntry = this.searchDataById[id];
    if (!searchEntry) {
      return "";
    }

    return [searchEntry.pageTitle, searchEntry.pageSection, searchEntry.textStandard, searchEntry.textHigh].join(" ");
  }

  previewDetails(id, queryResult) {
    const section = this._findSectionById(id);
    const snippets = queryResult.getSnippetsToHighlight(id);

    return { section, snippets };
  }

  _findSectionById(indexId) {
    if (this.sectionByIndexId === null) {
      this.sectionByIndexId = buildSectionByIndexId(this.allPages);
    }

    const sectionCoords = Search.convertIndexIdToSectionCoords(indexId);
    const key = sectionLookupKey(sectionCoords.dirName, sectionCoords.fileName, sectionCoords.pageSectionId || "");

    const section = this.sectionByIndexId.get(key);
    if (section === undefined) {
      console.error("expected section associated with", indexId);
    }

    return section;
  }
}

function sectionLookupKey(dirName, fileName, pageSectionId) {
  return dirName + "@@" + fileName + "@@" + pageSectionId;
}

// index sections both by their id and by page alone (empty section id means first section of the page)
function buildSectionByIndexId(allPages) {
  const result = new Map();

  allPages.pages.forEach((p) => {
    const tocItem = p.tocItem;

    p.content.forEach((de) => {
      if (de.type !== "Section") {
        return;
      }

      const pageKey = sectionLookupKey(tocItem.dirName, tocItem.fileName, "");
      if (!result.has(pageKey)) {
        result.set(pageKey, de);
      }

      if (de.id) {
        const idKey = sectionLookupKey(tocItem.dirName, tocItem.fileName, de.id);
        if (!result.has(idKey)) {
          result.set(idKey, de);
        }
      }
    });
  });

  return result;
}

function mapById(searchData) {
  const result = {};
  searchData.forEach(([id, section, pageTitle, pageSection, textStandard, textHigh]) => {
    result[id] = { section, pageTitle, pageSection, textStandard, textHigh };
  });

  return result;
}

export default Search;

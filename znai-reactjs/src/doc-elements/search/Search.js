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
import { encodeSearchQuery, searchIds, truncateQueryByMinLength } from "./flexSearch.js";

class Search {
  constructor(allPages) {
    this.allPages = allPages;
    this.searchIdx = window.znaiSearchIdx;
    this.searchDataById = mapById(window.znaiSearchData);
    // built lazily on first preview lookup, the constructor runs on doc load even if search is never used
    this.sectionByIndexId = null;
  }

  static convertIndexIdToSectionCoords(indexId) {
    const [dirName, fileName, pageSectionId] = indexId.split("@@");
    return { dirName, fileName, pageSectionId };
  }

  search(term) {
    const query = truncateQueryByMinLength(term, 3);
    const ids = searchIds(this.searchIdx, query);
    return new QueryResult(ids, encodeSearchQuery(query), (id) => this._textToHighlightById(id));
  }

  findSearchEntryById(id) {
    return this.searchDataById[id];
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

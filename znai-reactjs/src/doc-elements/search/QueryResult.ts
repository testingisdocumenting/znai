/*
 * Copyright 2024 znai maintainers
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

import { SearchResult } from "./flexSearch";

export default class QueryResult {
  private readonly queryResultsById: Record<string, SearchResult>;
  constructor(queryResults: SearchResult[]) {
    this.queryResultsById = queryResults.reduce((acc, searchResult) => {
      acc[searchResult.id] = searchResult;
      return acc;
    }, {} as Record<string, SearchResult>);
  }

  getIds() {
    return Object.keys(this.queryResultsById);
  }

  getSnippetsToHighlight(id: string) {
    return this.queryResultsById[id].termsToHighlight;
  }
}

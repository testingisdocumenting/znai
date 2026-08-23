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

import { deriveTermsToHighlight } from "./flexSearch";

export default class QueryResult {
  private readonly ids: string[];
  private readonly encodedQueryTerms: string[];
  private readonly textToHighlightById: (id: string) => string;
  // terms are derived lazily per id, only the displayed result pays for it
  private readonly termsToHighlightById: Record<string, string[]> = {};

  constructor(ids: string[], encodedQueryTerms: string[], textToHighlightById: (id: string) => string) {
    this.ids = ids;
    this.encodedQueryTerms = encodedQueryTerms;
    this.textToHighlightById = textToHighlightById;
  }

  getIds() {
    return this.ids;
  }

  getSnippetsToHighlight(id: string) {
    let terms = this.termsToHighlightById[id];
    if (!terms) {
      terms = deriveTermsToHighlight(this.encodedQueryTerms, this.textToHighlightById(id));
      this.termsToHighlightById[id] = terms;
    }

    return terms;
  }
}

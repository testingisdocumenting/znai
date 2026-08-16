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

import { DocElementContent } from "../default-elements/DocElement";

/**
 * checks if doc elements content contains any of the matched search terms.
 * lets elements with hidden content (e.g. read more) decide whether to auto reveal it
 * during search instead of rendering and highlighting every hidden block.
 */
export function contentMatchesSearchSnippets(content: DocElementContent | undefined, searchSnippets: string[]) {
  if (!content || searchSnippets.length === 0) {
    return false;
  }

  const loweredSnippets = searchSnippets.map((snippet) => snippet.toLowerCase());
  return anyTextValueMatches(content, loweredSnippets);
}

function anyTextValueMatches(value: unknown, loweredSnippets: string[]): boolean {
  if (typeof value === "string") {
    const text = value.toLowerCase();
    return loweredSnippets.some((snippet) => text.includes(snippet));
  }

  if (Array.isArray(value)) {
    return value.some((entry) => anyTextValueMatches(entry, loweredSnippets));
  }

  if (typeof value === "object" && value !== null) {
    for (const key in value) {
      // type holds doc element names like Snippet and is not a visible text
      if (key !== "type" && anyTextValueMatches((value as Record<string, unknown>)[key], loweredSnippets)) {
        return true;
      }
    }
  }

  return false;
}

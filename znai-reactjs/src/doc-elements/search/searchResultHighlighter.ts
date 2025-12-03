/*
 * Copyright 2025 znai maintainers
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

// @ts-ignore
import Mark from "mark.js/dist/mark.js";

export function highlightSearchResultAndMaybeScroll(root: HTMLElement, snippets: string[], scroll: boolean) {
  const mark = new Mark(root);
  mark.unmark({
    done: () => {
      mark.mark(snippets, {
        acrossElements: false,
        separateWordSearch: true,
        caseSensitive: false,
        ignoreJoiners: false,
        diacritics: false,
        ignorePunctuation: ["(", ")", ";", "[", "]", "-", "_", ".", ",", '"', "'", "~"],
        accuracy: "partially",
        done: () => {
          const marked = root.querySelector("mark");
          if (marked && scroll) {
            marked.scrollIntoView();
          }
        },
      });
    },
  });
}

export function removeSearchHighlight(root: HTMLElement) {
  const mark = new Mark(root);
  mark.unmark({});
}

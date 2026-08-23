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

const observeConfig = { childList: true, subtree: true };

/**
 * highlights search snippets inside root and keeps highlighting content that mounts later,
 * e.g. a read more block revealed while a search result is displayed.
 * content components stay unaware of the highlight mechanics this way.
 *
 * returned dispose stops watching for late mounted content,
 * highlights are removed separately with removeSearchHighlight
 */
export function startSearchHighlightSession(root: HTMLElement, snippets: string[]): () => void {
  markSnippets(root, snippets);

  const observer = new MutationObserver((mutations) => {
    const lateMounted: HTMLElement[] = [];
    for (const mutation of mutations) {
      for (const node of Array.from(mutation.addedNodes)) {
        if (node instanceof HTMLElement && node.tagName !== "MARK") {
          lateMounted.push(node);
        }
      }
    }

    if (lateMounted.length === 0) {
      return;
    }

    // pause observing while marking so mark.js own dom changes don't re-trigger this callback
    observer.disconnect();
    markSnippets(lateMounted, snippets);
    observer.observe(root, observeConfig);
  });

  observer.observe(root, observeConfig);

  return () => observer.disconnect();
}

export function removeSearchHighlight(root: HTMLElement) {
  const mark = new Mark(root);
  mark.unmark({});
}

// mark.js accepts a single element or an array of elements as context,
// late mounted nodes from one react commit are marked in a single pass
function markSnippets(root: HTMLElement | HTMLElement[], snippets: string[]) {
  const mark = new Mark(root);
  mark.unmark({
    done: () => {
      mark.mark(snippets, {
        acrossElements: false,
        separateWordSearch: true,
        caseSensitive: false,
        ignoreJoiners: false,
        diacritics: false,
        // underscore is deliberately not here: it is part of indexed symbols like cancel_trade
        ignorePunctuation: ["(", ")", ";", "[", "]", "-", ".", ",", '"', "'", "~"],
        accuracy: "partially",
        done: () => revealHiddenMatches(root),
      });
    },
  });
}

// highlights can land inside content collapsed with hidden="until-found", e.g. a read more block
// on a page a search result points to. fire the same beforematch event the browser fires for
// find-in-page matches, so owning components reveal through their one existing reveal path
function revealHiddenMatches(root: HTMLElement | HTMLElement[]) {
  const hiddenContainers = new Set<Element>();
  for (const element of Array.isArray(root) ? root : [root]) {
    for (const mark of Array.from(element.querySelectorAll("mark[data-markjs]"))) {
      // reveal every hidden ancestor, matching browser behavior for nested until-found regions
      for (let hidden = mark.closest("[hidden]"); hidden; hidden = hidden.parentElement?.closest("[hidden]") ?? null) {
        hiddenContainers.add(hidden);
      }
    }
  }

  hiddenContainers.forEach((container) => container.dispatchEvent(new Event("beforematch")));
}

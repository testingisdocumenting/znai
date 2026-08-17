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

import { RefObject, useEffect, useLayoutEffect } from "react";

/**
 * hides collapsible content with hidden="until-found" so browser find-in-page can still match it,
 * onFindInPageReveal is invoked when the browser reveals the content on a match so the owner
 * can sync its expanded state.
 *
 * the attribute is set manually because react normalizes hidden to a boolean and drops the value.
 * browsers without until-found support treat it as plain hidden which matches display none behavior
 */
export function useHiddenUntilFound(
  hiddenContainerRef: RefObject<HTMLElement | null>,
  hidden: boolean,
  onFindInPageReveal: () => void
) {
  useLayoutEffect(() => {
    const hiddenContainer = hiddenContainerRef.current;
    if (!hiddenContainer) {
      return;
    }

    if (hidden) {
      hiddenContainer.setAttribute("hidden", "until-found");
    } else {
      hiddenContainer.removeAttribute("hidden");
    }
  }, [hidden]);

  // browser fires beforematch right before revealing hidden content matched by find-in-page
  useEffect(() => {
    const hiddenContainer = hiddenContainerRef.current;
    if (!hiddenContainer) {
      return;
    }

    hiddenContainer.addEventListener("beforematch", onFindInPageReveal);
    return () => hiddenContainer.removeEventListener("beforematch", onFindInPageReveal);
  }, [onFindInPageReveal]);
}

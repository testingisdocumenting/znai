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

import { useEffect } from "react";
import { TextHighlighter } from "./textHighlihter";
import { mainPanelClassName } from "../../layout/classNames";
import { extractHighlightParams } from "./highlightUrl";

export function HighlightUrlText() {
  useEffect(() => {
    const params = extractHighlightParams();
    if (params) {
      const container = document.querySelector(mainPanelClassName) || document.body;
      const highlighter = new TextHighlighter(container);
      highlighter.highlight(params.selection, params.prefix, params.suffix);

      const firstHighlight = document.querySelector(".znai-highlight");
      if (firstHighlight) {
        firstHighlight.scrollIntoView({ behavior: "smooth", block: "center" });
      }
    }
  }, []);

  return null;
}

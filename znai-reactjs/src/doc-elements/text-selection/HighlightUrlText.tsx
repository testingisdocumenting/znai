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

import { useEffect, useRef } from "react";
import { TextHighlighter } from "./textHighlihter";
import { mainPanelClassName } from "../../layout/classNames";
import { extractHighlightParams } from "./highlightUrl";
import "./HighlightUrlText.css";

export function HighlightUrlText({ containerNode }: { containerNode: HTMLDivElement }) {
  const bubbleRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
    const params = extractHighlightParams();

    if (params) {
      const container = document.querySelector(mainPanelClassName) || document.body;
      const highlighter = new TextHighlighter(container);
      const highlights = highlighter.highlight(params.selection, params.prefix, params.suffix);
      const firstHighlightedElement = highlights[0];
      if (!firstHighlightedElement) {
        return;
      }

      if (params.question && bubbleRef.current) {
        const containerRect = containerNode.getBoundingClientRect();

        const range = document.createRange();
        range.setStart(firstHighlightedElement, 0);
        range.setEnd(highlights[highlights.length - 1], highlights[highlights.length - 1].childNodes.length);
        const selectionRect = range.getBoundingClientRect();

        const bubble = bubbleRef.current;
        bubble.innerText = params.question;
        bubble.style.display = "block";
        
        // Measure bubble dimensions after setting content
        const bubbleRect = bubble.getBoundingClientRect();
        const bubbleWidth = bubbleRect.width;
        
        const top = selectionRect.top - containerRect.top + containerNode.scrollTop - 60;
        const selectionCenter = selectionRect.left + selectionRect.width / 2.0;
        const left = selectionCenter - bubbleWidth / 2.0 - containerRect.left;

        bubble.style.top = `${top}px`;
        bubble.style.left = `${left}px`;
      }

      setTimeout(() => {
        if (firstHighlightedElement) {
          firstHighlightedElement.scrollIntoView({ behavior: "smooth", block: "center" });
        }
      }, 100);
    }
  }, []);

  return <div ref={bubbleRef} className="znai-highlight-question-bubble" />;
}

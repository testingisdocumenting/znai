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

import React, { useEffect, useRef, useState } from "react";

import { DocElementProps } from "../default-elements/DocElement";
import { Icon } from "../icons/Icon";

import { useHighlightOfHiddenElement } from "../text-selection/componentsHighlightUtils";
import { contentMatchesSearchSnippets } from "../search/searchSnippetsContentMatch";
import { highlightSearchResultAndMaybeScroll } from "../search/searchResultHighlighter";
import "./ReadMore.css";

interface Props extends DocElementProps {
  title: string;
}

export function ReadMore({ title, content, searchSnippets, elementsLibrary }: Props) {
  const isPartOfSearch = searchSnippets !== undefined;

  // during search auto reveal only when content has matched search terms,
  // pages with dozens of read more blocks are too expensive to render and highlight fully expanded
  const [expanded, setExpanded] = useState(
    () => searchSnippets !== undefined && contentMatchesSearchSnippets(content, searchSnippets)
  );
  const containerRef = useRef<HTMLDivElement | null>(null);
  const hiddenContainerRef = useRef<HTMLDivElement>(null);
  const hasHiddenHighlightedElement = useHighlightOfHiddenElement(
    containerRef,
    hiddenContainerRef,
    expanded,
    isPartOfSearch
  );

  // highlight search terms inside revealed content, manually revealed content
  // is not covered by the initial search highlight pass
  useEffect(() => {
    if (expanded && searchSnippets && containerRef.current) {
      highlightSearchResultAndMaybeScroll(containerRef.current, searchSnippets, false);
    }
  }, [expanded]);

  // during search, collapsed content is not mounted to avoid rendering and highlighting hidden blocks,
  // regular pages keep hidden content mounted so the highlight engine can find it
  const renderContent = expanded || !isPartOfSearch;

  const expandedClassName = expanded ? "expanded" : "collapsed";
  const topClassName = "znai-read-more content-block " + expandedClassName;
  const summaryClassName =
    "znai-read-more-title-block content-block " +
    expandedClassName +
    (hasHiddenHighlightedElement && !expanded ? " " + "znai-highlight single" : "");
  const summary = (
    <div className={summaryClassName} onClick={() => setExpanded((prev) => !prev)}>
      <Icon id="chevron-right" className="znai-read-more-icon" />
      <span className="znai-read-more-title">{title}</span>
    </div>
  );
  const style = expanded ? { display: "block" } : { display: "none" };
  return (
    <div className={topClassName} ref={containerRef}>
      {summary}
      <div className="znai-read-more-content content-block" style={style} ref={hiddenContainerRef}>
        {renderContent && (
          <elementsLibrary.DocElement content={content} elementsLibrary={elementsLibrary} searchSnippets={searchSnippets} />
        )}
      </div>
    </div>
  );
}

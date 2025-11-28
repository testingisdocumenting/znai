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

import React, { useEffect } from "react";
import { afterTitleId } from "../../../layout/classNamesAndIds";
import { DocElementProps } from "../../default-elements/DocElement";
import { SearchResultId } from "../../search/SearchResultId";
import { TocItem } from "../../../structure/TocItem";
import { highlightSearchResultAndMaybeScroll, removeSearchHighlight } from "../../search/searchResultHighlighter";

interface Props extends DocElementProps {
  tocItem: TocItem;
  searchResult: { id: SearchResultId; snippetsToHighlight: string[] };
  contentRootDom: HTMLElement;
  removeSearchResult: () => void;
}

export function DefaultPageContent(props: Props) {
  const { elementsLibrary, content, searchResult, tocItem, contentRootDom, removeSearchResult } = props;
  const { PageTitle } = elementsLibrary;

  const searchResultId = searchResult?.id;
  const searchSnippetsToHighlight = searchResult?.snippetsToHighlight;

  const isSearchResultOnThisPage =
    searchResultId && searchResultId.dirName === tocItem.dirName && searchResultId.fileName === tocItem.fileName;

  useEffect(() => {
    if (searchSnippetsToHighlight && isSearchResultOnThisPage && contentRootDom) {
      highlightSearchResultAndMaybeScroll(contentRootDom, searchSnippetsToHighlight, false);
    }
  }, [searchSnippetsToHighlight]);

  useEffect(() => {
    const handleKeyDown = (event: KeyboardEvent) => {
      if (event.key === "Escape" && isSearchResultOnThisPage) {
        removeSearchHighlight(contentRootDom);
        removeSearchResult();
        event.stopPropagation();
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [isSearchResultOnThisPage]);

  const renderedSections = content!.map((section) => {
    // @ts-ignore
    const id = section.id;
    const isPartOfSearch = isSearchResultOnThisPage && id === searchResultId.pageSectionId;
    return (
      <elementsLibrary.Section
        key={id}
        {...section}
        elementsLibrary={elementsLibrary}
        isPartOfSearch={isPartOfSearch}
        highlight={isPartOfSearch}
      />
    );
  });

  return (
    <>
      <div className="content-block">
        <PageTitle {...props} />
      </div>
      {renderedSections}
      <div id={afterTitleId}></div>
    </>
  );
}

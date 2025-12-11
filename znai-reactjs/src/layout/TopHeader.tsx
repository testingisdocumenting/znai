/*
 * Copyright 2024 znai maintainers
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

import React from "react";
import { DocMeta } from "../structure/docMeta";
import TocPanelSearch from "./TocPanelSearch";
import { DarkLightThemeSwitcher } from "./DarkLightThemeSwitcher";

import "./TopHeader.css";
import { TocItem } from "../structure/TocItem";

interface Props {
  docMeta: DocMeta;
  toc: TocItem[];
  selectedTocItem?: TocItem;
  onTocItemClick(dirName: string, fileName: string): void;
  scrollToTop(): void;
  scrollToPageSection(id: string): void;
  onTitleClick: () => void;
  onSearchClick: () => void;
}

export function TopHeader({
  docMeta,
  toc,
  selectedTocItem,
  onTitleClick,
  onSearchClick,
  onTocItemClick,
  scrollToTop,
  scrollToPageSection,
}: Props) {
  return (
    <div className="znai-top-header">
      <div className="znai-top-header-logo-and-title-and-selected-toc-item">
        <div className="znai-documentation-logo" />
        <div className="znai-top-header-title" onClick={onTitleClick}>
          {docMeta.title + " " + docMeta.type}
        </div>
        <div className="znai-top-header-selected-toc-item">{renderSelectedTocItem()}</div>
      </div>

      <div className="znai-top-header-right-toolbar">
        <div className="znai-top-header-search-area">
          <TocPanelSearch onClick={onSearchClick} />
        </div>

        <div className="znai-top-header-theme-switcher">
          <DarkLightThemeSwitcher />
        </div>
      </div>
    </div>
  );

  function chapterOnClick(dirName: string) {
    const found = toc.find((tocItem) => tocItem.dirName === dirName);

    if (!found) {
      return;
    }

    if (!found.items || found.items.length === 0) {
      return;
    }

    const firstPage = found.items[0];
    onTocItemClick(firstPage.dirName, firstPage.fileName);
  }

  function renderSelectedTocItem() {
    if (!selectedTocItem) {
      return null;
    }

    const parts = [];

    if (selectedTocItem.chapterTitle) {
      parts.push(
        <div
          key="chapter"
          className="znai-top-header-selected-chapter-title"
          onClick={() => chapterOnClick(selectedTocItem?.dirName)}
        >
          {selectedTocItem.chapterTitle}
        </div>
      );
    }

    if (selectedTocItem.pageTitle) {
      parts.push(
        <div key="page" className="znai-top-header-selected-page-title" onClick={() => scrollToTop()}>
          {selectedTocItem.pageTitle}
        </div>
      );
    }

    if (selectedTocItem.anchorId && selectedTocItem.pageSectionIdTitles) {
      const foundSectionTitle = selectedTocItem.pageSectionIdTitles.find(
        (idTitle) => idTitle.id === selectedTocItem.anchorId
      );

      if (foundSectionTitle) {
        const sectionId = selectedTocItem?.anchorId!;
        const href = "#" + sectionId;

        parts.push(
          <div
            key="section"
            className="znai-top-header-selected-section-title"
            onClick={() => scrollToPageSection(sectionId)}
          >
            <a href={href}>{foundSectionTitle.title}</a>
          </div>
        );
      }
    }

    const interleaved = [];
    if (parts.length > 0) {
      for (let idx = 0; idx < parts.length; idx++) {
        const isLast = idx === parts.length - 1;
        interleaved.push(parts[idx]);
        if (!isLast) {
          interleaved.push(
            <div key={"delimiter" + idx} className="znai-top-header-selected-delimiter">
              /
            </div>
          );
        }
      }
    }

    return <>{interleaved}</>;
  }
}

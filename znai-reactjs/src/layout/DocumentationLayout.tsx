/*
 * Copyright 2020 znai maintainers
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

import React, { useState } from "react";

import TocPanel from "./TocPanel";
import { PageGenError } from "../doc-elements/page-gen-error/PageGenError";
import { DocMeta } from "../structure/docMeta";
import { TocItem } from "../structure/TocItem";

import { useIsMobile } from "../theme/ViewPortContext";

import { TocMobileHeader } from "./mobile/TocMobileHeader";

import { TocMobilePanel } from "./mobile/TocMobilePanel";

import "./DocumentationLayout.css";
import "./mobile/MobileLayoutOverrides.css";

interface Props {
  searchPopup: React.ReactNode;
  renderedPage: React.ReactNode;
  renderedNextPrevNavigation: React.ReactNode;
  renderedFooter: React.ReactNode;
  docMeta: DocMeta;
  toc: Partial<TocItem>[];
  selectedTocItem?: TocItem;

  onHeaderClick(): void;

  onSearchClick(): void;

  onTocItemClick(dirName: string, fileName: string): void;

  onTocItemPageSectionClick(sectionId: string): void;

  onNextPage(): void;

  onPrevPage(): void;

  pageGenError?: string;
}

export const mainPanelClassName = "znai-main-panel";

export function DocumentationLayout({
  searchPopup,
  renderedPage,
  renderedNextPrevNavigation,
  renderedFooter,
  docMeta,
  toc,
  selectedTocItem,
  onHeaderClick,
  onSearchClick,
  onTocItemClick,
  onTocItemPageSectionClick,
  onNextPage,
  onPrevPage,
  pageGenError,
}: Props) {
  const [isMobileTocVisible, setMobileTocVisible] = useState(false);
  const isMobile = useIsMobile();

  const pageGenErrorPanel = pageGenError ? (
    <PageGenError error={pageGenError} />
  ) : null;

  const panelFullClassName = mainPanelClassName + (isMobile ? " mobile" : "");
  const DocumentationWrapper = isMobile ? React.Fragment : Documentation;

  return (
    <DocumentationWrapper>
      {!isMobile && (
        <div className="side-panel">
          <TocPanel
            toc={toc}
            docMeta={docMeta}
            selectedItem={selectedTocItem}
            onHeaderClick={onHeaderClick}
            onTocItemClick={onTocItemClick}
            onTocItemPageSectionClick={onTocItemPageSectionClick}
            onSearchClick={onSearchClick}
            onNextPage={onNextPage}
            onPrevPage={onPrevPage}
          />
        </div>
      )}

      {searchPopup}

      {isMobile && (
        <TocMobileHeader
          docMeta={docMeta}
          onHeaderClick={onHeaderClickAndCloseMenu}
          onMenuClick={toggleMobileToc}
        />
      )}

      {isMobileTocVisible ? (
        <TocMobilePanel
          toc={toc}
          onTocItemClick={selectTocItem}
          onTocItemPageSectionClick={selectPageSection}
        />
      ) : (
        <div className={panelFullClassName}>
          {renderedPage}

          <div className="page-bottom">
            {renderedNextPrevNavigation}
            {renderedFooter}
          </div>
        </div>
      )}

      {pageGenErrorPanel}
    </DocumentationWrapper>
  );

  function selectTocItem(dirName: string, fileName: string) {
    hideMobileToc();
    onTocItemClick(dirName, fileName);
  }

  function selectPageSection(sectionId: string) {
    hideMobileToc();
    onTocItemPageSectionClick(sectionId);
  }

  function onHeaderClickAndCloseMenu() {
    hideMobileToc();
    onHeaderClick();
  }

  function toggleMobileToc() {
    setMobileTocVisible(!isMobileTocVisible);
  }

  function hideMobileToc() {
    setMobileTocVisible(false);
  }
}

interface DocumentationProps {
  children: React.ReactNode;
}

function Documentation({ children }: DocumentationProps) {
  return <div className="documentation">{children}</div>;
}

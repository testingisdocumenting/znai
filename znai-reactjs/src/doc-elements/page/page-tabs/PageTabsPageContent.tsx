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

import React from "react";
import { afterTitleId } from "../../../layout/classNamesAndIds";
import PageTabsSelection from "./PageTabsSelection";
import { extractTabIds, buildContentForTab } from "./pageTabsContentUtils";
import { findParentWithScroll } from "../../../utils/domNodes";

interface ScrollSnapshot {
  parentWithScroll: HTMLElement;
  anchorNode: HTMLElement;
  anchorOffsetFromTop: number;
}

interface PageTabsState {
  activeTabId: string;
}

class PageTabsPageContent extends React.Component<any, PageTabsState> {
  contentRef: React.RefObject<HTMLDivElement>;

  constructor(props: any) {
    super(props);

    const tabIds = extractTabIds(props.content);
    this.state = { activeTabId: tabIds[0] || "" };
    this.contentRef = React.createRef();
  }

  render() {
    const { elementsLibrary, content, ...props } = this.props;
    const { PageTitle } = elementsLibrary;
    const { activeTabId } = this.state;

    const tabIds = extractTabIds(content);
    const filteredContent = buildContentForTab(content, activeTabId);

    const renderedSections = filteredContent!.map((section: any) => {
      const id = section.id;
      return <elementsLibrary.Section key={id} {...section} elementsLibrary={elementsLibrary} />;
    });

    return (
      <div ref={this.contentRef}>
        <div className="content-block">
          <PageTitle {...props} elementsLibrary={elementsLibrary} />
        </div>
        <PageTabsSelection tabIds={tabIds} activeTabId={activeTabId} onTabSelect={this.onTabSelect} />
        {renderedSections}
        <div id={afterTitleId}></div>
      </div>
    );
  }

  onTabSelect = (tabId: string) => {
    if (tabId === this.state.activeTabId) {
      return;
    }

    this.setState({ activeTabId: tabId });
  };

  getSnapshotBeforeUpdate(_prevProps: any, prevState: PageTabsState): ScrollSnapshot | null {
    if (prevState.activeTabId === this.state.activeTabId) {
      return null;
    }

    const node = this.contentRef.current;
    if (!node) {
      return null;
    }

    const parentWithScroll = findParentWithScroll(node);
    if (!parentWithScroll) {
      return null;
    }

    // find the shared (non-tab) content element closest to the viewport center
    // shared elements live outside .znai-page-tab-content wrappers and are stable across tab switches
    const anchor = findSharedAnchorNearViewportCenter(node);
    if (!anchor) {
      return null;
    }

    return {
      parentWithScroll,
      anchorNode: anchor.node,
      anchorOffsetFromTop: anchor.top,
    };
  }

  componentDidUpdate(_prevProps: any, _prevState: PageTabsState, snapshot: ScrollSnapshot | null) {
    if (!snapshot || !snapshot.anchorNode) {
      return;
    }

    // shared content nodes inside sections survive re-render
    // because sections use stable keys
    if (!snapshot.anchorNode.isConnected) {
      return;
    }

    const newTop = snapshot.anchorNode.getBoundingClientRect().top;
    const diffY = newTop - snapshot.anchorOffsetFromTop;
    snapshot.parentWithScroll.scrollTop += diffY;
  }
}

function findSharedAnchorNearViewportCenter(rootNode: HTMLElement): { node: HTMLElement; top: number } | null {
  const candidates = rootNode.querySelectorAll<HTMLElement>(".content-block, .znai-container");
  const viewportCenter = window.innerHeight / 2;

  let best: HTMLElement | null = null;
  let bestDistance = Infinity;
  let bestTop = 0;

  for (const el of candidates) {
    if (el.closest(".znai-page-tab-content")) {
      continue;
    }

    const rect = el.getBoundingClientRect();
    const elCenter = rect.top + rect.height / 2;
    const distance = Math.abs(elCenter - viewportCenter);

    if (distance < bestDistance) {
      bestDistance = distance;
      best = el;
      bestTop = rect.top;
    }
  }

  if (!best) {
    return null;
  }

  return { node: best, top: bestTop };
}

export default PageTabsPageContent;

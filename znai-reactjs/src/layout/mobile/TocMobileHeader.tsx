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

import React, { useEffect, useState } from 'react';

import { DocMeta } from '../../structure/docMeta';
import { TocItem } from '../../structure/TocItem';

import { Icon } from '../../doc-elements/icons/Icon';

import './TocMobileHeader.css';

interface Props {
  docMeta: DocMeta;
  selectedTocItem?: TocItem;

  onHeaderClick(): void;
  onMenuClick(): void;
  scrollToPageSection(id: string): void;
}

interface ScrolledPastHeader {
  id: string;
  title: string;
}

// Reveal the sticky bar a bit before the first header fully clears the top so the transition feels smooth.
const EARLY_REVEAL_OFFSET = 24;

// `scrollIntoView` lands a target with `scroll-margin-top` (see Page.css `.page-content.with-page-tabs [id]`
// at 32px) leaving the h1's top below the viewport edge. A header counts as "active" once its top has
// reached this offset, so a click-jump correctly identifies the jumped-to section instead of the one above.
const ACTIVE_DETECTION_OFFSET = 40;

export function TocMobileHeader({ docMeta, selectedTocItem, onHeaderClick, onMenuClick, scrollToPageSection }: Props) {
  const [scrolledPast, setScrolledPast] = useState<ScrolledPastHeader | null>(null);

  useEffect(() => {
    const headers = Array.from(document.querySelectorAll<HTMLElement>(".znai-section-title")).filter((h) => h.id);
    if (headers.length === 0) {
      setScrolledPast(null);
      return;
    }

    let queued = false;

    const apply = () => {
      queued = false;

      // Active section = most recent header whose top has crossed the active-detection threshold.
      // Updates the instant a new h1 reaches the top, so the bar can swap text without a hide-window.
      let active: HTMLElement | null = null;
      for (const h of headers) {
        if (h.getBoundingClientRect().top <= ACTIVE_DETECTION_OFFSET) {
          active = h;
        } else {
          break;
        }
      }

      // Visibility is gated only by the first header having scrolled past — keeps the bar hidden
      // at page start (no echo of the visible h1) but mounted across section transitions
      // (so the next h1 doesn't briefly take over the screen on its own).
      const showBar = active && headers[0].getBoundingClientRect().bottom <= EARLY_REVEAL_OFFSET;

      let next: ScrolledPastHeader | null = null;
      if (showBar) {
        const titleEl = active!.querySelector(".znai-section-title-text");
        next = { id: active!.id, title: (titleEl?.textContent ?? "").trim() };
      }

      setScrolledPast((prev) => {
        if (!next) return prev === null ? prev : null;
        if (prev && prev.id === next.id && prev.title === next.title) return prev;
        return next;
      });
    };

    const onScroll = () => {
      if (queued) return;
      queued = true;
      requestAnimationFrame(apply);
    };

    window.addEventListener("scroll", onScroll, { passive: true });
    apply();
    return () => window.removeEventListener("scroll", onScroll);
  }, [selectedTocItem?.dirName, selectedTocItem?.fileName]);

  return (
    <>
      <div className="znai-mobile-header">
        <div className="znai-mobile-header-logo-title">
          <div className="znai-documentation-logo mobile" />
          <div className="znai-mobile-header-title" onClick={onHeaderClick}>
            {docMeta.title + " " + docMeta.type}
          </div>
        </div>
        <div className="znai-mobile-header-burger">
          <Icon id="menu" onClick={onMenuClick} />
        </div>
      </div>

      {scrolledPast && (
        <div
          className="znai-mobile-sticky-section-title"
          onClick={() => scrollToPageSection(scrolledPast.id)}
        >
          <div className="znai-mobile-sticky-section-title-text">{scrolledPast.title}</div>
        </div>
      )}
    </>
  );
}

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

// Reveal the sticky bar a bit before the header fully clears the top so the transition feels smooth.
const EARLY_REVEAL_OFFSET = 24;

export function TocMobileHeader({ docMeta, selectedTocItem, onHeaderClick, onMenuClick, scrollToPageSection }: Props) {
  const [scrolledPast, setScrolledPast] = useState<ScrolledPastHeader | null>(null);

  useEffect(() => {
    const headers = Array.from(document.querySelectorAll<HTMLElement>(".znai-section-title")).filter((h) => h.id);
    if (headers.length === 0) {
      setScrolledPast(null);
      return;
    }

    const update = () => {
      let mostRecent: HTMLElement | null = null;
      for (const h of headers) {
        if (h.getBoundingClientRect().bottom <= EARLY_REVEAL_OFFSET) {
          mostRecent = h;
        } else {
          break;
        }
      }
      if (mostRecent) {
        const titleEl = mostRecent.querySelector(".znai-section-title-text");
        setScrolledPast({ id: mostRecent.id, title: (titleEl?.textContent ?? "").trim() });
      } else {
        setScrolledPast(null);
      }
    };

    const observer = new IntersectionObserver(update, {
      threshold: 0,
      rootMargin: `-${EARLY_REVEAL_OFFSET}px 0px 0px 0px`,
    });
    headers.forEach((h) => observer.observe(h));
    return () => observer.disconnect();
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

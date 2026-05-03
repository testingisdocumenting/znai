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

import React, { useEffect, useState } from "react";

import { DocMeta } from "../../structure/docMeta";
import { TocItem } from "../../structure/TocItem";

import { Icon } from "../../doc-elements/icons/Icon";

import "./TocMobileHeader.css";

interface Props {
  docMeta: DocMeta;
  selectedTocItem?: TocItem;

  onHeaderClick(): void;
  onMenuClick(): void;
  scrollToPageSection(id: string): void;
}

interface ActiveHeader {
  id: string;
  title: string;
}

interface HeaderInfo extends ActiveHeader {
  el: HTMLElement;
}

// Reveal the sticky bar a bit before the first header fully clears the top so the transition feels smooth.
const EARLY_REVEAL_OFFSET = 24;

// `scrollIntoView` lands a target with `scroll-margin-top` (Page.css `.page-content.with-page-tabs [id]` = 32px),
// so a header counts as "active" once its top is within 40px of the viewport edge — otherwise a click-jump
// would identify the section above the jumped-to one.
const ACTIVE_DETECTION_OFFSET = 40;

export function TocMobileHeader({ docMeta, selectedTocItem, onHeaderClick, onMenuClick, scrollToPageSection }: Props) {
  const [active, setActive] = useState<ActiveHeader | null>(null);

  useEffect(() => {
    const headers: HeaderInfo[] = Array.from(document.querySelectorAll<HTMLElement>(".znai-section-title"))
      .filter((el) => el.id)
      .map((el) => ({
        el,
        id: el.id,
        title: (el.querySelector(".znai-section-title-content")?.textContent ?? "").trim(),
      }));

    if (headers.length === 0) {
      setActive(null);
      return;
    }

    let queued = false;

    const apply = () => {
      queued = false;

      let current: HeaderInfo | null = null;
      for (const h of headers) {
        if (h.el.getBoundingClientRect().top <= ACTIVE_DETECTION_OFFSET) {
          current = h;
        } else {
          break;
        }
      }

      const showBar = current && headers[0].el.getBoundingClientRect().bottom <= EARLY_REVEAL_OFFSET;

      setActive((prev) => {
        if (!showBar) {
          return prev === null ? prev : null;
        }

        if (prev && prev.id === current!.id) {
          return prev;
        }

        return { id: current!.id, title: current!.title };
      });
    };

    const onScroll = () => {
      if (queued) {
        return;
      }

      queued = true;
      requestAnimationFrame(apply);
    };

    window.addEventListener("scroll", onScroll, { passive: true });
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

      {active && (
        <div className="znai-mobile-sticky-section-title" onClick={() => scrollToPageSection(active.id)}>
          <div className="znai-mobile-sticky-section-title-text">{active.title}</div>
        </div>
      )}
    </>
  );
}

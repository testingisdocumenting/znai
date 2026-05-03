/*
 * Copyright 2020 znai maintainers
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

import React, { useEffect, useRef, useState } from "react";

import { Icon } from "../icons/Icon";
import { presentationModeListeners } from "../presentation/PresentationModeListener";

import { TextBadge } from "../badge/TextBadge";

import { DocElementContent, ElementsLibraryMap } from "./DocElement";
import { HeadingContent } from "./HeadingContent";
import { elementsLibrary } from "../DefaultElementsLibrary";

import { isPresentationButtonVisible } from "../../structure/docMeta";
import { useIsMobile } from "../../theme/ViewPortContext";

import "./SectionTitle.css";
import "./HeadingStyles.css";

interface Props {
  id: string;
  title: string;
  headingContent?: DocElementContent;
  badge?: string;
  style?: string;
  elementsLibrary?: ElementsLibraryMap;
}

export function SectionTitle({ id, title, headingContent, badge, style }: Props) {
  const sentinelRef = useRef<HTMLDivElement>(null);
  const [isStuck, setIsStuck] = useState(false);
  const isMobile = useIsMobile();

  useEffect(() => {
    const sentinel = sentinelRef.current;
    if (!isMobile || !sentinel) {
      setIsStuck(false);
      return;
    }
    // Observe a sibling sentinel above the title rather than the title itself: the title's box
    // changes when `is-stuck` toggles (padding, border, ellipsis vs wrap), and observing it would
    // feed those layout changes back into the observer and cause a flip-flop loop. The sentinel's
    // position depends only on content above the title, so it stays stable across stuck toggles.
    // rootMargin top matches the title's mobile margin-top so the trigger fires exactly when the
    // title pins at viewport top.
    const observer = new IntersectionObserver(
      ([entry]) => setIsStuck(!entry.isIntersecting && entry.boundingClientRect.top < 0),
      { rootMargin: "24px 0px -100% 0px", threshold: 0 }
    );
    observer.observe(sentinel);
    return () => observer.disconnect();
  }, [isMobile]);

  const className =
    "content-block znai-section-title znai-heading" +
    (style ? " " + style : "") +
    (isStuck ? " is-stuck" : "");

  return id ? (
    <>
      <div ref={sentinelRef} className="znai-section-title-sentinel" aria-hidden="true" />
      {/* @ts-ignore */}
      <h1 className={className} id={id}>
        <span className="znai-section-title-text">
          <HeadingContent title={title} headingContent={headingContent} elementsLibrary={elementsLibrary} />
          <a className="znai-section-title-link" href={"#" + id}>
            <Icon id="link" />
          </a>
        </span>
        {badge && <TextBadge text={badge} useExtraLeftMargin={true} />}
        {isPresentationButtonVisible() && (
          <Icon id="maximize" className="znai-section-title-presentation" onClick={openPresentation} />
        )}
      </h1>
    </>
  ) : (
    <h1 className="empty-section-title" id="implicit-section" />
  );

  function openPresentation() {
    presentationModeListeners.notifyPresentationEnter(id);
  }
}

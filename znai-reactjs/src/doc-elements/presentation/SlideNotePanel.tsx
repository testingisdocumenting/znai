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

import React, { useLayoutEffect, useRef, useState } from 'react';

import { PresentationSlide } from './PresentationSlide';
import PresentationRegistry from './PresentationRegistry';

import './SlideNotePanel.css';

interface Props {
  presentationRegistry: PresentationRegistry;
  pageLocalSlideIdx: number;
  slide: PresentationSlide;
}

export function SlideNotePanel({presentationRegistry, pageLocalSlideIdx, slide}: Props) {
  const [height, setHeight] = useState(0);
  const contentNode = useRef<HTMLDivElement>(null);

  const currentNote = slide.info.slideVisibleNote
  const isVisible = currentNote && currentNote.length > 0;

  useLayoutEffect(() => {
    if (contentNode.current && !height) {
      setHeight(contentNode.current!.offsetHeight);
    }
  }, [slide, currentNote, height])

  if (!hasNotes()) {
    return null;
  }

  const visibility = isVisible ? 'visible' : 'hidden';

  const noteToRender = !height ? findLongestNote() : currentNote;
  const heightToUse = !height ? undefined : height;

  return (
    <div className="znai-slide-note-panel" ref={contentNode} style={{visibility, height: heightToUse}}>
      {noteToRender}
    </div>
  )

  function hasNotes() {
    const componentStartIdx = presentationRegistry.slideComponentStartIdxByIdx(pageLocalSlideIdx)
    for (let idx = 0; idx < slide.numberOfSlides; idx++) {
      const slideVisibleNote = extractSlideInfo(componentStartIdx + idx);
      if (slideVisibleNote && slideVisibleNote.length > 0) {
        return true;
      }
    }

    return false;
  }

  function findLongestNote() {
    const componentStartIdx = presentationRegistry.slideComponentStartIdxByIdx(pageLocalSlideIdx)

    let result = '';
    for (let idx = 0; idx < slide.numberOfSlides; idx++) {
      const slideVisibleNote = extractSlideInfo(componentStartIdx + idx);
      if (slideVisibleNote && slideVisibleNote.length > result.length) {
        result = slideVisibleNote
      }
    }

    return result;
  }

  function extractSlideInfo(slideIdx: number) {
    const {slideVisibleNote} = presentationRegistry.extractCombinedSlideInfo(slideIdx);
    return slideVisibleNote;
  }
}
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

import { SlidesLayout } from './SlidesLayout';

import PresentationRegistry from './PresentationRegistry';

import { PresentationDimension } from './PresentationDimensions';

import './SlidePanel.css';

interface Props {
  presentationRegistry: PresentationRegistry;
  currentSlideIdx: number;
}

const maxScaleRatio = 3;

export function SlidePanel({presentationRegistry, currentSlideIdx}: Props) {
  const areaNode = useRef<HTMLDivElement>(null);
  const [slideArea, setSlideArea] = useState<PresentationDimension | undefined>()

  useLayoutEffect(() => {
    if (areaNode.current) {
      setSlideArea(calcSlideArea(areaNode.current));
    }
  }, [currentSlideIdx])

  return (
    <div ref={areaNode} className="znai-presentation-slide-panel">
      {slideArea && (<SlidesLayout presentationRegistry={presentationRegistry}
                                          currentSlideIdx={currentSlideIdx}
                                          maxScaleRatio={maxScaleRatio}
                                          presentationArea={slideArea}/>)
      }
    </div>
  );
}

function calcSlideArea(node: HTMLElement) {
  if (!node) {
    return undefined
  }

  return {
    width: node.offsetWidth,
    height: node.offsetHeight
  }
}


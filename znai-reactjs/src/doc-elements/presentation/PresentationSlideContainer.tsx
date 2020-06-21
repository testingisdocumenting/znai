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

import React, { useEffect, useRef, useState } from 'react';

import './PresentationSlideContainer.css';

interface Props {
  slideIdx: number;
  maxScaleRatio: number;
  isPadded: boolean;
  isScaled: boolean;
  isCentered: boolean;
  flex: number;
  render(): React.ReactNode;
}

export function PresentationSlideContainer({
                                             slideIdx,
                                             maxScaleRatio,
                                             isPadded,
                                             isScaled,
                                             isCentered,
                                             flex,
                                             render
                                           }: Props) {
  const [scaleRatio, setScaleRatio] = useState(1);
  const [slideAppeared, setSlideAppeared] = useState(false);

  const areaNode = useRef<HTMLDivElement>(null);
  const contentNode = useRef<HTMLDivElement>(null);

  useEffect(() => {
    setScaleRatio(isScaled ?
      calcScale(areaNode.current!, contentNode.current!, isPadded, maxScaleRatio) :
      1.0);

    setSlideAppeared(true);
  }, [isPadded, maxScaleRatio, isScaled, slideIdx])

  const className = "znai-presentation-slide-container " +
    (isCentered ? " centered" : "") +
    (isPadded ? " padded" : "") +
    (slideAppeared ? " znai-presentation-slide-appeared" : "");

  const rendered = render();

  const wrappedChildren = isScaled ? (
    <div ref={contentNode} style={{transform: "scale(" + scaleRatio + ")"}}>
      {rendered}
    </div>
  ) : rendered;

  return (
    <div className={className} ref={areaNode} style={{flex}}>
      {wrappedChildren}
    </div>
  )
}

function calcScale(areaNode: HTMLDivElement,
                   contentNode: HTMLDivElement,
                   isPadded: boolean,
                   maxScaleRatio: number): number {
  // code based padding to avoid margin collapse
  const hPad = isPadded ? 60 : 0;
  const vPad = isPadded ? 30 : 0;

  const widthRatio = (areaNode.offsetWidth - hPad) / contentNode.offsetWidth;
  const heightRatio = (areaNode.offsetHeight - vPad) / contentNode.offsetHeight;

  return Math.min(widthRatio, heightRatio, maxScaleRatio);
}
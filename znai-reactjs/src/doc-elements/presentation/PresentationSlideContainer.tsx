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

import React, { useLayoutEffect, useRef, useState } from "react";

import { PresentationDimension, SlideAreaDimension } from "./PresentationDimensions";

import "./PresentationSlideContainer.css";

interface Props {
  slideIdx: number;
  maxScaleRatio: number;
  isPadded: boolean;
  isScaled: boolean;
  isCentered: boolean;
  presentationArea: PresentationDimension;
  slideArea: SlideAreaDimension;
  render(): React.ReactNode;
}

export function PresentationSlideContainer({
  slideIdx,
  maxScaleRatio,
  isPadded,
  isScaled,
  isCentered,
  presentationArea,
  slideArea,
  render,
}: Props) {
  const [scaleRatio, setScaleRatio] = useState(1);
  const [slideAppeared, setSlideAppeared] = useState(false);

  const contentNode = useRef<HTMLDivElement>(null);

  useLayoutEffect(() => {
    if (!isScaled) {
      setScaleRatio(1.0);
      setSlideAppeared(true);
      return;
    }

    const node = contentNode.current!;

    const tryCalcScale = () => {
      if (!hasContentLoaded(node)) {
        return false;
      }
      setScaleRatio(calcScale(presentationArea, slideArea, node, isPadded, maxScaleRatio));
      setSlideAppeared(true);
      return true;
    };

    if (tryCalcScale()) {
      return;
    }

    const resizeObserver = new ResizeObserver(() => {
      if (tryCalcScale()) {
        resizeObserver.disconnect();
      }
    });
    resizeObserver.observe(node);

    return () => {
      resizeObserver.disconnect();
    };
  }, [isPadded, maxScaleRatio, isScaled, slideIdx, presentationArea, slideArea]);

  const className =
    "znai-presentation-slide-container " +
    (isCentered ? " centered" : "") +
    (isPadded ? " padded" : "") +
    (slideAppeared ? " znai-presentation-slide-appeared" : "");

  const rendered = render();

  const wrappedChildren = isScaled ? (
    <div ref={contentNode} style={{ transform: "scale(" + scaleRatio + ")" }}>
      {rendered}
    </div>
  ) : (
    rendered
  );

  const maxWidth = (presentationArea.width * slideArea.widthPercentage) / 100.0;
  const maxHeight = (presentationArea.height * slideArea.heightPercentage) / 100.0;

  return (
    <div className={className} style={{ width: maxWidth, height: maxHeight }}>
      {wrappedChildren}
    </div>
  );
}

function hasContentLoaded(contentNode: HTMLDivElement): boolean {
  return contentNode.offsetWidth > 0 && contentNode.offsetHeight > 0;
}

function calcScale(
  presentationArea: PresentationDimension,
  slideArea: SlideAreaDimension,
  contentNode: HTMLDivElement,
  isPadded: boolean,
  maxScaleRatio: number
): number {
  // code based padding to avoid margin collapse
  const hPad = isPadded ? 60 : 0;
  const vPad = isPadded ? 30 : 0;

  const maxWidth = calcMaxWidth(presentationArea, slideArea);
  const maxHeight = calcMaxHeight(presentationArea, slideArea);

  const widthRatio = (maxWidth - hPad) / contentNode.offsetWidth;
  const heightRatio = (maxHeight - vPad) / contentNode.offsetHeight;

  return Math.min(widthRatio, heightRatio, maxScaleRatio);
}

function calcMaxWidth(presentationArea: PresentationDimension, slideArea: SlideAreaDimension) {
  return (presentationArea.width * slideArea.widthPercentage) / 100.0;
}

function calcMaxHeight(presentationArea: PresentationDimension, slideArea: SlideAreaDimension) {
  return (presentationArea.height * slideArea.heightPercentage) / 100.0;
}

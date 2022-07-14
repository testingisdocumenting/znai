/*
 * Copyright 2022 znai maintainers
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

import { DocElementContent, WithElementsLibrary } from "../default-elements/DocElement";

import { AnnotatedImageOrderedList } from "./AnnotatedImageOrderedList";

import { TooltipPlacement } from "../../components/Tooltip";

import "./AnnotatedImageWithOrderedList.css";

interface Props extends WithElementsLibrary {
  annotatedImageContent: DocElementContent;
  orderedListContent: DocElementContent;
  isImageFirst: boolean;
}

export function AnnotatedImageWithOrderedList({
  elementsLibrary,
  annotatedImageContent,
  orderedListContent,
  isImageFirst,
}: Props) {
  const [annotationToHighlightIdx, setAnnotationToHighlightIdx] = useState(-1);

  const AnnotatedImage = elementsLibrary.AnnotatedImage;
  const OrderedList = AnnotatedImageOrderedList;

  // @ts-ignore
  const isInvertedColors = annotatedImageContent.shapes.map((shape) => shape.invertedColors);

  const renderedList = (
    <OrderedList
      // @ts-ignore
      content={orderedListContent.content}
      elementsLibrary={elementsLibrary}
      isInvertedColors={isInvertedColors}
      onHover={handleOnListItemHover}
    />
  );

  const renderedImage = (
    <AnnotatedImage
      {...annotatedImageContent}
      elementsLibrary={elementsLibrary}
      // @ts-ignore
      shapesTooltipContent={buildTooltipContent()}
      annotationToHighlightIdx={annotationToHighlightIdx}
    />
  );

  if (isImageFirst) {
    return (
      <>
        {renderedImage}
        {renderedList}
      </>
    );
  }

  return (
    <>
      {renderedList}
      {renderedImage}
    </>
  );

  function buildTooltipContent(): Array<{ placement: TooltipPlacement; content: any }> {
    // @ts-ignore
    const badgesTooltipsContent = orderedListContent.content.map((listItem) => (
      <elementsLibrary.DocElement content={listItem.content} elementsLibrary={elementsLibrary} />
    ));

    // @ts-ignore
    const annotationShapes = annotatedImageContent.shapes || [];

    let badgeIdx = 0;
    return annotationShapes.map((shape: any) => {
      const type = shape.type;
      if (type === "badge") {
        return {
          placement: "top-left",
          content: badgesTooltipsContent[badgeIdx++],
        };
      }

      return undefined;
    });
  }

  function handleOnListItemHover(bulletListIdx: number) {
    // we get a bullet list idx, which doesn't correspond to all the annotations one to one
    // we count badge annotation and once it matches a specific bullet list idx,
    // then highlight a corresponding annotation idx

    // @ts-ignore
    const annotationShapes = annotatedImageContent.shapes || [];
    let annotBadgeIdx = -1;
    for (let annotIdx = 0; annotIdx < annotationShapes.length; annotIdx++) {
      if (annotationShapes[annotIdx].type === "badge") {
        annotBadgeIdx++;
      }

      if (annotBadgeIdx === bulletListIdx) {
        setAnnotationToHighlightIdx(annotIdx);
      }
    }
  }
}

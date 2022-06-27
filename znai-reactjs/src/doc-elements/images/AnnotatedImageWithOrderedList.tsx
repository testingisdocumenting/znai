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
  const tooltipsContent = orderedListContent.content.map((listItem) => (
    <elementsLibrary.DocElement content={listItem.content} elementsLibrary={elementsLibrary} />
  ));

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
      shapesTooltipContent={tooltipsContent}
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

  function handleOnListItemHover(idx: number) {
    setAnnotationToHighlightIdx(idx);
  }
}

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

import React from "react";

import { DocElementContent, DocElementProps, ElementsLibraryMap } from "../default-elements/DocElement";
import "./AnnotatedImageOrderedList.css";

interface Props extends DocElementProps {
  isInvertedColors: boolean[];
}

export function AnnotatedImageOrderedList({ content, elementsLibrary, isInvertedColors }: Props) {
  return (
    <div className="znai-annotated-image-ordered-list content-block">
      {content.map((item, idx) => (
        <ListItem
          key={idx}
          idx={idx}
          content={item.content!}
          elementsLibrary={elementsLibrary}
          isInvertedColors={isInvertedColors[idx]}
        />
      ))}
    </div>
  );
}

interface ListItemProps {
  elementsLibrary: ElementsLibraryMap;
  idx: number;
  content: DocElementContent;
  isInvertedColors: boolean;
}

function ListItem({ elementsLibrary, idx, content, isInvertedColors }: ListItemProps) {
  const numberClassName = "znai-annotated-image-ordered-list-number" + (isInvertedColors ? " inverted" : "");
  return (
    <>
      <div className={numberClassName}>{idx + 1}</div>
      <div className="znai-annotated-image-ordered-list-content list-item">
        <elementsLibrary.DocElement content={content} elementsLibrary={elementsLibrary} />
      </div>
    </>
  );
}

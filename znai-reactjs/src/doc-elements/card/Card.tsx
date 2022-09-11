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

import { DocElementContent, WithElementsLibrary } from "../default-elements/DocElement";

import "./Card.css";

interface Props extends WithElementsLibrary {
  title?: string;
  imageSrc?: string;
  imageHeight?: number;
  background?: string;
  bodyContent: DocElementContent;
}

export function Card({ title, elementsLibrary, imageSrc, imageHeight, background, bodyContent }: Props) {
  const cardClassName = "znai-card " + (isLastElementContentContainer() ? " no-bottom-padding" : "");
  return (
    <div className="znai-card-wrapper content-block">
      <div className={cardClassName}>
        {imageSrc && (
          <div className="znai-card-image" style={{ background }}>
            <img src={imageSrc} alt="card visual" style={{ height: imageHeight }} />
          </div>
        )}
        <div className="znai-card-content">
          {title && <div className="znai-card-title">{title}</div>}
          <div className="znai-card-body">
            <elementsLibrary.DocElement content={bodyContent} elementsLibrary={elementsLibrary} />
          </div>
        </div>
      </div>
    </div>
  );

  function isLastElementContentContainer() {
    if (bodyContent.length === 0) {
      return false;
    }

    const type = bodyContent[bodyContent.length - 1].type;
    return type === "Snippet" || type === "ApiParameters";
  }
}

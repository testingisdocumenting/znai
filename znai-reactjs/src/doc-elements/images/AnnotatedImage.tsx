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

import React, { CSSProperties } from "react";

import { imageAdditionalPreviewUrlParam } from "./imagePreviewAdditionalUrlParam";

import { cssVarPixelValue } from "../../utils/cssVars";

import Annotations from "./annotations/Annotations";
import { zoom } from "../zoom/Zoom";

import "./AnnotatedImage.css";

export interface ImageProps {
  imageSrc: string;
  shapes: object[];
  width: number;
  height: number;
  align?: string;
  title?: string;
  fit?: boolean;
  scale?: number;
  border?: boolean;
  timestamp?: number;
}

export function AnnotatedImage(props: ImageProps) {
  const { imageSrc, width, height, title, shapes, align, fit, scale, border, timestamp } = props;

  const scaleToUse = calcScale();

  const scaledWidth = width * scaleToUse;
  const scaledHeight = height * scaleToUse;

  const borderSize = border ? 1 : 0;
  const borderSizeAdjustment = borderSize * 2;

  const parentStyle: CSSProperties = {
    position: "relative",
    width: scaledWidth + borderSizeAdjustment + "px",
    height: scaledHeight + borderSizeAdjustment + "px",
  };

  const imageWidth = scaledWidth + "px";
  const imageHeight = scaledHeight + "px";

  const childContainerStyle: CSSProperties = {
    position: "absolute",
    width: imageWidth,
    height: imageHeight,
  };

  const annotations = new Annotations(shapes);
  const isScaledDown = scaleToUse < 1.0;

  const containerClassName =
    "znai-annotated-image-container" +
    (align ? " content-block " + align : "") +
    (fit ? " znai-image-fit" : "") +
    (isScaledDown ? " znai-image-scaled-down" : "");

  const imageClassName = "znai-annotated-image" + (border ? " border" : "");

  return (
    <div className={containerClassName}>
      <div>
        {renderTitle()}
        <div style={parentStyle} className={imageClassName} onClick={zoomImage}>
          <div style={childContainerStyle}>
            <img
              alt="annotated"
              src={imageSrc + imageAdditionalPreviewUrlParam(timestamp)}
              width={imageWidth}
              height={imageHeight}
            />
          </div>
          <div style={childContainerStyle}>
            <svg width={imageWidth} height={imageHeight}>
              {annotations.staticAnnotationsToRender(scaleToUse)}
            </svg>
          </div>
        </div>
      </div>
    </div>
  );

  function calcScale() {
    if (fit) {
      const singleColumnWidth = cssVarPixelValue("znai-single-column-full-width");
      return singleColumnWidth / width;
    }

    return scale || 1.0;
  }

  function renderTitle() {
    if (!title) {
      return null;
    }

    return <div className="znai-image-title">{title}</div>;
  }

  function zoomImage() {
    if (!isScaledDown) {
      return;
    }

    const propsNoScaleOrPosition = { ...props };
    delete propsNoScaleOrPosition.fit;
    delete propsNoScaleOrPosition.scale;
    delete propsNoScaleOrPosition.align;

    zoom.zoom(<AnnotatedImage {...propsNoScaleOrPosition} />);
  }
}

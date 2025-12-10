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

import { CSSProperties} from "react";
import React, { useState } from "react";

import { imageAdditionalPreviewUrlParam } from "./imagePreviewAdditionalUrlParam";

import { cssVarPixelValue } from "../../utils/cssVars";

import Annotations from "./annotations/Annotations";
import { zoom } from "../zoom/Zoom";

import { isPreviewEnabled } from "../../structure/docMeta";

import { TooltipPlacement } from "../../components/Tooltip";

import { WithElementsLibrary } from "../default-elements/DocElement";

import { ContainerTitleCommonProps} from "../container/ContainerTitle";
import { ContainerTitle, useIsUserDrivenCollapsed } from "../container/ContainerTitle";

import { useIsMobile } from "../../theme/ViewPortContext";
import { ContainerCommonProps } from "../container/Container";
import { Container } from "../container/Container";

import "./AnnotatedImage.css";

export interface AnnotatedImageProps extends WithElementsLibrary, ContainerTitleCommonProps, ContainerCommonProps {
  imageSrc: string;
  shapes: object[];
  width: number;
  height: number;
  alt?: string;
  align?: string;
  fit?: boolean;
  scale?: number;
  border?: boolean;
  mobileOnly?: boolean;
  desktopOnly?: boolean;
  timestamp?: number;
  inlined?: boolean;
  shapesTooltipContent?: Array<{ placement: TooltipPlacement; content: any }>;
  annotationToHighlightIdx?: number;
}

export function AnnotatedImage(props: AnnotatedImageProps) {
  const {
    imageSrc,
    title,
    anchorId,
    collapsed,
    noGap,
    next,
    prev,
    width,
    height,
    alt,
    shapes,
    align,
    fit,
    scale,
    border,
    timestamp,
    inlined,
    mobileOnly,
    desktopOnly,
    shapesTooltipContent,
    annotationToHighlightIdx,
    elementsLibrary,
  } = props;

  const isMobile = useIsMobile();
  const { userDrivenCollapsed, collapseToggle } = useIsUserDrivenCollapsed(collapsed);

  const scaleToUse = calcScale();

  const [mousePosition, setMousePosition] = useState<{ x: number; y: number }>({ x: -1, y: -1 });

  if (isMobile && desktopOnly) {
    return null;
  }

  if (!isMobile && mobileOnly) {
    return null;
  }

  const sizeSpecified = width > 0;

  const scaledWidth = width * scaleToUse;
  const scaledHeight = height * scaleToUse;

  const borderSize = border ? 1 : 0;
  const borderSizeAdjustment = borderSize * 2;

  const parentStyle: CSSProperties = {
    position: "relative",
    width: sizeSpecified ? scaledWidth + borderSizeAdjustment + "px" : "auto",
    height: calcContainerHeight(),
  };

  const imageWidth = sizeSpecified ? scaledWidth + "px" : "auto";
  const imageHeight = sizeSpecified ? scaledHeight + "px" : "auto";

  const childContainerStyle: CSSProperties = {
    position: "absolute",
    width: imageWidth,
    height: imageHeight,
  };

  const annotations = new Annotations(shapes);
  const isScaledDown = scaleToUse < 1.0;
  const isCentered = !align || align === "center";

  const containerClassName =
    "znai-annotated-image-container" +
    (isCentered ? " center" : "") +
    (align ? " content-block " + align : "") +
    (title ? " with-title" : "") +
    (inlined ? "  inlined" : "");

  const imageClassName =
    "znai-annotated-image" +
    (border ? " border" : "") +
    (fit ? " znai-image-fit" : "") +
    (isScaledDown ? " znai-image-scaled-down" : "");

  return (
    <Container className={containerClassName} noGap={noGap} next={next} prev={prev}>
      {renderTitle()}
      {renderPaddedImageIfRequired()}
      {renderCoordinates()}
    </Container>
  );

  function renderPaddedImageIfRequired() {
    const renderedImage = renderImage();
    return isCentered ? (
      <>
        <div />
        {renderedImage}
        <div />
      </>
    ) : (
      renderedImage
    );
  }

  function renderImage() {
    const image = (
      <img
        alt={alt}
        src={imageSrc + imageAdditionalPreviewUrlParam(timestamp)}
        width={imageWidth}
        height={imageHeight}
      />
    );

    // comes from a standard markdown syntax
    if (!sizeSpecified) {
      return image;
    }

    return (
      <div style={parentStyle} className={imageClassName} onClick={zoomImage}>
        <div style={childContainerStyle}>{image}</div>
        <div style={childContainerStyle}>
          <svg width={imageWidth} height={imageHeight} onMouseMove={handleMouseMove} onMouseLeave={handleMouseLeave}>
            {annotations.staticAnnotationsToRender(
              updatedShapesTooltipBasedOnText(),
              annotationToHighlightIdx,
              scaleToUse
            )}
          </svg>
        </div>
      </div>
    );
  }

  function calcContainerHeight() {
    if (userDrivenCollapsed) {
      return 0;
    }

    return sizeSpecified ? scaledHeight + borderSizeAdjustment + "px" : "auto";
  }

  function updatedShapesTooltipBasedOnText() {
    // may have empty elements when it comes from AnnotatedImageWithOrderedList
    // we populate
    return shapes.map((shape: any, idx: number) => {
      const tooltip: { placement: TooltipPlacement; content: any } | undefined = shapesTooltipContent
        ? shapesTooltipContent[idx]
        : undefined;

      if (tooltip) {
        return tooltip;
      }

      return buildTooltipForShape(shape);
    });

    function buildTooltipForShape(shape: any) {
      if (shape.type === "badge" && shape.tooltip) {
        return {
          placement: "center",
          content: shape.tooltip, // todo: doc elements markdown support from CSV/json
        };
      } else if (shape.type !== "badge" && shape.tooltip) {
        return {
          placement: placementForShape(),
          content: <elementsLibrary.DocElement content={shape.tooltip} elementsLibrary={elementsLibrary} />,
        };
      }

      return undefined;

      function placementForShape() {
        if (shape.type !== "arrow") {
          return "center";
        }

        if (shape.beginX < shape.endX) {
          return shape.beginY < shape.endY ? "bottom-right" : "top-right";
        } else {
          return shape.beginY < shape.endY ? "bottom-left" : "top-left";
        }
      }
    }
  }

  function calcScale() {
    if (!fit) {
      return scale || 1.0;
    }

    const singleColumnWidth = isMobile ? window.innerWidth : cssVarPixelValue("znai-single-column-full-width");
    return singleColumnWidth / width;
  }

  function renderTitle() {
    const renderedTitle = title ? (
      <ContainerTitle
        title={title}
        additionalTitleClassNames="znai-image-title"
        anchorId={anchorId}
        collapsed={userDrivenCollapsed}
        onCollapseToggle={collapseToggle}
      />
    ) : (
      <div />
    );

    return isCentered ? (
      <>
        <div />
        {renderedTitle}
        <div />
      </>
    ) : (
      renderedTitle
    );
  }

  function handleMouseMove(e: React.MouseEvent<SVGElement>) {
    if (!isPreviewEnabled()) {
      return;
    }

    const bounds = e.currentTarget.getBoundingClientRect();
    const x = e.clientX - bounds.left;
    const y = e.clientY - bounds.top;

    setMousePosition({ x, y });
  }

  function handleMouseLeave() {
    setMousePosition({ x: -1, y: -1 });
  }

  function renderCoordinates() {
    if (!isPreviewEnabled() || mousePosition.x < 0 || mousePosition.y < 0) {
      return null;
    }

    const k = 1.0 / scaleToUse;

    const effectiveX = (k * mousePosition.x) | 0;
    const effectiveY = (k * mousePosition.y) | 0;
    const renderedCoord = (
      <div className="znai-image-preview-coordinates">
        preview coordinates, x: {effectiveX}; y: {effectiveY}
      </div>
    );

    return isCentered ? (
      <>
        <div />
        {renderedCoord}
        <div />
      </>
    ) : (
      renderedCoord
    );
  }

  function zoomImage() {
    if (!isScaledDown) {
      return;
    }

    const propsRemovedStyles = { ...props };
    delete propsRemovedStyles.fit;
    delete propsRemovedStyles.scale;
    delete propsRemovedStyles.align;
    delete propsRemovedStyles.collapsed;

    zoom.zoom(<AnnotatedImage {...propsRemovedStyles} />);
  }
}

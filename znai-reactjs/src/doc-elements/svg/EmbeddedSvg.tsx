/*
 * Copyright 2025 znai maintainers
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

import React, { useEffect, useRef } from "react";

import { svgAttributesToProps } from "./svgUtils";
import { DocElementMeta, isAllAtOnce } from "../meta/meta";
import { calcFitScale } from "../images/fitUtils";
import { useIsMobile } from "../../theme/ViewPortContext";

import "./EmbeddedSvg.css";

interface SvgElementProps {
  id?: string;
  [key: string]: unknown;
}

interface EmbeddedSvgProps {
  svg: string;
  idsToReveal?: string[];
  isPresentation?: boolean;
  slideIdx?: number;
  meta?: DocElementMeta;
  actualSize?: boolean;
  scale?: number;
  fit?: boolean;
}

export function EmbeddedSvg({
  svg,
  idsToReveal,
  isPresentation,
  slideIdx,
  meta,
  actualSize,
  scale,
  fit,
}: EmbeddedSvgProps) {
  const svgRef = useRef<SVGSVGElement>(null);
  const originalSizeRef = useRef<{
    viewBox: string | null;
    height: string | null;
    width: string | null;
  }>({ viewBox: null, height: null, width: null });

  const isMobile = useIsMobile();

  useEffect(() => {
    if (svgRef.current) {
      saveOriginalSize();
      changeSizeWhenPropIsChanged();
    }
  }, []);

  useEffect(() => {
    changeSizeWhenPropIsChanged();
  }, [actualSize, fit, scale, isMobile, isPresentation]);

  function saveOriginalSize() {
    if (!svgRef.current) return;
    originalSizeRef.current = {
      viewBox: svgRef.current.getAttribute("viewBox"),
      height: svgRef.current.getAttribute("height"),
      width: svgRef.current.getAttribute("width"),
    };
  }

  function changeSizeWhenPropIsChanged() {
    if (isPresentation) {
      forceActualSizeSvg();
    } else if (actualSize) {
      forceActualSizeSvg();
    } else if (fit) {
      restoreOriginalSize();
      applyFitScale();
    } else {
      restoreOriginalSize();
    }
  }

  function getSvgWidth(): number | null {
    if (!svgRef.current) return null;

    const widthAttr = svgRef.current.getAttribute("width");
    if (widthAttr) {
      const width = parseFloat(widthAttr);
      if (!isNaN(width)) return width;
    }

    const viewBox = svgRef.current.getAttribute("viewBox");
    if (viewBox) {
      const parts = viewBox.split(/\s+|,/);
      if (parts.length >= 4) {
        const width = parseFloat(parts[2]);
        if (!isNaN(width)) return width;
      }
    }

    return null;
  }

  function applyFitScale() {
    const svgWidth = getSvgWidth();
    if (!svgWidth || !svgRef.current) return;

    const fitScale = calcFitScale(fit, svgWidth, scale, isMobile);

    if (fitScale < 1) {
      svgRef.current.setAttribute("width", svgWidth * fitScale + "px");
      const originalHeight = originalSizeRef.current.height;
      if (originalHeight) {
        const heightValue = parseFloat(originalHeight);
        if (!isNaN(heightValue)) {
          svgRef.current.setAttribute("height", heightValue * fitScale + "px");
        }
      }
    }
  }

  function forceActualSizeSvg() {
    if (!svgRef.current) return;

    const scaleToUse = scale ?? 1;
    const bbox = svgRef.current.getBBox();
    svgRef.current.setAttribute("width", bbox.width * scaleToUse + "px");
    svgRef.current.removeAttribute("height");
    svgRef.current.setAttribute("viewBox", `${bbox.x} ${bbox.y} ${bbox.width} ${bbox.height}`);
  }

  function restoreOriginalSize() {
    if (!svgRef.current) return;

    const restore = (attrKey: string, value: string | null) => {
      if (value !== null) {
        svgRef.current!.setAttribute(attrKey, value);
      } else {
        svgRef.current!.removeAttribute(attrKey);
      }
    };

    restore("width", originalSizeRef.current.width);
    restore("height", originalSizeRef.current.height);
    restore("viewBox", originalSizeRef.current.viewBox);
  }

  function childrenReactElementsFromDomNode(domNode: Element | null): React.ReactNode[] {
    const children: React.ReactNode[] = [];

    if (!domNode) {
      return children;
    }

    const lastPartIdx = isAllAtOnce(meta ?? {}) ? idsToReveal?.length ?? 0 : slideIdx ?? 0;
    const idsForSlide = isPresentation && idsToReveal ? idsToReveal.slice(0, lastPartIdx + 1) : idsToReveal;

    const childNodes = domNode.childNodes || [];
    for (let i = 0, len = childNodes.length; i < len; i++) {
      const child = childNodes[i] as Element;

      const childProps = svgAttributesToProps(child.attributes) as SvgElementProps;
      const key = childProps.id ? childProps.id : i;

      if (idsToReveal && childProps.id && idsForSlide?.indexOf(childProps.id) === -1) {
        continue;
      }

      const reactChildElement =
        child.nodeName === "g" ? (
          <g key={key} {...childProps} dangerouslySetInnerHTML={{ __html: child.innerHTML }} />
        ) : (
          <g key={key} dangerouslySetInnerHTML={{ __html: child.outerHTML }} />
        );

      children.push(reactChildElement);
    }

    return children;
  }

  // server side rendering
  if (typeof DOMParser === "undefined") {
    return null;
  }

  const parser = new DOMParser();
  const dom = parser.parseFromString(svg, "application/xml");

  const svgProps = svgAttributesToProps(dom.documentElement.attributes);
  const children = childrenReactElementsFromDomNode(dom.documentElement);

  return (
    <div className="embedded-svg content-block">
      <svg {...svgProps} ref={svgRef}>
        {children}
      </svg>
    </div>
  );
}

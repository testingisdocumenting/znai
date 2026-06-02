/*
 * Copyright 2021 znai maintainers
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

import React, { useEffect, useRef, useState } from "react";

import { isLocalUrl } from "../../structure/links";
import { documentationNavigation } from "../../structure/DocumentationNavigation";
import { zoom } from "../zoom/Zoom";
import { useMermaidSvg, MermaidIconPack } from "./useMermaidSvg";
import { isMermaidSvgShrunk } from "./mermaidSvgSize";
import { MermaidZoomed } from "./MermaidZoomed";

import "./Mermaid.css";

interface Props {
  mermaid: string;
  wide?: boolean;
  iconpacks?: MermaidIconPack[];
}

export default function Mermaid(props: Props) {
  const html = useMermaidSvg(props.mermaid, props.iconpacks);
  const containerRef = useRef<HTMLDivElement>(null);

  // a large diagram is shrunk to fit the page width and becomes hard to read; in that case we let
  // the reader open it in a full screen zoom & pan overlay. Re-checked on resize as the available
  // width (and thus whether the diagram fits) changes with the window/layout.
  const [isZoomable, setIsZoomable] = useState(false);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) {
      return;
    }

    function update() {
      setIsZoomable(isMermaidSvgShrunk(containerRef.current));
    }

    update();

    const observer = new ResizeObserver(update);
    observer.observe(container);
    return () => observer.disconnect();
  }, [html]);

  useEffect(() => {
    const container = containerRef.current;
    if (!container) {
      return;
    }

    function handleClick(e: MouseEvent) {
      const target = e.target as HTMLElement;
      const anchor = target.closest("a");
      if (!anchor) {
        return;
      }

      const url = anchor.getAttribute("href") || anchor.getAttributeNS("http://www.w3.org/1999/xlink", "href");
      if (url && isLocalUrl(url)) {
        e.preventDefault();
        documentationNavigation.navigateToUrl(url);
      }
    }

    container.addEventListener("click", handleClick);
    return () => container.removeEventListener("click", handleClick);
  }, [html, isZoomable]);

  function openZoom() {
    zoom.zoom(<MermaidZoomed mermaid={props.mermaid} iconpacks={props.iconpacks} />);
  }

  const className =
    "znai-mermaid " + (props.wide ? "wide" : "content-block") + (isZoomable ? " znai-mermaid-zoomable" : "");

  return (
    <div
      ref={containerRef}
      className={className}
      onClick={isZoomable ? openZoom : undefined}
      dangerouslySetInnerHTML={{ __html: html }}
    ></div>
  );
}

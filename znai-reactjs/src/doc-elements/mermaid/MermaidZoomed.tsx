/*
 * Copyright 2026 znai maintainers
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

import React, { useEffect, useLayoutEffect, useRef, useState } from "react";

import { Icon } from "../icons/Icon";
import { isLocalUrl } from "../../structure/links";
import { documentationNavigation } from "../../structure/DocumentationNavigation";
import { zoom } from "../zoom/Zoom";
import { useMermaidSvg, MermaidIconPack } from "./useMermaidSvg";
import { mermaidSvgNaturalSize } from "./mermaidSvgSize";

import "./MermaidZoomed.css";

interface Props {
  mermaid: string;
  iconpacks?: MermaidIconPack[];
}

interface View {
  scale: number;
  x: number;
  y: number;
}

// the overlay opens at the diagram's natural (100%) size; from there the user can zoom in for
// detail up to MAX_SCALE, or zoom out down to the size at which the whole diagram is visible
const MAX_SCALE = 4;
const WHEEL_ZOOM_SENSITIVITY = 0.0015;
const BUTTON_ZOOM_FACTOR = 1.3;
// drag distance (px) past which the gesture is a pan, not a click on a node link
const CLICK_MOVE_THRESHOLD = 4;

/**
 * Full screen, interactive view of a mermaid diagram. Rendered inside the shared zoom overlay
 * (Escape / backdrop click close it). Wheel zooms towards the cursor, drag pans, and the toolbar
 * exposes zoom in/out, fit-to-screen and close.
 */
export function MermaidZoomed(props: Props) {
  const html = useMermaidSvg(props.mermaid, props.iconpacks);

  const surfaceRef = useRef<HTMLDivElement>(null);
  const svgWrapRef = useRef<HTMLDivElement>(null);

  const naturalRef = useRef({ width: 0, height: 0 });
  const fitScaleRef = useRef(1);
  // the very first layout fits the diagram to the screen; later re-layouts must not throw away
  // the zoom the user has already set
  const initializedRef = useRef(false);

  const [view, setView] = useState<View>({ scale: 1, x: 0, y: 0 });
  const [ready, setReady] = useState(false);
  const [dragging, setDragging] = useState(false);

  // current view mirrored into a ref for use at the start of a drag
  const viewRef = useRef(view);
  viewRef.current = view;

  const dragRef = useRef<{ pointerX: number; pointerY: number; viewX: number; viewY: number; scale: number; moved: boolean } | null>(null);
  // a pan ends with a click event we don't want to treat as a link activation
  const suppressClickRef = useRef(false);

  function clamp(candidate: View): View {
    const surface = surfaceRef.current;
    const { width: natW, height: natH } = naturalRef.current;
    if (!surface || natW === 0 || natH === 0) {
      return candidate;
    }

    const surfaceW = surface.clientWidth;
    const surfaceH = surface.clientHeight;

    // zoom out is allowed down to the whole-diagram-visible scale (or natural size, whichever is
    // smaller, so a diagram that already fits is never shrunk); zoom in goes up to MAX_SCALE
    const minScale = Math.min(fitScaleRef.current, 1);
    const scale = Math.min(Math.max(candidate.scale, minScale), MAX_SCALE);
    const contentW = natW * scale;
    const contentH = natH * scale;

    const x = contentW <= surfaceW ? (surfaceW - contentW) / 2 : Math.min(0, Math.max(surfaceW - contentW, candidate.x));
    const y = contentH <= surfaceH ? (surfaceH - contentH) / 2 : Math.min(0, Math.max(surfaceH - contentH, candidate.y));

    return { scale, x, y };
  }

  // zoom keeping the point under (anchorX, anchorY) - relative to the surface - fixed on screen
  function zoomAround(anchorX: number, anchorY: number, factor: number) {
    setView((prev) => {
      const targetScale = prev.scale * factor;
      const worldX = (anchorX - prev.x) / prev.scale;
      const worldY = (anchorY - prev.y) / prev.scale;
      return clamp({ scale: targetScale, x: anchorX - worldX * targetScale, y: anchorY - worldY * targetScale });
    });
  }

  function zoomByButton(factor: number) {
    const surface = surfaceRef.current;
    if (!surface) {
      return;
    }
    zoomAround(surface.clientWidth / 2, surface.clientHeight / 2, factor);
  }

  // fit the whole diagram into the view
  function fitToScreen() {
    setView(clamp({ scale: fitScaleRef.current, x: 0, y: 0 }));
  }

  // measure the diagram and fit it to the available space. The svg can re-render after the user
  // has already zoomed (a late mermaid render, a theme change); on those re-layouts keep the
  // current zoom and only re-bound it, instead of snapping back to the fit scale.
  useLayoutEffect(() => {
    const surface = surfaceRef.current;
    const svgWrap = svgWrapRef.current;
    if (!surface || !svgWrap) {
      return;
    }

    const svg = svgWrap.querySelector("svg");
    if (!svg) {
      return;
    }

    const natural = mermaidSvgNaturalSize(svg);
    if (natural.width === 0 || natural.height === 0) {
      return;
    }

    // render the svg at its natural size so our transform is relative to it
    svg.style.maxWidth = "none";
    svg.style.width = natural.width + "px";
    svg.style.height = natural.height + "px";
    naturalRef.current = natural;

    // the scale at which the whole diagram is visible (the zoom-out limit / fit button target)
    fitScaleRef.current = Math.min(surface.clientWidth / natural.width, surface.clientHeight / natural.height);

    if (initializedRef.current) {
      setView((prev) => clamp(prev));
    } else {
      initializedRef.current = true;
      // open at the diagram's natural (100%) size, centered
      setView(
        clamp({ scale: 1, x: (surface.clientWidth - natural.width) / 2, y: (surface.clientHeight - natural.height) / 2 })
      );
      setReady(true);
    }
  }, [html]);

  // wheel zoom needs a non-passive listener so we can prevent the page from scrolling
  useEffect(() => {
    const surface = surfaceRef.current;
    if (!surface) {
      return;
    }

    function onWheel(e: WheelEvent) {
      e.preventDefault();
      const rect = surface!.getBoundingClientRect();
      const factor = Math.exp(-e.deltaY * WHEEL_ZOOM_SENSITIVITY);
      zoomAround(e.clientX - rect.left, e.clientY - rect.top, factor);
    }

    surface.addEventListener("wheel", onWheel, { passive: false });
    return () => surface.removeEventListener("wheel", onWheel);
  }, []);

  // navigate internal links inside the diagram (a pan gesture is not treated as a click)
  useEffect(() => {
    const svgWrap = svgWrapRef.current;
    if (!svgWrap) {
      return;
    }

    function handleClick(e: MouseEvent) {
      if (suppressClickRef.current) {
        suppressClickRef.current = false;
        return;
      }

      const anchor = (e.target as HTMLElement).closest("a");
      if (!anchor) {
        return;
      }

      const url = anchor.getAttribute("href") || anchor.getAttributeNS("http://www.w3.org/1999/xlink", "href");
      if (url && isLocalUrl(url)) {
        e.preventDefault();
        zoom.clearZoom();
        documentationNavigation.navigateToUrl(url);
      }
    }

    svgWrap.addEventListener("click", handleClick);
    return () => svgWrap.removeEventListener("click", handleClick);
  }, [html]);

  function onPointerDown(e: React.PointerEvent) {
    if (e.button !== 0) {
      return;
    }
    dragRef.current = {
      pointerX: e.clientX,
      pointerY: e.clientY,
      viewX: viewRef.current.x,
      viewY: viewRef.current.y,
      scale: viewRef.current.scale,
      moved: false,
    };
  }

  function onPointerMove(e: React.PointerEvent) {
    const drag = dragRef.current;
    if (!drag) {
      return;
    }

    const dx = e.clientX - drag.pointerX;
    const dy = e.clientY - drag.pointerY;

    // capture the pointer only once this becomes an actual pan - capturing on press would
    // retarget the click to the surface and break clicks on node links
    if (!drag.moved) {
      if (Math.abs(dx) <= CLICK_MOVE_THRESHOLD && Math.abs(dy) <= CLICK_MOVE_THRESHOLD) {
        return;
      }
      drag.moved = true;
      surfaceRef.current?.setPointerCapture(e.pointerId);
      setDragging(true);
    }

    setView(clamp({ scale: drag.scale, x: drag.viewX + dx, y: drag.viewY + dy }));
  }

  function onPointerUp(e: React.PointerEvent) {
    const drag = dragRef.current;
    if (!drag) {
      return;
    }
    suppressClickRef.current = drag.moved;
    if (drag.moved) {
      surfaceRef.current?.releasePointerCapture(e.pointerId);
      setDragging(false);
    }
    dragRef.current = null;
  }

  // a click on the diagram (drag, button) should not bubble to the overlay backdrop and close it
  function stopPropagation(e: React.MouseEvent) {
    e.stopPropagation();
  }

  return (
    <div className="znai-mermaid zoomed" onClick={stopPropagation}>
      <div className="znai-mermaid-zoomed-toolbar">
        <span className="znai-mermaid-zoomed-percent">{ready ? `${Math.round(view.scale * 100)}%` : ""}</span>
        <Icon id="zoom-out" className="znai-mermaid-zoomed-button" onClick={() => zoomByButton(1 / BUTTON_ZOOM_FACTOR)} />
        <Icon id="zoom-in" className="znai-mermaid-zoomed-button" onClick={() => zoomByButton(BUTTON_ZOOM_FACTOR)} />
        <Icon id="maximize" className="znai-mermaid-zoomed-button" onClick={fitToScreen} />
        <Icon id="x" className="znai-mermaid-zoomed-button znai-mermaid-zoomed-close" onClick={() => zoom.clearZoom()} />
      </div>

      <div
        ref={surfaceRef}
        className="znai-mermaid-zoomed-surface"
        style={{ cursor: dragging ? "grabbing" : "grab" }}
        onPointerDown={onPointerDown}
        onPointerMove={onPointerMove}
        onPointerUp={onPointerUp}
        onPointerCancel={onPointerUp}
      >
        <div
          className="znai-mermaid-zoomed-content"
          style={{
            transform: `translate(${view.x}px, ${view.y}px) scale(${view.scale})`,
            transformOrigin: "0 0",
            visibility: ready ? "visible" : "hidden",
          }}
        >
          <div ref={svgWrapRef} className="znai-mermaid-zoomed-svg" dangerouslySetInnerHTML={{ __html: html }} />
        </div>
      </div>
    </div>
  );
}

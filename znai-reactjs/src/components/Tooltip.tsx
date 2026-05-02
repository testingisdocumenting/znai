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

import { CSSProperties } from "react";
import React, { useEffect, useLayoutEffect, useRef, useState } from "react";

import { useIsMobile } from "../theme/ViewPortContext";

import "./Tooltip.css";

export type TooltipPlacement =
  | "top-left"
  | "top-right"
  | "bottom-left"
  | "bottom-right"
  | "center"
  | "parent-content-block";

interface TooltipProps {
  content: any;
  children: React.ReactNode;
  contentClassName?: string;
  placement?: TooltipPlacement;
}

interface TooltipPayload {
  clientRect: DOMRect;
  placement: TooltipPlacement;
  className?: string;
  tooltipTrigger: Element;
  content: any;
}

interface TooltipListener {
  display(payload: TooltipPayload): void;
  clear(): void;
  setIsOutsideTooltipTrigger(isOutside: boolean): void;
}

const HIDE_DELAY_MS = 150;
const VIEWPORT_MARGIN = 8;
const TRIGGER_GAP = 4;

export class TooltipEngine {
  listeners: TooltipListener[] = [];

  add(listener: TooltipListener) {
    this.listeners.push(listener);
  }

  remove(listener: TooltipListener) {
    this.listeners = this.listeners.filter((l) => l !== listener);
  }

  display(payload: TooltipPayload) {
    this.listeners.forEach((l) => l.display(payload));
  }

  setIsOutsideTrigger(isOutside: boolean) {
    this.listeners.forEach((l) => l.setIsOutsideTooltipTrigger(isOutside));
  }
}

const tooltipEngine = new TooltipEngine();

const HIDDEN_STYLE: CSSProperties = { visibility: "hidden", top: 0, left: 0 };

export function TooltipRenderer() {
  const tooltipRef = useRef<HTMLDivElement>(null);
  const isInsideContentRef = useRef(false);
  const isOutsideTriggerRef = useRef(false);
  const hideTimerRef = useRef<number | null>(null);
  const isMobile = useIsMobile();
  const [tooltipPayload, setTooltipPayload] = useState<TooltipPayload | null>(null);
  const [positionStyle, setPositionStyle] = useState<CSSProperties>(HIDDEN_STYLE);

  useEffect(() => {
    const listener: TooltipListener = {
      display(payload: TooltipPayload) {
        cancelHideTimer();
        setPositionStyle(HIDDEN_STYLE);
        setTooltipPayload(payload);
      },

      setIsOutsideTooltipTrigger(isOutside: boolean) {
        isOutsideTriggerRef.current = isOutside;
        if (isOutside && !isInsideContentRef.current) {
          scheduleHide();
        } else {
          cancelHideTimer();
        }
      },

      clear() {
        cancelHideTimer();
        setTooltipPayload(null);
      },
    };

    tooltipEngine.add(listener);
    return () => {
      tooltipEngine.remove(listener);
      cancelHideTimer();
    };
  }, []);

  // measure rendered tooltip and compute final position
  useLayoutEffect(() => {
    if (!tooltipPayload || !tooltipRef.current) {
      return;
    }
    const tooltipRect = tooltipRef.current.getBoundingClientRect();
    setPositionStyle(computePosition(tooltipPayload, tooltipRect));
  }, [tooltipPayload]);

  // close on scroll/resize while open. capture: true catches scroll on inner panels
  // (desktop's .znai-main-panel) as well as window/document scroll (mobile).
  useEffect(() => {
    if (!tooltipPayload) {
      return;
    }
    const close = () => {
      cancelHideTimer();
      setTooltipPayload(null);
    };
    window.addEventListener("scroll", close, { capture: true, passive: true });
    window.addEventListener("resize", close, { passive: true });
    return () => {
      window.removeEventListener("scroll", close, { capture: true });
      window.removeEventListener("resize", close);
    };
  }, [tooltipPayload]);

  if (!tooltipPayload) {
    return null;
  }

  // outer div owns positioning (fixed). inner div carries the caller-supplied
  // contentClassName; keeping it inside avoids leaking caller styles like
  // `position: relative` (which would clobber `position: fixed` on the outer).
  const contentClassName = tooltipPayload.className || "znai-tooltip-default-style";
  const className = `znai-tooltip ${tooltipPayload.placement}` + (isMobile ? " mobile" : "");
  return (
    <div
      className={className}
      style={positionStyle}
      ref={tooltipRef}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      <div className={contentClassName}>{tooltipPayload.content}</div>
    </div>
  );

  function handleMouseEnter() {
    isInsideContentRef.current = true;
    cancelHideTimer();
  }

  function handleMouseLeave() {
    isInsideContentRef.current = false;
    if (isOutsideTriggerRef.current) {
      scheduleHide();
    }
  }

  function cancelHideTimer() {
    if (hideTimerRef.current !== null) {
      window.clearTimeout(hideTimerRef.current);
      hideTimerRef.current = null;
    }
  }

  function scheduleHide() {
    cancelHideTimer();
    hideTimerRef.current = window.setTimeout(() => {
      hideTimerRef.current = null;
      if (isOutsideTriggerRef.current && !isInsideContentRef.current) {
        setTooltipPayload(null);
      }
    }, HIDE_DELAY_MS);
  }
}

function computePosition(payload: TooltipPayload, tooltipRect: DOMRect): CSSProperties {
  const { clientRect, placement, tooltipTrigger } = payload;
  const w = tooltipRect.width;
  const h = tooltipRect.height;
  const vw = window.innerWidth;
  const vh = window.innerHeight;

  // vertical: place on the requested side. for top-* / bottom-* flip if it would
  // clip; for parent-content-block always go below (matches original behavior —
  // flipping above for footnote-style previews tends to overlap preceding content).
  const above = clientRect.top - h - TRIGGER_GAP;
  const below = clientRect.bottom + TRIGGER_GAP;
  let top: number;
  if (placement === "center") {
    top = (clientRect.top + clientRect.bottom) / 2 - h / 2;
  } else if (placement === "top-left" || placement === "top-right") {
    top = above < VIEWPORT_MARGIN && below + h <= vh - VIEWPORT_MARGIN ? below : above;
  } else if (placement === "bottom-left" || placement === "bottom-right") {
    top = below + h > vh - VIEWPORT_MARGIN && above >= VIEWPORT_MARGIN ? above : below;
  } else {
    top = below;
  }

  // horizontal anchor
  let left: number;
  switch (placement) {
    case "top-left":
    case "bottom-left":
      left = clientRect.left - w / 2;
      break;
    case "top-right":
    case "bottom-right":
      left = clientRect.right - w / 2;
      break;
    case "center":
      left = (clientRect.left + clientRect.right) / 2 - w / 2;
      break;
    case "parent-content-block": {
      const parent = findParentContentBlock(tooltipTrigger);
      left = parent ? parent.getBoundingClientRect().left : clientRect.left;
      break;
    }
  }

  // clamp into viewport horizontally; tooltip max-width in CSS guarantees it can fit
  if (left + w > vw - VIEWPORT_MARGIN) {
    left = vw - VIEWPORT_MARGIN - w;
  }
  if (left < VIEWPORT_MARGIN) {
    left = VIEWPORT_MARGIN;
  }

  return { top, left };
}

function findParentContentBlock(trigger: Element): Element | null {
  // walk all the way up and keep the outermost match: nested content-blocks
  // (e.g. footnote inside an attention-block) have inner padding that would
  // shift the tooltip; align to the page's outer content column instead.
  let node: Element | null = trigger;
  let result: Element | null = null;
  while (node) {
    const cn = (node as HTMLElement).className;
    if (cn.includes("content-block")) {
      result = node;
    }
    node = node.parentElement;
  }
  return result;
}

export function Tooltip({ content, contentClassName, placement, children }: TooltipProps) {
  return (
    <TooltipImpl content={content} contentClassName={contentClassName} placement={placement} isSvg={false}>
      {children}
    </TooltipImpl>
  );
}

export function TooltipSvg({ content, placement, children }: TooltipProps) {
  return (
    <TooltipImpl content={content} placement={placement} isSvg={true}>
      {children}
    </TooltipImpl>
  );
}

interface TooltipImplProps extends TooltipProps {
  isSvg: boolean;
}

function TooltipImpl({ isSvg, content, contentClassName, placement, children }: TooltipImplProps) {
  const nodeRef = useRef<Element>(null);

  const ParentComponent = isSvg ? "g" : "div";

  return (
    <ParentComponent
      className="znai-tooltip-provider"
      // @ts-ignore
      ref={nodeRef}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      {children}
    </ParentComponent>
  );

  function handleMouseEnter() {
    if (nodeRef.current) {
      tooltipEngine.display({
        content,
        clientRect: nodeRef.current.getBoundingClientRect(),
        className: contentClassName,
        placement: placement || "top-left",
        tooltipTrigger: nodeRef.current,
      });
      tooltipEngine.setIsOutsideTrigger(false);
    }
  }

  function handleMouseLeave() {
    tooltipEngine.setIsOutsideTrigger(true);
  }
}

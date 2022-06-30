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

import React, { CSSProperties, useEffect, useRef, useState } from "react";
import "./Tooltip.css";

export type TooltipPlacement = "top-left" | "top-right" | "bottom-left" | "bottom-right";

interface TooltipProps {
  content: any;
  children: React.ReactNode;
  placement?: TooltipPlacement;
}

interface TooltipPayload {
  clientRect: DOMRect;
  placement: TooltipPlacement;
  content: any;
}

interface TooltipListener {
  display(payload: TooltipPayload): void;
  clear(): void;
}

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

  clear() {
    this.listeners.forEach((l) => l.clear());
  }
}

const tooltipEngine = new TooltipEngine();

export function TooltipRenderer() {
  const tooltipRef = useRef<HTMLDivElement>(null);
  const [tooltipPayload, setTooltipPayload] = useState<TooltipPayload | null>(null);

  useEffect(() => {
    const listener: TooltipListener = {
      display(payload: TooltipPayload) {
        setTooltipPayload(payload);
      },

      clear() {
        setTooltipPayload(null);
      },
    };

    tooltipEngine.add(listener);
    return () => tooltipEngine.remove(listener);
  }, []);

  if (!tooltipPayload) {
    return null;
  }

  const className = "znai-tooltip " + tooltipPayload.placement;
  return (
    <div
      className={className}
      style={calcPosition(tooltipPayload.clientRect, tooltipPayload.placement)}
      ref={tooltipRef}
    >
      {tooltipPayload.content}
    </div>
  );

  function calcPosition(clientRect: DOMRect, placement: TooltipPlacement): CSSProperties {
    const contentHeight = tooltipRef.current ? tooltipRef.current.clientHeight : 0;

    const gap = 2;

    switch (placement) {
      case "top-left":
        return {
          top: clientRect.top - contentHeight - gap,
          left: clientRect.left - gap,
        };

      case "top-right":
        return {
          top: clientRect.top - contentHeight - gap,
          left: clientRect.right + gap,
        };

      case "bottom-left":
        return {
          top: clientRect.bottom + gap,
          left: clientRect.left - gap,
        };

      case "bottom-right":
        return {
          top: clientRect.bottom + gap,
          left: clientRect.right + gap,
        };
    }
  }
}

export function Tooltip({ content, placement, children }: TooltipProps) {
  return (
    <TooltipImpl content={content} placement={placement} isSvg={false}>
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

function TooltipImpl({ isSvg, content, placement, children }: TooltipImplProps) {
  const nodeRef = useRef<Element>(null);
  const insideElementRef = useRef(false);

  const clientTop = nodeRef.current?.getBoundingClientRect()?.top;

  // hide tooltip on any element movement
  // when scroll happens and element moves around, tooltip stays outside
  // as there is no <leave> event is fired until scroll stops
  useEffect(() => {
    if (clientTop && insideElementRef.current) {
      handleMouseLeave();
    }
  }, [clientTop]);

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
      insideElementRef.current = true;
      tooltipEngine.display({
        content,
        clientRect: nodeRef.current.getBoundingClientRect(),
        placement: placement || "top-left",
      });
    }
  }

  function handleMouseLeave() {
    insideElementRef.current = false;
    tooltipEngine.clear();
  }
}

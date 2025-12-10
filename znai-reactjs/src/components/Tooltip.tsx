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

import { CSSProperties} from "react";
import React, { useEffect, useRef, useState } from "react";

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

  clear() {
    this.listeners.forEach((l) => l.clear());
  }
}

const tooltipEngine = new TooltipEngine();

export function TooltipRenderer() {
  const tooltipRef = useRef<HTMLDivElement>(null);
  const isInsideContentRef = useRef(false);
  const isOutsideTriggerRef = useRef(false);
  const [tooltipPayload, setTooltipPayload] = useState<TooltipPayload | null>(null);

  useEffect(() => {
    const listener: TooltipListener = {
      display(payload: TooltipPayload) {
        setTooltipPayload(payload);
      },

      setIsOutsideTooltipTrigger(isOutside: boolean) {
        isOutsideTriggerRef.current = isOutside;
        if (isOutside && !isInsideContentRef.current) {
          setTimeout(() => {
            if (isOutsideTriggerRef.current && !isInsideContentRef.current) {
              setTooltipPayload(null);
            }
          }, 150);
        }
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

  const styleClassName = tooltipPayload.className || "znai-tooltip-default-style";
  const className = `znai-tooltip ${styleClassName} ${tooltipPayload.placement}`;
  return (
    <div
      className={className}
      style={calcPosition(tooltipPayload.tooltipTrigger, tooltipPayload.clientRect, tooltipPayload.placement)}
      ref={tooltipRef}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      {tooltipPayload.content}
    </div>
  );

  function handleMouseEnter() {
    isInsideContentRef.current = true;
  }

  function handleMouseLeave() {
    isInsideContentRef.current = false;

    if (isOutsideTriggerRef.current) {
      tooltipEngine.clear();
    }
  }

  function calcPosition(triggerNode: Element, clientRect: DOMRect, placement: TooltipPlacement): CSSProperties {
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
        return bottomLeft();

      case "bottom-right":
        return {
          top: clientRect.bottom + gap,
          left: clientRect.right + gap,
        };

      case "center":
        return {
          top: (clientRect.top + clientRect.bottom) / 2.0,
          left: (clientRect.left + clientRect.right) / 2.0,
        };

      case "parent-content-block":
        const parentContentBlock = findParentContentBlock();
        if (parentContentBlock) {
          const contentBlockRect = parentContentBlock.getBoundingClientRect();
          const top = clientRect.bottom + gap;
          const left = contentBlockRect.left;
          return {
            top,
            left,
          };
        } else {
          console.error("can't find parent-content-block", parentContentBlock);
          return bottomLeft();
        }
    }

    function bottomLeft() {
      return {
        top: clientRect.bottom + gap,
        left: clientRect.left - gap,
      };
    }

    function findParentContentBlock() {
      let node: Element | null = triggerNode;
      let result: Element | null = null;
      while (node) {
        if (node.className.includes("content-block")) {
          result = node;
        }

        node = node.parentElement;
      }

      return result;
    }
  }
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
  const isInsideElementRef = useRef(false);

  const clientTop = nodeRef.current?.getBoundingClientRect()?.top;

  // hide tooltip on any element movement
  // when scroll happens and element moves around, tooltip stays outside
  // as there is no <leave> event is fired until scroll stops
  useEffect(() => {
    if (clientTop && isInsideElementRef.current) {
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
      isInsideElementRef.current = true;
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
    isInsideElementRef.current = false;
    tooltipEngine.setIsOutsideTrigger(true);
  }
}

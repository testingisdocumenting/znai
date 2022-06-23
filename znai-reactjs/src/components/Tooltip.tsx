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

interface Props {
  content: any;
  children: React.ReactNode;
}

interface TooltipPayload {
  clientRect: DOMRect;
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

  const position: CSSProperties = {
    top: tooltipPayload.clientRect.top - (tooltipRef.current ? tooltipRef.current.clientHeight : 0),
    left: tooltipPayload.clientRect.left,
  };

  return (
    <div className="znai-tooltip" style={position} ref={tooltipRef}>
      {tooltipPayload.content}
    </div>
  );
}

export function Tooltip({ content, children }: Props) {
  const nodeRef = useRef<HTMLDivElement>(null);

  return (
    <div
      className="znai-tooltip-provider"
      ref={nodeRef}
      onMouseEnter={handleMouseEnter}
      onMouseLeave={handleMouseLeave}
    >
      {children}
    </div>
  );

  function handleMouseEnter() {
    if (nodeRef.current) {
      tooltipEngine.display({
        content,
        clientRect: nodeRef.current.getBoundingClientRect(),
      });
    }
  }

  function handleMouseLeave() {
    tooltipEngine.clear();
  }
}

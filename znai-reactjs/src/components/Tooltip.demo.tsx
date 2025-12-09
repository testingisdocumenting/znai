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

import type { CSSProperties } from "react";
import React from "react";

import type { Registry } from "react-component-viewer";
import type { TooltipPlacement} from "./Tooltip";
import { Tooltip, TooltipRenderer } from "./Tooltip";

export function tooltipDemo(registry: Registry) {
  registry.add("simple", () => (
    <>
      <TooltipRenderer />
      <Tooltip content="hello world">
        <div>hover over me</div>
      </Tooltip>
    </>
  ));

  tooltipPlacement("top-left");
  tooltipPlacement("top-right");
  tooltipPlacement("bottom-left");
  tooltipPlacement("bottom-right");

  function tooltipPlacement(placement: TooltipPlacement) {
    const squareStyle: CSSProperties = { height: 100, width: 100, border: "1px solid black" };

    registry.add(placement, () => (
      <>
        <TooltipRenderer />
        <Tooltip content="hello world" placement={placement}>
          <div style={squareStyle}>hover over me</div>
        </Tooltip>
      </>
    ));
  }
}

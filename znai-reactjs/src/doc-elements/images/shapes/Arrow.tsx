/*
 * Copyright 2021 znai maintainers
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

import React from "react";
import { TooltipPlacement, TooltipSvg } from "../../../components/Tooltip";

interface LineParams {
  beginX: number;
  beginY: number;
  endX: number;
  endY: number;
  text: string;
  invertedColors: boolean;
  className?: string;
  scale: number;
}

const ArrowBody = ({ beginX, beginY, endX, endY, text, scale, invertedColors }: LineParams) => {
  const colors = invertedColors
    ? {
        line: "var(--znai-image-annotation-line-color)",
        fill: "var(--znai-image-annotation-inverted-fill-color)",
        text: "var(--znai-image-annotation-text-color)",
      }
    : {
        line: "var(--znai-image-annotation-inverted-line-color)",
        fill: "var(--znai-image-annotation-fill-color)",
        text: "var(--znai-image-annotation-inverted-text-color)",
      };

  const style = {
    ...colors,
    lineWidth: 1,
    fontSize: "var(--znai-image-annotation-font-size)",
  };

  const scaledBx = beginX * scale;
  const scaledBy = beginY * scale;
  const scaledEx = endX * scale;
  const scaledEy = endY * scale;

  const length = Math.sqrt(Math.pow(scaledEx - scaledBx, 2) + Math.pow(scaledEy - scaledBy, 2));
  const arrowWidth = 6;
  const halfWidth = arrowWidth / 2.0;
  const arrowHeadLength = 15;

  const arrowAngleDx = 10;
  const arrowAngleDy = 8;

  const lengthWithoutArrowHead = length - arrowHeadLength;

  /*                           (end - 1)
                                \
                                 \
    -arrowWidth/2-----------------\      length, -arrowWidth/2
    | 0                                > (end)
    ------------------------------/  <--- lengthWithoutArrowHead, width/2
                                 /
                                / (-arrowAngleDx, arrowAngleDy)
                              (start)

     */

  const path = `M ${lengthWithoutArrowHead - arrowAngleDx} ${halfWidth + arrowAngleDy} 
  l ${arrowAngleDx} ${-arrowAngleDy}
  l ${-lengthWithoutArrowHead} 0
  l 0 ${-arrowWidth}
  l ${lengthWithoutArrowHead} 0
  l ${-arrowAngleDx} ${-arrowAngleDy}
  L ${length} 0 z`;

  const svgArrow = <path d={path} fill={style.fill} stroke={style.line} strokeWidth={style.lineWidth} />;
  const svgArrowWithOptionalTooltip = text ? (
    <TooltipSvg content={text} placement={calcTooltipPlacement()}>
      {svgArrow}
    </TooltipSvg>
  ) : (
    svgArrow
  );

  const svgRotatedArrow = <g transform={`rotate(${calcAngle()})`}>{svgArrowWithOptionalTooltip}</g>;

  return <g transform={`translate(${scaledBx} ${scaledBy})`}>{svgRotatedArrow}</g>;

  function calcAngle() {
    // calc angle https://math.stackexchange.com/questions/878785/how-to-find-an-angle-in-range0-360-between-2-vectors
    const x1 = scaledEx - scaledBx;
    const y1 = scaledBy - scaledEy;
    const x2 = 1;
    const y2 = 0;

    const dot = x1 * x2 + y1 * y2;
    const det = x1 * y2 - y1 * x2;

    return (Math.atan2(det, dot) * 180.0) / Math.PI;
  }

  function calcTooltipPlacement(): TooltipPlacement {
    if (beginX < endX) {
      return beginY < endY ? "bottom-right" : "top-right";
    } else {
      return beginY < endY ? "bottom-left" : "top-left";
    }
  }
};

const arrow = {
  body: ArrowBody,

  knobs: (shape: any) => {
    const tail = { id: "tail", x: shape.beginX, y: shape.beginY };
    const tip = { id: "tip", x: shape.endX, y: shape.endY };
    return [tail, tip];
  },

  update: (shape: any, knobId: string, dx: number, dy: number) => {
    switch (knobId) {
      case "body":
        return {
          ...shape,
          beginX: shape.beginX + dx,
          beginY: shape.beginY + dy,
          endX: shape.endX + dx,
          endY: shape.endY + dy,
        };
      case "tail":
        return { ...shape, beginX: shape.beginX + dx, beginY: shape.beginY + dy };
      case "tip":
        return { ...shape, endX: shape.endX + dx, endY: shape.endY + dy };
      default:
        return { ...shape };
    }
  },
};

export default arrow;

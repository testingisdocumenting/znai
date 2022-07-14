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

import "./Rectangle.css";

interface RectParams {
  beginX: number;
  beginY: number;
  endX: number;
  endY: number;
  scale: number;
  invertedColors?: boolean;
}

const RectangleBody = ({ beginX, beginY, endX, endY, scale, invertedColors }: RectParams) => {
  const scaledBx = beginX * scale;
  const scaledBy = beginY * scale;

  const scaledEx = endX * scale;
  const scaledEy = endY * scale;

  const width = Math.abs(scaledBx - scaledEx);
  const height = Math.abs(scaledBy - scaledEy);

  const x = Math.min(scaledBx, scaledEx);
  const y = Math.min(scaledBy, scaledEy);

  const thickness = 4;
  const lineWidth = 2;

  const colors = invertedColors
    ? {
        line: "var(--znai-image-annotation-inverted-line-color)",
        fill: "var(--znai-image-annotation-fill-color)",
        text: "var(--znai-image-annotation-inverted-text-color)",
      }
    : {
        line: "var(--znai-image-annotation-line-color)",
        fill: "var(--znai-image-annotation-inverted-fill-color)",
        text: "var(--znai-image-annotation-text-color)",
      };

  const style = {
    ...colors,
    lineWidth,
    fontSize: "var(--znai-image-annotation-font-size)",
  };

  /*

       -----------------------  <--| thickness
       | ------------------- |  <--|
       | |                 | |
       | |                 | |
       | ------------------- |
       -----------------------

   */

  const halfLineWidth = lineWidth / 2.0;
  const thicknessAndLine = thickness + lineWidth / 2.0;
  const doubleThicknessAndLine = 2 * thicknessAndLine;

  const pathOuter = `M ${x - thicknessAndLine} ${y - thicknessAndLine} h ${width + doubleThicknessAndLine} v ${
    height + doubleThicknessAndLine
  } h -${width + doubleThicknessAndLine} z`;

  const pathInner = `M ${x - halfLineWidth} ${y - halfLineWidth} v ${height + lineWidth} h ${width + lineWidth} v -${
    height + lineWidth
  } z`;

  return (
    <path
      className="znai-annotation-rectangle"
      d={pathOuter + " " + pathInner}
      fill={style.fill}
      stroke={style.line}
      strokeWidth={style.lineWidth}
      strokeOpacity={0.5}
    />
  );
};

const rectangle = {
  body: RectangleBody,

  knobs: (shape: any) => {
    const topLeft = { id: "topLeft", x: shape.x, y: shape.y };
    const topRight = { id: "topRight", x: shape.x + shape.width, y: shape.y };
    const bottomRight = { id: "bottomRight", x: shape.x + shape.width, y: shape.y + shape.height };
    const bottomLeft = { id: "bottomLeft", x: shape.x, y: shape.y + shape.height };
    return [topLeft, topRight, bottomLeft, bottomRight];
  },

  update: (shape: any, knobId: string, dx: number, dy: number) => {
    switch (knobId) {
      case "body":
        return { ...shape, x: shape.x + dx, y: shape.y + dy };
      case "topLeft":
        return { ...shape, width: shape.width - dx, height: shape.height - dy, x: shape.x + dx, y: shape.y + dy };
      case "topRight":
        return { ...shape, width: shape.width + dx, height: shape.height - dy, y: shape.y + dy };
      case "bottomRight":
        return { ...shape, width: shape.width + dx, height: shape.height + dy };
      case "bottomLeft":
        return { ...shape, width: shape.width - dx, height: shape.height + dy, x: shape.x + dx };
      default:
        return { ...shape };
    }
  },
};

export default rectangle;

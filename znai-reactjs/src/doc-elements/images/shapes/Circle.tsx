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
import { AnnotationStyle, styleByName } from "./styleByName";

import "./Circle.css";

interface CircleParamsBase {
  x: number;
  y: number;
  r: number;
  text: string;
  align: string;
  className?: string;
  scale: number;
}

interface CircleParams extends CircleParamsBase {
  color: string;
}

interface CircleParamsWithStyle extends CircleParamsBase {
  style: AnnotationStyle;
}

interface BadgeParams {
  x: number;
  y: number;
  text: string;
  align: string;
  invertedColors: boolean;
  scale: number;
}

function CircleBody(circleParams: CircleParams) {
  const style = styleByName(circleParams.color);
  return <CircleBodyImpl {...circleParams} style={style} />;
}

function BadgeBody(badgeParams: BadgeParams) {
  const colors = badgeParams.invertedColors
    ? {
        line: "var(--znai-image-annotation-badge-inverted-line-color)",
        fill: "var(--znai-image-annotation-badge-inverted-fill-color)",
        text: "var(--znai-image-annotation-badge-inverted-text-color)",
      }
    : {
        line: "var(--znai-image-annotation-badge-line-color)",
        fill: "var(--znai-image-annotation-badge-fill-color)",
        text: "var(--znai-image-annotation-badge-text-color)",
      };

  const style = {
    ...colors,
    lineWidth: "var(--znai-image-annotation-badge-line-width)",
    fontSize: "var(--znai-image-annotation-badge-font-size)",
  };

  return (
    <CircleBodyImpl {...badgeParams} r={12} style={style} scale={badgeParams.scale} className="znai-annotation-badge" />
  );
}

function CircleBodyImpl({ x, y, r = 15, style, text, align, scale, className }: CircleParamsWithStyle) {
  const [cx, cy] = calcCenter();

  const scaledX = cx * scale;
  const scaledY = cy * scale;

  return (
    <g transform={`translate(${scaledX}, ${scaledY})`} className={className}>
      <circle
        cx={0}
        cy={0}
        r={r}
        stroke={style.line}
        strokeWidth={style.lineWidth}
        fill={style.fill}
        strokeOpacity={1}
        fillOpacity={1}
      />
      <text x={0} y={0} fill={style.text} textAnchor="middle" fontSize={style.fontSize} alignmentBaseline="central">
        {text}
      </text>
    </g>
  );

  function calcCenter() {
    const d = r * 2;
    return [x + deltaX(), y + deltaY()];

    function deltaX() {
      switch (align) {
        case "ToTheLeft":
          return -d;
        case "ToTheRight":
          return d;
        default:
          return 0;
      }
    }

    function deltaY() {
      switch (align) {
        case "Above":
          return -d;
        case "Below":
          return d;
        default:
          return 0;
      }
    }
  }
}

export const circle = {
  body: CircleBody,

  knobs: (shape: CircleParams) => {
    const right = { id: "right", x: shape.x + shape.r, y: shape.y };
    return [right];
  },

  update: (shape: CircleParams, knobId: string, dx: number, dy: number) => {
    if (knobId === "body") {
      return { ...shape, x: shape.x + dx, y: shape.y + dy };
    } else if (knobId === "right") {
      return { ...shape, r: shape.r + dx };
    }
  },
};

export const badge = {
  ...circle,
  body: BadgeBody,
};

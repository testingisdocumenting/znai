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

import React from "react";
import { EchartBar } from "./EchartBar";
import { EchartPie } from "./EchartPie";
import { EchartLine } from "./EchartLine";
import { PresentationProps } from "../presentation/PresentationProps";

interface Props extends PresentationProps {
  chartType: string;
  data: any[][];
  labels?: string[];
  height?: number;
  stack?: boolean;
  horizontal?: boolean;
  legend?: boolean;
  wide?: boolean;
  padding?: string;
  breakpoint?: any[];
}

export function EchartGeneric({
  labels,
  chartType,
  data,
  height,
  stack,
  horizontal,
  legend,
  wide,
  padding,
  breakpoint,
  isPresentation,
  slideIdx,
}: Props) {
  const commonProps = {
    height: height || 400,
    legend: legend || false,
    wide: wide || false,
    padding: padding || "",
    breakpoint: breakpoint || [],
    isPresentation,
    slideIdx,
  };

  switch (chartType) {
    case "bar":
      return <EchartBar labels={labels!} data={data} stack={stack} horizontal={horizontal} {...commonProps} />;
    case "pie":
      return <EchartPie data={data} {...commonProps} />;
    case "line":
      return <EchartLine labels={labels!} data={data} {...commonProps} />;
    default:
      return <div>{"undefined chart type: " + chartType}</div>;
  }
}

export const presentationEchartHandler = {
  component: EchartGeneric,
  numberOfSlides: ({ breakpoint }: Props) => {
    if (breakpoint) {
      return breakpoint.length + 1;
    }

    return 1;
  },
};

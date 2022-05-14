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

interface Props {
  chartType: string;
  data: any[][];
  labels?: string[];
  height?: number;
  stack?: boolean;
  horizontal?: boolean;
}

export function EchartGeneric({ labels, chartType, data, height, stack, horizontal }: Props) {
  const heightToUse = height || 400;

  switch (chartType) {
    case "bar":
      return <EchartBar labels={labels!} data={data} height={heightToUse} stack={stack} horizontal={horizontal} />;
    case "pie":
      return <EchartPie data={data} height={heightToUse} />;
    case "line":
      return <EchartLine labels={labels!} data={data} height={heightToUse} />;
    default:
      return <div>{"undefined chart type: " + chartType}</div>;
  }
}

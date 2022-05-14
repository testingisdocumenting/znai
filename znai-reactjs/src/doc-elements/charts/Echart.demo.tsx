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
import { Registry } from "react-component-viewer";
import { EchartGeneric } from "./EchartGeneric";

export function echartDemo(registry: Registry) {
  registry
    .add("bar", () => <EchartGeneric {...barChartData()} />)
    .add("bar horizontal", () => <EchartGeneric {...barChartData()} horizontal={true} height={700} />)
    .add("bar stacked", () => <EchartGeneric {...barChartData()} stack={true} />)
    .add("bar legend", () => <EchartGeneric {...barChartData()} stack={true} legend={true} />)
    .add("pie", () => <EchartGeneric {...pieChartData()} />)
    .add("pie legend", () => <EchartGeneric {...pieChartData()} legend={true} />)
    .add("line with text x axis", () => <EchartGeneric {...multiLineTextXData()} />)
    .add("line with number", () => <EchartGeneric {...multiLineNumberXData()} />)
    .add("line legend", () => <EchartGeneric {...multiLineTextXData()} legend={true} />);
}

function barChartData() {
  return {
    chartType: "bar",
    labels: ["x", "sales", "tax", "fee"],
    data: [
      ["shirt", 5, 7, 17],
      ["cardigan", 20, 2, 2],
      ["chiffon", 36, 6, 7],
      ["pants", 10, 7, 3],
      ["heels", 10, 2, 5],
      ["socks", 12, 1, 15],
    ],
  };
}

function pieChartData() {
  return {
    chartType: "pie",
    labels: [],
    data: [
      ["shirt", 5],
      ["cardigan", 20],
      ["chiffon", 36],
      ["pants", 10],
      ["heels", 10],
      ["socks", 12],
    ],
  };
}

function multiLineTextXData() {
  return {
    chartType: "line",
    labels: ["x", "sales", "tax", "fee"],
    data: [
      ["Monday", 5, 7, 17],
      ["Tuesday", 20, 2, 2],
      ["Wednesday", 36, 6, 7],
      ["Thursday", 10, 7, 3],
      ["Friday", 10, 2, 5],
    ],
  };
}

function multiLineNumberXData() {
  return {
    chartType: "line",
    labels: ["x", "sales", "tax", "fee"],
    data: [
      [5, 5, 7, 17],
      [14, 20, 2, 2],
      [30, 36, 6, 7],
      [33, 10, 7, 3],
      [38, 10, 2, 5],
    ],
  };
}

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
    .add("bar padding", () => <EchartGeneric {...barChartData()} padding="10% 35%" />)
    .add("bar horizontal", () => <EchartGeneric {...barChartData()} horizontal={true} height={700} />)
    .add("bar stacked", () => <EchartGeneric {...barChartData()} stack={true} />)
    .add("bar stacked horizontal", () => <EchartGeneric {...barChartDataLarge()} stack={true} horizontal={true} />)
    .add("bar legend", () => <EchartGeneric {...barChartData()} stack={true} legend={true} />)
    .add("pie", () => <EchartGeneric {...pieChartData()} />)
    .add("pie legend", () => <EchartGeneric {...pieChartData()} legend={true} />)
    .add("line with text x axis", () => <EchartGeneric {...multiLineTextXData()} />)
    .add("line with number", () => <EchartGeneric {...multiLineNumberXData()} />)
    .add("line time", () => <EchartGeneric {...timelineChart("line")} />)
    .add("bar time", () => <EchartGeneric {...timelineChart("line")} />)
    .add("line legend", () => <EchartGeneric {...multiLineTextXData()} legend={true} />)
    .add("line wide", () => <EchartGeneric {...multiLineNumberXDataLarge()} wide={true} />);
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

function barChartDataLarge() {
  return {
    chartType: "bar",
    labels: ["x", "sales", "tax", "fee"],
    data: [
      ["shirt", 5000, 7000, 17000],
      ["cardigan", 20000, 2000, 2000],
      ["chiffon", 36000, 6000, 7000],
      ["pants", 10000, 7000, 3000],
      ["heels", 10000, 2000, 5000],
      ["socks", 12000, 1000, 15000],
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

function timelineChart(type: string) {
  return {
    chartType: type,
    isTimeSeries: true,
    labels: ["time", "fee", "feefee"],
    data: [
      ["2022-10-04", 5, 7],
      ["2022-11-05", 20, 2],
      ["2022-12-12", 36, 6],
      ["2023-01-14", 10, 7],
      ["2023-02-24", 10, 2],
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

function multiLineNumberXDataLarge() {
  return {
    chartType: "line",
    labels: ["x", "sales", "tax", "fee"],
    data: [
      [0, 5, 7, 1700],
      [202138, 20000, 200, 300],
      [502138, 3006, 6, 7],
      [1002138, 10000, 7, 3],
      [2002138, 10, 15000, 5],
    ],
  };
}

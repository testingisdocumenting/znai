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
import { EchartReactWrapper } from "./EchartReactWrapper";

interface Props {
  labels: string[];
  data: any[][];
  height: number;
  stack?: boolean;
  horizontal?: boolean;
}

export function EchartBar({ labels, data, height, stack, horizontal }: Props) {
  return <EchartReactWrapper height={height} echartConfigProvider={configProvider} />;

  function configProvider() {
    const series = [];
    for (let colIdx = 1; colIdx < labels.length; colIdx++) {
      series.push(createSeriesInstance(colIdx));
    }

    return {
      ...defineAxes(),
      series: series,
    };

    function defineAxes() {
      const axisData = data.map((row) => row[0]);

      return horizontal
        ? {
            xAxis: {},
            yAxis: {
              data: axisData,
            },
          }
        : {
            xAxis: {
              data: axisData,
            },
            yAxis: {},
          };
    }

    function createSeriesInstance(columnIdx: number) {
      return {
        name: labels[columnIdx],
        type: "bar",
        data: data.map((row) => row[columnIdx]),
        stack: stack ? "stack" : undefined,
      };
    }
  }
}

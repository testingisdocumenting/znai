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
import { EchartCommonProps } from "./EchartCommon";
import { createInvisibleLineSeries, partialDataExcludingDataAfterPoint } from "./echartUtils";

const type = "line";

interface Props extends EchartCommonProps {
  labels: string[];
  data: any[][];
}

export function EchartLine({ labels, data, ...commonProps }: Props) {
  return <EchartReactWrapper echartConfigProvider={configProvider} {...commonProps} />;

  function configProvider() {
    const firstRow = data.length > 0 ? data[0] : [0];
    const isXNumbers = typeof firstRow[0] === "number";

    const series = [];
    for (let colIdx = 1; colIdx < labels.length; colIdx++) {
      series.push(createSeriesInstance(colIdx));
    }

    series.push({ ...createInvisibleLineSeries(data), type });

    return {
      ...defineAxes(),
      series: series,
    };

    function defineAxes() {
      if (isXNumbers) {
        return {
          xAxis: {},
          yAxis: {},
        };
      }

      const axisData = data.map((row) => row[0]);

      return {
        xAxis: {
          data: axisData,
        },
        yAxis: {},
      };
    }

    function createSeriesInstance(columnIdx: number) {
      return {
        name: labels[columnIdx],
        type,
        data: partialDataExcludingDataAfterPoint(data, columnIdx, calcBreakpoint()),
      };

      function calcBreakpoint() {
        if (!commonProps.isPresentation || !commonProps.breakpoints) {
          return undefined;
        }

        if (commonProps.slideIdx! > commonProps.breakpoints.length) {
          return undefined;
        }

        return commonProps.breakpoints[commonProps.slideIdx!];
      }
    }
  }
}

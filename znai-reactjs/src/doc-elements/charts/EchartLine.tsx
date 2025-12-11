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
import {
  createInvisibleLineSeries,
  echartCalcBreakpoint,
  isNumericChartValue,
  partialDataExcludingDataAfterPoint,
} from "./echartUtils";

const type = "line";

interface Props extends EchartCommonProps {
  labels: string[];
}

export function EchartLine(props: Props) {
  return (
    <EchartReactWrapper
      echartConfigProvider={configProvider}
      maxAxisNumericValueProvider={maxNumberProvider}
      {...props}
    />
  );

  function configProvider() {
    const firstRow = props.data.length > 0 ? props.data[0] : [0];
    const isXNumbersOrTime = isNumericChartValue(firstRow[0]) || props.isTimeSeries;

    const series = [];
    for (let colIdx = 1; colIdx < props.labels.length; colIdx++) {
      series.push(createSeriesInstance(colIdx));
    }

    series.push({ ...createInvisibleLineSeries(props.data, props.isTimeSeries), type });

    return {
      ...defineAxes(),
      series: series,
    };

    function defineAxes() {
      if (isXNumbersOrTime) {
        return {
          xAxis: props.isTimeSeries ? { type: "time" } : {},
          yAxis: {},
        };
      }

      const axisData = props.data.map((row) => row[0]);

      return {
        xAxis: {
          data: axisData,
        },
        yAxis: {},
      };
    }

    function createSeriesInstance(columnIdx: number) {
      return {
        name: props.labels[columnIdx],
        type,
        data: partialDataExcludingDataAfterPoint(
          props.data,
          columnIdx,
          props.isTimeSeries,
          echartCalcBreakpoint(props)
        ),
      };
    }
  }

  function maxNumberProvider() {
    let maxX = Number.MIN_SAFE_INTEGER;

    for (let rowIdx = 0; rowIdx < props.data.length; rowIdx++) {
      maxX = Math.max(maxX, props.data[rowIdx][0]);
    }

    return maxX;
  }
}

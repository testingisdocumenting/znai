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

interface Props extends EchartCommonProps {
  labels: string[];
  stack?: boolean;
  horizontal?: boolean;
}

export function EchartBar(props: Props) {
  return (
    <EchartReactWrapper
      echartConfigProvider={configProvider}
      maxAxisNumericValueProvider={maxAxisNumericValueProvider}
      {...props}
    />
  );

  function configProvider() {
    const series = [];
    for (let colIdx = 1; colIdx < props.labels.length; colIdx++) {
      series.push(createSeriesInstance(colIdx));
    }

    return {
      ...defineAxes(),
      series: series,
    };

    function defineAxes() {
      const axisData = props.data.map((row) => row[0]);

      return props.horizontal
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
        name: props.labels[columnIdx],
        type: "bar",
        data: props.data.map((row) => row[columnIdx]),
        stack: props.stack ? "stack" : undefined,
      };
    }
  }

  function maxAxisNumericValueProvider() {
    if (!props.horizontal) {
      return 0;
    }

    if (props.stack) {
      return maxSum();
    }

    return max();

    function maxSum() {
      let result = Number.MIN_SAFE_INTEGER;

      for (let rowIdx = 0; rowIdx < props.data.length; rowIdx++) {
        let sum = 0;
        for (let colIdx = 1; colIdx < props.labels.length; colIdx++) {
          sum += props.data[rowIdx][colIdx];
        }

        result = Math.max(result, sum);
      }

      return result;
    }

    function max() {
      let result = Number.MIN_SAFE_INTEGER;

      for (let rowIdx = 0; rowIdx < props.data.length; rowIdx++) {
        for (let colIdx = 1; colIdx < props.labels.length; colIdx++) {
          result = Math.max(result, props.data[rowIdx][colIdx]);
        }
      }

      return result;
    }
  }
}

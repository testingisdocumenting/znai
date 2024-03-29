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

import { EchartCommonProps } from "./EchartCommon";

export function isNumericChartValue(value: any) {
  return typeof value === "number";
}

/**
 * finds max and min values from data and creates invisible line data series
 * so that chart scale remains the same for presentation mode as new values are revealed over time
 * @param data data to analyze
 * @param isTimeSeries true if treat as time series
 */
export function createInvisibleLineSeries(data: any[][], isTimeSeries: boolean) {
  const isNumericOrTime = isNumericChartValue(data[0][0]) || isTimeSeries;

  let minY = Number.MAX_SAFE_INTEGER;
  let maxY = Number.MIN_SAFE_INTEGER;

  let minX = isNumericOrTime ? Number.MAX_SAFE_INTEGER : data[0][0];
  let maxX = isNumericOrTime ? Number.MIN_SAFE_INTEGER : data[data.length - 1][0];

  for (let rowIdx = 0; rowIdx < data.length; rowIdx++) {
    if (isNumericOrTime) {
      minX = Math.min(minX, data[rowIdx][0]);
      maxX = Math.max(maxX, data[rowIdx][0]);
    }

    for (let yIdx = 1; yIdx < data[0].length; yIdx++) {
      minY = Math.min(minY, data[rowIdx][yIdx]);
      maxY = Math.max(maxY, data[rowIdx][yIdx]);
    }
  }

  return {
    data: [
      {
        value: isNumericOrTime ? [minX, minY] : minY,
        itemStyle: { color: "none" },
      },
      {
        value: isNumericOrTime ? [maxX, maxY] : maxY,
        itemStyle: { color: "none" },
      },
    ],

    lineStyle: { color: "none" },
  };
}

/**
 * creates partial data based on column and break point value
 * all the values after breakpoint are ignored
 * @param fullData all data
 * @param colIdx column index to process
 * @param isTimeSeries true if treat X as time
 * @param breakpointX breakpoint to split data
 */
export function partialDataExcludingDataAfterPoint(
  fullData: any[][],
  colIdx: number,
  isTimeSeries: boolean,
  breakpointX: number | string | undefined
) {
  const partialData: any[][] = [];

  const isNumbersOrTimeX = isNumericChartValue(fullData[0][0]) || isTimeSeries;

  for (let rowIdx = 0; rowIdx < fullData.length; rowIdx++) {
    const row = fullData[rowIdx];
    const x = row[0];

    if (isNumbersOrTimeX && breakpointX !== undefined && x > breakpointX) {
      break;
    }

    partialData.push(isNumbersOrTimeX ? [x, row[colIdx]] : row[colIdx]);

    if (!isNumbersOrTimeX && x === breakpointX) {
      break;
    }
  }

  return partialData;
}

export function echartCalcBreakpoint(props: EchartCommonProps) {
  if (!props.isPresentation || !props.breakpoint) {
    return undefined;
  }

  if (props.slideIdx! > props.breakpoint.length) {
    return undefined;
  }

  return props.breakpoint[props.slideIdx!];
}

export function findBreakpointDataIndexForText(data: any[][], breakpoint: string | undefined) {
  if (!breakpoint) {
    return undefined;
  }

  for (let rowIdx = 0; rowIdx < data.length; rowIdx++) {
    const row = data[rowIdx];
    const x = row[0];

    if (breakpoint !== undefined && x === breakpoint) {
      return rowIdx;
    }
  }

  return undefined;
}

/**
 * creates grid config from padding shortcut
 * @param padding "10%" "10% 10%" "5px 10px"
 */
export function echartGridUsingPadding(padding: string) {
  const trimmed = padding.trim();
  if (trimmed.length === 0) {
    return undefined;
  }

  const parts = trimmed.split(/\s+/);
  if (parts.length === 1) {
    const value = parts[0];
    return {
      left: value,
      right: value,
      top: value,
      bottom: value,
    };
  }

  if (parts.length >= 2) {
    const topBottom = parts[0];
    const leftRight = parts[1];
    return {
      left: leftRight,
      right: leftRight,
      top: topBottom,
      bottom: topBottom,
    };
  }
}

const numberFormatter = new Intl.NumberFormat("en-US");

export function echartGridUsingMaxDataAndLegend(legend: boolean, maxAxisNumber: number) {
  return {
    left: 0,
    right: rightGap(),
    top: 8 + (legend ? 32 : 0),
    bottom: 0,
  };

  function rightGap() {
    const maxValueLabel = numberFormatter.format(maxAxisNumber);

    // approximate size of half of the value
    // we calc padding as echarts cuts label on the right
    return (maxValueLabel.length / 2.0) * 8;
  }
}

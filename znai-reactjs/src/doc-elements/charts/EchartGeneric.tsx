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

import React, { useEffect } from "react";

import { configuredEcharts } from "./EchartsCommon";
import { useRef } from "react";
import { EChartsType } from "echarts/types/dist/shared";

const echarts = configuredEcharts();

interface Props {
  labels: string[];
  chartType: string;
  data: any[][];
  height?: number;
  stack?: boolean;
  horizontal?: boolean;
}

export function EchartGeneric({ labels, chartType, data, height, stack, horizontal }: Props) {
  const echartDivNodeRef = useRef<HTMLDivElement>(null);
  const echartRef = useRef<EChartsType>();

  useEffect(() => {
    echartRef.current = echarts.init(echartDivNodeRef.current!);

    const series = [];
    for (let colIdx = 1; colIdx < labels.length; colIdx++) {
      series.push(createSeriesInstance(colIdx));
    }

    echartRef.current.setOption({
      tooltip: {},
      ...defineAxes(),
      animation: false,
      series: series,
    });

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
        type: chartType,
        data: data.map((row) => row[columnIdx]),
        stack: stack ? "stack" : undefined,
      };
    }
  }, [chartType, labels, data, stack, horizontal]);

  useEffect(() => {
    if (echartRef.current) {
      echartRef.current.resize();
    }
  }, [height]);

  const heightToUse = height || 400;

  return <div className="content-block" ref={echartDivNodeRef} style={{ height: heightToUse }} />;
}

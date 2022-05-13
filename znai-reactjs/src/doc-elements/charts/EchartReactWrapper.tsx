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

import React, { useEffect, useRef } from "react";
import { EChartsType } from "echarts/types/dist/shared";
import { configuredEcharts } from "./EchartsCommon";

interface Props {
  echartConfigProvider(): any;
  height: number;
}

const echarts = configuredEcharts();

export function EchartReactWrapper({ echartConfigProvider, height }: Props) {
  const echartDivNodeRef = useRef<HTMLDivElement>(null);
  const echartRef = useRef<EChartsType>();

  useEffect(() => {
    echartRef.current = echarts.init(echartDivNodeRef.current!);

    const config = {
      ...echartConfigProvider(),
      tooltip: {},
      animation: false,
    };

    echartRef.current.setOption(config);
  }, [echartConfigProvider]);

  useEffect(() => {
    if (echartRef.current) {
      echartRef.current.resize();
    }
  }, [height]);

  return <div className="content-block" ref={echartDivNodeRef} style={{ height }} />;
}

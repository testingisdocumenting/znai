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

import React, { MutableRefObject, RefObject, useEffect, useRef } from "react";
import { EChartsType } from "echarts/types/dist/shared";
import { configuredEcharts, EchartCommonProps } from "./EchartCommon";

import { PresentationProps } from "../presentation/PresentationProps";

import "./EchartReactWrapper.css";

interface Props extends EchartCommonProps, PresentationProps {
  echartConfigProvider(): any;
}

const echarts = configuredEcharts();

export function EchartReactWrapper(props: Props) {
  const echartDivNodeRef = useRef<HTMLDivElement>(null);
  const echartRef = useRef<EChartsType>();

  useEffect(() => {
    // TODO theme integration via context
    // @ts-ignore
    window.znaiTheme.addChangeHandler(onThemeChange);
    // @ts-ignore
    return () => window.znaiTheme.removeChangeHandler(onThemeChange);

    function onThemeChange() {
      createOrInitEchart(echartDivNodeRef, echartRef, props);
    }
  }, [props]);

  useEffect(
    () => {
      createOrInitEchart(echartDivNodeRef, echartRef, props);
    },
    // @ts-ignore
    [props]
  );

  useEffect(() => {
    if (echartRef.current) {
      echartRef.current.resize();
    }
  }, [props.height]);

  const style = {
    width: props.isPresentation ? "var(--znai-single-column-full-width)" : undefined,
    height: props.height,
  };

  return <div className="content-block znai-chart" ref={echartDivNodeRef} style={style} />;
}

function createOrInitEchart(
  htmlNode: RefObject<HTMLDivElement>,
  echartRef: MutableRefObject<EChartsType | undefined>,
  props: Props
) {
  if (echartRef.current) {
    echartRef.current?.dispose();
  }

  echartRef.current = echarts.init(
    htmlNode.current!,
    // TODO theme context
    // @ts-ignore
    window.znaiTheme.name === "znai-dark" ? "dark" : undefined
  );

  const config = {
    tooltip: { trigger: "axis" },
    ...props.echartConfigProvider(),
    legend: createLegend(),
    animation: false,
  };

  echartRef.current.setOption(config);

  function createLegend() {
    if (!props.legend) {
      return undefined;
    }

    return {
      orient: "horizontal",
    };
  }
}

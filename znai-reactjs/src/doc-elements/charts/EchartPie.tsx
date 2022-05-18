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

export function EchartPie(props: EchartCommonProps) {
  return <EchartReactWrapper echartConfigProvider={configProvider} {...props} />;

  function configProvider() {
    return {
      series: [createSeriesInstance()],
      tooltip: {},
    };

    function createSeriesInstance() {
      const labelsGap = 20;
      const legendGap = props.legend ? 32 : 0;
      return {
        radius: props.height / 2.0 - labelsGap - legendGap,
        type: "pie",
        data: props.data.map((row) => ({ name: row[0], value: row[1] })),
      };
    }
  }
}

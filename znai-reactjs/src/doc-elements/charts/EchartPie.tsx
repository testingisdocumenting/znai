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
  data: any[][];
  height: number;
}

export function EchartPie({ data, height }: Props) {
  return <EchartReactWrapper height={height} echartConfigProvider={configProvider} />;

  function configProvider() {
    return {
      series: [createSeriesInstance()],
    };

    function createSeriesInstance() {
      return {
        radius: height / 2.0 - 20,
        type: "pie",
        data: data.map((row) => ({ name: row[0], value: row[1] })),
      };
    }
  }
}

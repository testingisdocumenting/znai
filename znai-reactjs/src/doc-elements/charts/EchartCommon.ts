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

import * as echarts from "echarts/core";
import { GridComponent, TooltipComponent, LegendComponent } from "echarts/components";
import { BarChart, LineChart, PieChart } from "echarts/charts";
import { LabelLayout } from "echarts/features";
import { SVGRenderer } from "echarts/renderers";
import { PresentationProps } from "../presentation/PresentationProps";

let configured = false;
export function configuredEcharts() {
  if (!configured) {
    echarts.use([
      TooltipComponent,
      GridComponent,
      LegendComponent,
      BarChart,
      PieChart,
      LineChart,
      LabelLayout,
      SVGRenderer,
    ]);

    configured = true;
  }

  return echarts;
}

export interface EchartCommonProps extends PresentationProps {
  breakpoints: any[];
  height: number;
  legend: boolean;
}

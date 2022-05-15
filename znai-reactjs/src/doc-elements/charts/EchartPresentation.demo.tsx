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

import { createPresentationDemo } from "../demo-utils/PresentationDemo";
import { Registry } from "react-component-viewer";

export function chartsPresentationDemo(registry: Registry) {
  registry.add(
    "line labels",
    createPresentationDemo([
      {
        type: "EchartGeneric",
        chartType: "line",
        labels: ["x", "sales", "tax", "fee"],
        data: [
          ["Monday", 5, 7, 17],
          ["Tuesday", 20, 2, 2],
          ["Wednesday", 36, 6, 7],
          ["Thursday", 10, 7, 3],
          ["Friday", 10, 2, 5],
        ],
        breakpoints: ["Tuesday", "Thursday"],
      },
    ])
  );

  registry.add(
    "line numbers",
    createPresentationDemo([
      {
        type: "EchartGeneric",
        chartType: "line",
        legend: true,
        labels: ["x", "sales", "tax", "fee"],
        data: [
          [3, 5, 7, 17],
          [15, 20, 2, 2],
          [20.5, 36, 6, 7],
          [30.8, 10, 7, 3],
          [54.12, 10, 2, 5],
        ],
        breakpoints: [16, 21, 31],
      },
    ])
  );
}

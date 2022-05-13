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
import { Registry } from "react-component-viewer";
import { EchartGeneric } from "./EchartGeneric";

export function echartDemo(registry: Registry) {
  registry.add("bar", () => (
    <EchartGeneric
      labels={["x", "sales", "tax", "fee"]}
      chartType="bar"
      data={[
        ["shirt", 5, 7, 17],
        ["cardigan", 20, 2, 2],
        ["chiffon", 36, 6, 7],
        ["pants", 10, 7, 3],
        ["heels", 10, 2, 5],
        ["socks", 12, 1, 15],
      ]}
    />
  ));
}

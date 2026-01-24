/*
 * Copyright 2025 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import { Svg } from "./Svg";
import { Registry, simulateState } from "react-component-viewer";
import { svg } from "./svg.testdata";

const [getActualSize, setActualSize] = simulateState(false);

export function embeddedSvgDemo(registry: Registry) {
  registry
    .add("simple", () => <Svg svg={svg()} />)
    .add("partially revealed", () => <Svg svg={svg()} idsToReveal={["partC"]} />)
    .add("actual size", () => <Svg svg={svg()} actualSize={true} />)
    .add("partially revealed with actual size", () => <Svg svg={svg()} idsToReveal={["partC"]} actualSize={true} />)
    .add("actual size with scale", () => <Svg svg={svg()} actualSize={true} scale={0.5} />)
    .add("partially revealed with actual size and scale", () => (
      <Svg svg={svg()} idsToReveal={["partC"]} scale={0.5} actualSize={true} />
    ))
    .add("flip actual size for preview mode", () => (
      <div>
        <button onClick={toggleActualSize}>toggle actual size</button>
        <Svg svg={svg(300, undefined)} idsToReveal={["partC"]} scale={0.5} actualSize={getActualSize()} />
      </div>
    ))
    .add("wide svg without fit", () => <Svg svg={svg(1200, 400)} />)
    .add("wide svg with fit", () => <Svg svg={svg(1200, 400)} fit={true} />)
    .add("wide svg with fit and scale", () => <Svg svg={svg(1200, 400)} fit={true} scale={0.8} />);
}

function toggleActualSize() {
  setActualSize(!getActualSize());
}

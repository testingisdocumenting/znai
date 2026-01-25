/*
 * Copyright 2026 znai maintainers
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

import { createPresentationDemo } from "../demo-utils/PresentationDemo";
import { svg, wideBoxesSvg } from "./svg.testdata";

export function svgPresentationDemo(registry) {
  registry
    .add(
      "embedded",
      createPresentationDemo([
        {
          type: "Svg",
          svg: svg(),
          idsToReveal: ["partA", "partC"],
        },
      ])
    )
    .add(
      "from src",
      createPresentationDemo([
        {
          type: "Svg",
          svgSrc: "svg.svg",
          idsToReveal: ["partA", "partC"],
        },
      ])
    )
    .add(
      "all at once",
      createPresentationDemo([
        {
          type: "Svg",
          svgSrc: "svg.svg",
          meta: { allAtOnce: true },
          idsToReveal: ["partA", "partC"],
        },
      ])
    )
    .add(
      "no ids to reveal",
      createPresentationDemo([
        {
          type: "Svg",
          svg: svg(),
        },
      ])
    )
    .add(
      "wide boxes embedded",
      createPresentationDemo([
        {
          type: "Svg",
          svg: wideBoxesSvg(),
        },
      ])
    )
    .add(
      "wide boxes from src",
      createPresentationDemo([
        {
          type: "Svg",
          svgSrc: "wide-boxes.svg",
        },
      ])
    );
}

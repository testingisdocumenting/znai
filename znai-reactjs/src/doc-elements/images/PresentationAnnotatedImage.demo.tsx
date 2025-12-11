/*
 * Copyright 2020 znai maintainers
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

export function imagePresentationDemo(registry: Registry) {
  registry
    .add(
      "image with fit",
      createPresentationDemo([
        {
          type: "AnnotatedImage",
          imageSrc: "ui.jpg",
          fit: true,
          shapes: [],
          width: 800,
          height: 400,
        },
      ])
    )
    .add(
      "image with badges",
      createPresentationDemo([
        {
          type: "AnnotatedImage",
          imageSrc: "ui.jpg",
          shapes: badgeAlignment(220, 220, false).shapes,
          width: 800,
          height: 400,
        },
      ])
    );
}

function badgeAlignment(x: number, y: number, inverted: boolean) {
  return {
    imageSrc: "ui.jpg",
    width: 800,
    height: 400,
    shapes: [
      { type: "badge", id: "c1", x, y, text: "1", invertedColors: inverted },
      {
        type: "badge",
        id: "c1",
        x,
        y,
        text: "2",
        align: "Above",
        invertedColors: inverted,
      },
      {
        type: "badge",
        id: "c2",
        x,
        y,
        text: "3",
        align: "Below",
        invertedColors: inverted,
      },
      {
        type: "badge",
        id: "c2",
        x,
        y,
        text: "4",
        align: "ToTheLeft",
        invertedColors: inverted,
      },
      {
        type: "badge",
        id: "c2",
        x,
        y,
        text: "5",
        align: "ToTheRight",
        invertedColors: inverted,
      },
    ],
  };
}

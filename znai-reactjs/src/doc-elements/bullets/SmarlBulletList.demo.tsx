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

import { elementsLibrary } from "../DefaultElementsLibrary";
import { Registry } from "react-component-viewer";

export function smartBulletListsDemo(registry: Registry) {
  registry.add("steps left", () => (
    // @ts-ignore
    <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={shortBullets("Steps", "left")} />
  ));
  registry.add("steps right", () => (
    // @ts-ignore
    <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={shortBullets("Steps", "right")} />
  ));
  registry.add("steps center", () => (
    // @ts-ignore
    <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={shortBullets("Steps", "center")} />
  ));
}

function shortBullets(type: string, align: string) {
  return [
    {
      bulletMarker: "*",
      tight: false,
      type: "BulletList",
      meta: {
        bulletListType: type,
        align,
      },
      content: [
        {
          type: "ListItem",
          content: [
            {
              type: "Paragraph",
              content: [
                {
                  text: "first",
                  type: "SimpleText",
                },
              ],
            },
          ],
        },
        {
          type: "ListItem",
          content: [
            {
              type: "Paragraph",
              content: [
                {
                  text: "second multi words",
                  type: "SimpleText",
                },
              ],
            },
          ],
        },
        {
          type: "ListItem",
          content: [
            {
              type: "Paragraph",
              content: [
                {
                  text: "third",
                  type: "SimpleText",
                },
              ],
            },
          ],
        },
      ],
    },
  ];
}

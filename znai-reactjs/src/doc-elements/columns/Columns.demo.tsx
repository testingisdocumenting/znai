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

import React from "react";

import { elementsLibrary } from "../DefaultElementsLibrary";
import { Columns } from "./Columns";
import { ViewPortProvider } from "../../theme/ViewPortContext";
import { Registry } from "react-component-viewer";

export function columnsDemo(registry: Registry) {
  registry
    .add("half half", () => <Columns elementsLibrary={elementsLibrary} {...contentAndConfig()} />)
    .add("half half mobile", () => (
      <ViewPortProvider isMobileForced={true}>
        <Columns elementsLibrary={elementsLibrary} {...contentAndConfig()} />
      </ViewPortProvider>
    ))
    .add("missing column content", () => <Columns elementsLibrary={elementsLibrary}
                                                  {...missingContentForRightColumn()} />)
    .add("with code snippet", () => <Columns elementsLibrary={elementsLibrary} {...snippetAndText()} />)
    .add("with code snippet mobile", () => (
      <ViewPortProvider isMobileForced={true}>
        <Columns elementsLibrary={elementsLibrary} {...snippetAndText()} />
      </ViewPortProvider>
    ));
}

function contentAndConfig() {
  return {
    isPresentation: false,
    slideIdx: 0,
    config: {
      border: true,
      left: { portion: 10 },
      right: {},
    },
    columns: [
      {
        content: [
          {
            text: `It_is_very_`,
            type: "SimpleText",
          },
        ],
      },
      {
        content: [
          {
            text: "one liner",
            type: "SimpleText",
          },
        ],
      },
    ],
  };
}

function missingContentForRightColumn() {
  return {
    isPresentation: false,
    slideIdx: 0,
    columns: [
      {
        content: [
          {
            type: "Paragraph",
            content: [{
              isFile: false,
              type: "Link",
              content: [{
                text: "left link",
                type: "SimpleText"
              }],
              url: "http://localhost:3030"
            }]
          }
        ]
      },
      {
        content: []
      }
    ],
    type: "Columns",
    config: {}
  }
}

function snippetAndText() {
  return {
    isPresentation: false,
    slideIdx: 0,
    config: {
      left: { portion: 10 },
    },
    columns: [
      {
        content: [
          {
            text:
              "multi line text multi line text multi line text multi line text " +
              "multi line text multi line text multi line text multi line text " +
              "multi line text multi line text multi line text multi line text " +
              "multi line text multi line text multi line text multi line text " +
              "multi line text multi line text multi line text multi line text",
            type: "SimpleText",
          },
        ],
      },
      {
        content: [
          {
            snippet: "function test() {\n  return null;\n}\n",
            type: "Snippet",
            lang: "javascript",
          },
        ],
      },
    ],
  };
}

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
import { Section } from "../default-elements/Section";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { TooltipRenderer } from "../../components/Tooltip";

export function annotatedImageWithOrderedListDemo(registry: Registry) {
  registry.add("list first", () => (
    <>
      <TooltipRenderer />
      <Section id="list-first" title="List First" elementsLibrary={elementsLibrary} content={listFirstContent()} />
    </>
  ));
  registry.add("image first", () => (
    <>
      <TooltipRenderer />
      <Section id="image-first" title="Image First" elementsLibrary={elementsLibrary} content={imageFirstContent()} />
    </>
  ));
}

function orderedList() {
  return {
    type: "OrderedList",
    startNumber: 1,
    content: [
      {
        type: "ListItem",
        content: [
          {
            type: "Paragraph",
            content: [
              {
                text: "First list item's first paragraph.",
                type: "SimpleText",
              },
              {
                type: "SoftLineBreak",
              },
              {
                text: "This is still part of the first paragraph.",
                type: "SimpleText",
              },
            ],
          },
          {
            type: "Paragraph",
            content: [
              {
                text: "First list item's second paragraph",
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
                text: "Second list item's first paragraph",
                type: "SimpleText",
              },
            ],
          },
        ],
      },
    ],
  };
}

function annotatedImage() {
  return {
    type: "AnnotatedImage",
    imageSrc: "ui.jpg",
    width: 800,
    height: 400,
    shapes: [
      {
        type: "arrow",
        id: "a1",
        beginX: 200,
        beginY: 100,
        endX: 400,
        endY: 150,
        text: "hello arrow",
      },
      {
        type: "rectangle",
        id: "r1",
        beginX: 20,
        beginY: 10,
        endX: 60,
        endY: 50,
        text: "",
      },
      {
        type: "badge",
        id: "c1",
        x: 100,
        y: 100,
        text: "1",
        align: "center",
        invertedColors: true,
      },
      {
        type: "badge",
        id: "c2",
        x: 400,
        y: 200,
        text: "2",
        align: "center",
        invertedColors: false,
      },
    ],
  };
}

function listFirstContent() {
  return [orderedList(), annotatedImage()];
}

function imageFirstContent() {
  return [annotatedImage(), orderedList()];
}

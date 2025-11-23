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

import type { Registry } from "react-component-viewer";
import { Card } from "./Card";
import {
  contentApiParameters,
  contentParagraph,
  contentParagraphSmall,
  contentSingleLink,
  contentSnippet,
  contentTable,
} from "../demo-utils/contentGenerators";
import { elementsLibrary } from "../DefaultElementsLibrary";
import type { DocElementContent } from "../default-elements/DocElement";

export function cardsDemo(registry: Registry) {
  registry.add("large image", () => (
    <Card
      title="My Card"
      imageSrc="diamond.svg"
      bodyContent={[contentParagraph(false)]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("forced image height", () => (
    <Card
      title="My Card"
      imageSrc="diamond.svg"
      imageHeight={100}
      bodyContent={[contentParagraph(false)]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("background", () => (
    <Card
      title="My Card"
      imageSrc="diamond.svg"
      imageHeight={120}
      imageBackground="linear-gradient(to right, #266465, #9198e5)"
      bodyContent={[contentParagraph(false)]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("small image with code", () => (
    <Card
      title="My Card"
      imageSrc="small-book.png"
      // @ts-ignore
      bodyContent={[contentParagraphSmall(false), { ...contentSnippet(false), title: "example" }]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("with link and code", () => (
    <Card
      title="My Card"
      imageSrc="diamond.svg"
      imageHeight={120}
      imageBackground="linear-gradient(to right, #266465, #9198e5)"
      bodyContent={[
        contentParagraphSmall(false),
        // @ts-ignore
        { ...contentSnippet(false), title: "example" },
        contentSingleLink("Learn More"),
        contentSingleLink("report"),
      ]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("small image with api parameters", () => (
    <Card
      title="My Card"
      imageSrc="small-book.png"
      // @ts-ignore
      bodyContent={[contentParagraphSmall(false), { ...contentApiParameters(), title: "example" }]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("with table", () => (
    <Card
      title="My Card"
      imageSrc="small-book.png"
      // @ts-ignore
      bodyContent={[contentParagraphSmall(false), contentTable(true, undefined)]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("two columns", () => (
    // @ts-ignore
    <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[twoColumnsContent()]} />
  ));

  registry.add("three columns", () => (
    // @ts-ignore
    <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[threeColumnsContent()]} />
  ));
}

function cardContent(bodyContent: DocElementContent, removeImage?: boolean) {
  return {
    type: "Card",
    title: "My Card",
    imageSrc: removeImage ? undefined : "books.jpg",
    bodyContent: bodyContent,
  };
}

function twoColumnsContent() {
  return {
    type: "Columns",
    isPresentation: false,
    slideIdx: 0,
    config: {
      left: {},
      right: {},
    },
    columns: [
      {
        content: [cardContent([contentParagraph(false)])],
      },
      {
        content: [cardContent([contentParagraphSmall(false)])],
      },
    ],
  };
}

function threeColumnsContent() {
  return {
    type: "Columns",
    isPresentation: false,
    slideIdx: 0,
    config: {
      left: {},
      right: {},
    },
    columns: [
      {
        content: [cardContent([contentParagraph(false)])],
      },
      {
        content: [cardContent([contentParagraphSmall(false)], true)],
      },
      {
        content: [cardContent([contentParagraphSmall(false)])],
      },
    ],
  };
}

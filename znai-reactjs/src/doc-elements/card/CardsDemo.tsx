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
import { Card } from "./Card";
import {
  contentApiParameters,
  contentParagraph,
  contentParagraphSmall,
  contentSnippet,
} from "../demo-utils/contentGenerators";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { DocElementContent } from "../default-elements/DocElement";

export function cardsDemo(registry: Registry) {
  registry.add("large image", () => (
    <Card
      title="My Card"
      imageSrc="books.jpg"
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

  registry.add("small image with api parameters", () => (
    <Card
      title="My Card"
      imageSrc="small-book.png"
      // @ts-ignore
      bodyContent={[contentParagraphSmall(false), { ...contentApiParameters(), title: "example" }]}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("two columns", () => (
    // @ts-ignore
    <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[columnsContent()]} />
  ));
}

function cardContent(bodyContent: DocElementContent) {
  return {
    type: "Card",
    title: "My Card",
    imageSrc: "books.jpg",
    bodyContent: bodyContent,
  };
}

function columnsContent() {
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

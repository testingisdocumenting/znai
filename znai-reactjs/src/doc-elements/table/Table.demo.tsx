/*
 * Copyright 2020 znai maintainers
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

import { elementsLibrary } from "../DefaultElementsLibrary";
import Table from "./Table";
import { Registry } from "react-component-viewer";

export function tableDemo(registry: Registry) {
  registry
    .add("no style", () => (
      <Table
        table={defaultStyle(twoColumnsData())}
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("with title", () => (
      <Table
        table={defaultStyle(twoColumnsData())}
        title="User Data"
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("no header, vertical only", () => (
      <Table
        table={vertLinesOnly(twoColumnsData())}
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("no header, vertical only, no vertical padding", () => (
      <Table
        table={noVertPadding(twoColumnsData())}
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("long inlined code", () => (
      <Table
        table={defaultStyle(dataWithLongInlinedCode())}
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("code snippet", () => (
      <Table
        table={defaultStyle(dataWithCodeSnippet())}
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("forced width", () => (
      <Table
        table={fourColumnsWithWidth(fourColumnsData())}
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("wide mode", () => (
      <Table
        table={{ ...fourColumnsWithWidth(fourColumnsData()), wide: true }}
        title="Wide mode"
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("no style mobile multiple columns", () => (
      <div style={{ width: 300 }}>
        <Table
          table={fourColumns(fourColumnsData())}
          elementsLibrary={elementsLibrary}
        />
      </div>
    ))
    .add("min width mobile multiple columns", () => (
      <div style={{ width: 300 }}>
        <Table
          table={{ ...fourColumns(fourColumnsData()), minColumnWidth: 150 }}
          elementsLibrary={elementsLibrary}
        />
      </div>
    ));
}

function defaultStyle(data: any[][]) {
  return {
    styles: [],
    columns: twoColumns(),
    data: data,
  };
}

function vertLinesOnly(data: any[][]) {
  return {
    styles: ["no-header", "middle-vertical-lines-only"],
    columns: twoColumns(),
    data: data,
  };
}

function noVertPadding(data: any[][]) {
  return {
    styles: ["no-header", "middle-vertical-lines-only", "no-vertical-padding"],
    columns: twoColumns(),
    data: data,
  };
}

function twoColumns() {
  return [
    { title: "Column 1", align: "right", width: "40%" },
    { title: "Column 2" },
  ];
}

function fourColumns(data: any[][]) {
  return {
    columns: [
      { title: "Column__1", align: "right" },
      { title: "Column__2", width: "50%" },
      { title: "Column 3" },
      { title: "Column 4" },
    ],
    data,
  };
}

function fourColumnsWithWidth(data: any[][]) {
  return {
    columns: [
      { title: "Column__1", align: "right" },
      { title: "Column__2", width: "50%" },
      { title: "Column 3", width: 400 },
      { title: "Column 4", width: 600 },
    ],
    data,
  };
}

function twoColumnsData() {
  return [
    [1, 2],
    [3, 4],
    [
      "hello",
      [
        {
          text: "We saw (todo link) how you can annotate images using ",
          type: "SimpleText",
        },
        {
          code: "include-image",
          type: "InlinedCode",
        },
        {
          text: " plugin.",
          type: "SimpleText",
        },
        {
          type: "SoftLineBreak",
        },
        {
          text:
            "Now let\u0027s automate the screenshot and annotations assigning process.",
          type: "SimpleText",
        },
      ],
    ],
  ];
}

function fourColumnsData() {
  return [
    [1, 2, 3, 5],
    [5, 6, 7, 8],
    [
      "hello",
      [
        {
          text: "Wesaw(todolink)howyoucanannotate images using ",
          type: "SimpleText",
        },
      ],
      "hello",
      [
        {
          text: "We saw (todo link) how you can annotate images using ",
          type: "SimpleText",
        },
      ],
    ],
  ];
}

function dataWithLongInlinedCode() {
  return [
    [1, 2],
    [
      "hello",
      [
        {
          text: "We saw (todo link) how you can annotate images using ",
          type: "SimpleText",
        },
        {
          code:
            "longAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreAlongAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreAlongAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreAlongAlineAurlAorAsomethingAelseAandAthenAaAbitAmoreA",
          type: "InlinedCode",
        },
      ],
    ],
  ];
}

function dataWithCodeSnippet() {
  return [
    [1, 2],
    [
      "hello",
      [
        {
          snippet:
            "class InternationalPriceService implements PriceService, AnotherInterface {\n" +
            "    private static void main(String... args) {\n" +
            "        ... // code goes here\n" +
            "    } // code stops here\n" +
            "}\n",
          lang: "Java",
          type: "Snippet",
          title: "Snippet in a cell",
        },
      ],
    ],
  ];
}

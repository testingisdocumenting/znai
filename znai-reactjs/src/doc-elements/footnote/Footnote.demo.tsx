/*
 * Copyright 2024 znai maintainers
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
import { FootnoteReference } from "./FootnoteReference";
import { Registry } from "react-component-viewer";
import { TooltipRenderer } from "../../components/Tooltip";
import { FootnotePreview } from "./FootnotePreview";

export function footnoteDemo(registry: Registry) {
  registry.add("default", () => (
    <>
      <TooltipRenderer />
      <div className="content-block">
        <p>
          hello world some longer text and even further apart
          <FootnoteReference label="1" elementsLibrary={elementsLibrary} content={footnoteContent()} />
        </p>
      </div>
    </>
  ));

  registry.add("longer label", () => (
    <>
      <TooltipRenderer />
      <div className="content-block">
        <p>
          hello world some longer text and even further apart and some more
          <FootnoteReference label="23" elementsLibrary={elementsLibrary} content={longerFootnoteContent()} />
        </p>
      </div>
    </>
  ));

  registry.add("preview", () => {
    return (
      <div className="znai-footnote-preview-container">
        <FootnotePreview label="1" elementsLibrary={elementsLibrary} content={footnoteContent()} />
      </div>
    );
  });
}

function footnoteContent() {
  return [
    {
      type: "Paragraph",
      content: [
        {
          text: "experimentational footnote",
          type: "SimpleText",
        },
      ],
    },
  ];
}

function longerFootnoteContent() {
  return [
    {
      type: "Paragraph",
      content: [
        {
          text: "experimentational footnote",
          type: "SimpleText",
        },
        {
          type: "SoftLineBreak",
        },
        {
          text: "with multiple text",
          type: "SimpleText",
        },
        {
          type: "SoftLineBreak",
        },
        {
          text: "and more",
          type: "SimpleText",
        },
      ],
    },
    {
      lang: "",
      snippet: "var a = 2\n",
      lineNumber: "",
      type: "Snippet",
    },
  ];
}

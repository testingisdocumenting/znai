/*
 * Copyright 2026 znai maintainers
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
import { elementsLibrary } from "../DefaultElementsLibrary";
import { Checkboxes } from "./Checkboxes";

export function checkboxesDemo(registry: Registry) {
  registry
    .add("one liners", () => (
      <Checkboxes elementsLibrary={elementsLibrary} blockId="demo-one-liners" checkboxItems={oneLiners()} />
    ))
    .add("long wrapped text", () => (
      <Checkboxes elementsLibrary={elementsLibrary} blockId="demo-long-text" checkboxItems={longText()} />
    ))
    .add("with code snippet", () => (
      <Checkboxes elementsLibrary={elementsLibrary} blockId="demo-with-snippet" checkboxItems={withSnippet()} />
    ));
}

function oneLiners() {
  return [
    {
      id: "install-cli",
      content: [paragraph("install cli")],
    },
    {
      id: "setup-environment",
      content: [paragraph("setup environment")],
    },
    {
      id: "run-first-example",
      content: [paragraph("run first example")],
    },
  ];
}

function longText() {
  const longLine =
    "review the deployment guide and make sure all the required services are provisioned, " +
    "credentials are stored in the secrets manager, and the monitoring dashboards are configured " +
    "for every environment including staging and production";

  return [
    {
      id: "review-deployment-guide",
      content: [paragraph(longLine)],
    },
    {
      id: "short-follow-up",
      content: [paragraph("short follow up")],
    },
  ];
}

function withSnippet() {
  return [
    {
      id: "install-dependencies",
      content: [
        paragraph("install dependencies using package manager"),
        {
          type: "Snippet",
          lang: "bash",
          snippet: "npm install --save-dev my-tool\nnpm run my-tool -- --init\n",
        },
      ],
    },
    {
      id: "validate-config",
      content: [
        paragraph("validate generated config"),
        {
          type: "Snippet",
          lang: "javascript",
          snippet: 'module.exports = {\n  preset: "default",\n  output: "./build",\n};\n',
        },
      ],
    },
    {
      id: "commit-changes",
      content: [paragraph("commit changes")],
    },
  ];
}

function paragraph(text: string) {
  return {
    type: "Paragraph",
    content: [
      {
        text,
        type: "SimpleText",
      },
    ],
  };
}

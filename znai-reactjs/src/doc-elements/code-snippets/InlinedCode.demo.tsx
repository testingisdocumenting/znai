/*
 * Copyright 2021 znai maintainers
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

import * as React from "react";

import { InlinedCode } from "./InlinedCode";
import Link from "../default-elements/Link";
import { elementsLibrary } from "../DefaultElementsLibrary";
import type { Registry } from "react-component-viewer";
import type { DocElementContent } from "../default-elements/DocElement";

export function inlinedCodeDemo(registry: Registry) {
  registry
    .add("regular", () => <InlinedCode code="ClassName" />)
    .add("no break", () => (
      <div style={{ width: 100 }}>
        <InlinedCode code="my class name of something" />
      </div>
    ))
    .add("with global doc ref", () => <InlinedCode code="package.SuperClass" />)
    .add("with local doc ref", () => (
      <InlinedCode
        code="another.SuperClass"
        references={{
          "another.SuperClass": {
            pageUrl: "#another-url",
          },
        }}
      />
    ))
    .add("inside link tet", () => (
      <Link
        url="#url"
        isFile={false}
        content={linkContent() as DocElementContent}
        elementsLibrary={elementsLibrary}
      />
    ));
}

function linkContent() {
  return [
    {
      type: "SimpleText",
      text: "text ",
    },
    {
      type: "InlinedCode",
      code: "superCode",
    },
    {
      type: "SimpleText",
      text: " text ",
    },
  ];
}

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

import { codeWithMethodCalls, contentParagraph } from "../demo-utils/contentGenerators";

export function snippetsResultOutputDemo(registry) {
  registry.add("result output no gap", () => (
    <elementsLibrary.DocElement
      elementsLibrary={elementsLibrary}
      content={[
        contentParagraph(false),
        compactContentSnippet(codeWithMethodCalls(), { noGap: true, noGapBorder: true }),
        compactContentSnippet("hello world\noutput", { resultOutput: true }),
        contentParagraph(false),
      ]}
    />
  ));
}

function compactContentSnippet(content, { noGap, noGapBorder, resultOutput } = {}) {
  return {
    type: "Snippet",
    lang: "java",
    noGap: noGap,
    resultOutput,
    noGapBorder,
    snippet: content,
  };
}

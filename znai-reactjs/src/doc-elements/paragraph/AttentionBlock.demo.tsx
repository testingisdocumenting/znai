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
import { AttentionBlock } from "./AttentionBlock";
import { elementsLibrary } from "../DefaultElementsLibrary";

const paragraph = { type: "Paragraph", content: [{ text: "first name", type: "SimpleText" }] };
const code = { type: "Snippet", snippet: "println 'hello world'" };

const multipleParagraph = [paragraph, paragraph, paragraph];
const multipleParagraphWithCode = [paragraph, code, paragraph];

export function attentionBlockDemo(registry: Registry) {
  registry.add("notes without label", () => (
    <AttentionBlock
      attentionType="note"
      iconTooltip="Note"
      content={multipleParagraph}
      elementsLibrary={elementsLibrary}
    />
  ));

  registry.add("notes with label", () => (
    <AttentionBlock attentionType="note" label="Note" content={multipleParagraph} elementsLibrary={elementsLibrary} />
  ));

  registry.add("question without label", () => (
    <AttentionBlock
      attentionType="question"
      iconTooltip="Question"
      content={multipleParagraphWithCode}
      elementsLibrary={elementsLibrary}
    />
  ));
}

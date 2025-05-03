/*
 * Copyright 2025 znai maintainers
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

import { Registry } from "react-component-viewer";
import * as React from "react";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { contentParagraph } from "../demo-utils/contentGenerators";
import { ReadMore } from "./ReadMore";

export function readMoreDemo(registry: Registry) {
  registry.add("regular text", () =>
    surroundWithText(
      elementsLibrary,
      <ReadMore title="Press to reveal" content={[contentParagraph(false)]} elementsLibrary={elementsLibrary} />
    )
  );
}

function surroundWithText(elementsLibrary: any, rendered: any) {
  const DocElement = elementsLibrary.DocElement;
  return (
    <React.Fragment>
      <DocElement content={[contentParagraph(false)]} elementsLibrary={elementsLibrary} />
      {rendered}
      <DocElement content={[contentParagraph(false)]} elementsLibrary={elementsLibrary} />
    </React.Fragment>
  );
}

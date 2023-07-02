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

import { Snippet } from "./Snippet";
import { TwoSidesLayoutRightPart } from "../page/two-sides/TwoSidesLayout";
import { TooltipRenderer } from "../../components/Tooltip";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { Registry } from "react-component-viewer";

export function snippetsWithInlineCommentsDemo(registry: Registry) {
  registry
    .add("with bullet points from comments java", () => (
      <>
        <TooltipRenderer />
        <Snippet
          wide={false}
          lang="java"
          snippet={javaWithComments()}
          commentsType="inline"
          elementsLibrary={elementsLibrary}
        />
      </>
    ))
    .add("with bullet points but no option", () => (
      <>
        <Snippet wide={false} lang="java" snippet={javaWithComments()} elementsLibrary={elementsLibrary} />
      </>
    ))
    .add("with bullet points from callouts props java", () => (
      <>
        <Snippet
          wide={false}
          lang="java"
          snippet={javaNoComments()}
          callouts={{
            2: [
              { type: "SimpleText", text: "some code goes here " },
              { type: "InlinedCode", code: "test-code" },
            ],
          }}
          elementsLibrary={elementsLibrary}
        />
      </>
    ))
    .add("with multiline bullet points java", () => (
      <Snippet
        wide={false}
        lang="java"
        snippet={javaCodeWithMultilineComments()}
        elementsLibrary={elementsLibrary}
        commentsType="inline"
      />
    ))
    .add("with bullet points python", () => (
      <Snippet
        wide={false}
        lang="python"
        snippet={pythonCodeWithComments()}
        commentsType="inline"
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("with bullet points python comments above", () => (
      <Snippet
        wide={false}
        lang="python"
        snippet={pythonCodeWithCommentsAbove()}
        commentsType="inline"
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("with spoiler bullet points", () => (
      <Snippet
        wide={false}
        lang="java"
        snippet={javaWithComments()}
        spoiler={true}
        commentsType="inline"
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("wide with bullet points", () => (
      <Snippet wide={true} lang="java" snippet={wideCode()} commentsType="inline" elementsLibrary={elementsLibrary} />
    ))
    .add("wrap with bullet points", () => (
      <Snippet wrap={true} lang="java" snippet={wideCode()} commentsType="inline" elementsLibrary={elementsLibrary} />
    ))
    .add("wide with bullet points right side background", () => (
      <TwoSidesLayoutRightPart>
        <Snippet wide={true} lang="java" snippet={wideCode()} commentsType="inline" elementsLibrary={elementsLibrary} />
      </TwoSidesLayoutRightPart>
    ))
    .add("wide with spoiler bullet points", () => (
      <Snippet
        wide={true}
        spoiler={true}
        lang="java"
        snippet={wideCode()}
        commentsType="inline"
        elementsLibrary={elementsLibrary}
      />
    ))
    .add("with empty bullet points", () => (
      <Snippet lang="java" snippet={codeWithoutComments()} commentsType="inline" elementsLibrary={elementsLibrary} />
    ));
}

function javaWithComments() {
  return (
    "class InternationalPriceService implements PriceService {\n" +
    "    private static void main(String... args) {\n" +
    "        ... // some code goes here\n" +
    "    } // another code stops here\n" +
    "}\n"
  );
}

function javaNoComments() {
  return (
    "class InternationalPriceService implements PriceService {\n" +
    "    private static void main(String... args) {\n" +
    "        ...\n" +
    "    }\n" +
    "}\n"
  );
}

function wideCode() {
  return (
    "class InternationalPriceService implements PriceService {\n" +
    "    private static void LongJavaInterfaceNameWithSuperFactory createMegaAbstractFactory(final ExchangeCalendarLongerThanLife calendar) { // this one is long wow many text goes here still yeah\n" +
    "        ... // code goes here\n" +
    "    } // code stops here\n" +
    "}\n"
  );
}

function javaCodeWithMultilineComments() {
  return (
    "class InternationalPriceService implements PriceService {\n" +
    "    private static void main(String... args) {\n" +
    "        ... // multiline comment multi line comment multiline comment multi line comment multiline comment " +
    "multi line comment multiline comment multi line comment \n" +
    "    } // Code stops here code stops here code stops here code" +
    " stops here code stops here code stops here code stops here code stops here code stops here \n" +
    "}\n"
  );
}

function pythonCodeWithComments() {
  return `import market

def method:
    print("hello") # hello message
`;
}

function pythonCodeWithCommentsAbove() {
  return `def method:
    # hello message
    # on multiple lines
    print("hello")
`;
}

function codeWithoutComments() {
  return (
    "class InternationalPriceService implements PriceService {\n" +
    "    private static void main(String... args) {\n" +
    "        ...\n" +
    "    }\n" +
    "}\n"
  );
}

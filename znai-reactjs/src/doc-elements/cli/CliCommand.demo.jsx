/*
 * Copyright 2021 znai maintainers
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

import { CliCommand } from "./CliCommand";
import { Paragraph } from "../paragraph/Paragraph";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { contentParagraph } from "../demo-utils/contentGenerators";

const shortCommand = "git meta push123";

const longCommand = "git meta push123 origin HEAD:myfeature/pushrequest some more lines even --more " +
  "--some-long-options --another-longer-option";

export function cliCommandDemo(registry) {
  registry
    .add("multiple short commands surrounded with text", () => (
      <>
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={[
          contentParagraph(false),
          cliContent({command: shortCommand, paramsToHighlight: ["push"], isPresentation: false}),
          cliContent({command: shortCommand, paramsToHighlight: ["push"], isPresentation: false}),
          contentParagraph(false),
        ]}/>
      </>
    ))
    .add("multiple long commands surrounded with text", () => (
      <>
        <ParagraphText />
        <CliCommand command={longCommand}
                    paramsToHighlight={["push"]}
                    isPresentation={false} />
        <CliCommand command={longCommand}
                    paramsToHighlight={["push"]}
                    isPresentation={false} />
        <ParagraphText />
      </>
    ))
    .add("with colons", () => <CliCommand command={longCommand}
                                          paramsToHighlight={["push"]}
                                          isPresentation={false} />)
    .add("with brackets", () => <CliCommand command="git <param1> <param2>" paramsToHighlight={["push"]}
                                            isPresentation={false} />)
    .add("with threshold", () => <CliCommand command={longCommand}
                                             threshold={50}
                                             paramsToHighlight={["push"]}
                                             isPresentation={false} />)
    .add("with split after", () => <CliCommand
      command={longCommand}
      paramsToHighlight={["push"]}
      splitAfter={["origin", "more"]}
      isPresentation={false} />);
}

function ParagraphText() {
  return (
    <Paragraph content={simpleText()} elementsLibrary={elementsLibrary} />
  );
}

function cliContent(props) {
  return {
    type: "CliCommand",
    ...props
  }
}

function simpleText() {
  return [
    { type: "SimpleText", text: "simple paragraph with text. simple paragraph with text. simple paragraph with text." }
  ];
}

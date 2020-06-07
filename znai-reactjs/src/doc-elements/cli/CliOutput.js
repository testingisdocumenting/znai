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

import React from 'react'

import SnippetContainer from "../code-snippets/SnippetContainer";
import SimpleCodeSnippet from "../code-snippets/SimpleCodeSnippet";

import {TokensPrinter} from "../code-snippets/TokensPrinter";

const CliOutput = ({lines, ...props}) => {
    return (
        <SnippetContainer className="cli-output"
                          linesOfCode={renderLines(lines)}
                          snippetComponent={SimpleCodeSnippet}
                          {...props}/>
    )

    function renderLines(lines) {
        const printer = new TokensPrinter()
        lines.forEach((line) => {
            printer.print("cli-output-line", line)
            printer.println()
        })

        return printer.linesOfTokens
    }
}

const presentationCliOutput = {component: CliOutput,
    numberOfSlides: ({highlight}) => {
        if (highlight) {
            return highlight.length + 1;
        }

        return 1
    }
}

export {CliOutput, presentationCliOutput}

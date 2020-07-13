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

import {convertAnsiToTokenLines} from "./ansiToTokensConverter";

import SimpleCodeSnippet from "../code-snippets/SimpleCodeSnippet";

import {isAllAtOnce} from "../meta/meta";

import './CliOutput.css';

const CliOutput = ({lines, ...props}) => {
    const linesOfTokens = convertAnsiToTokenLines(lines);

    return (
        <SnippetContainer className="cli-output"
                          linesOfCode={linesOfTokens}
                          snippetComponent={SimpleCodeSnippet}
                          numberOfVisibleLines={100}
                          {...props}/>
    )
}

const presentationCliOutput = {component: CliOutput,
    numberOfSlides: (props) => {
        return 1 + highlightNumberOfSlides(props) + (props.revealLineStop || []).length;
    }
}

function highlightNumberOfSlides({meta, highlight}) {
    highlight = highlight || []
    if (isAllAtOnce(meta) && highlight.length > 0) {
        return 1
    }

    return highlight.length
}


export {CliOutput, presentationCliOutput}

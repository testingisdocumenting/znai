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

import {convertAnsiToTokenLines} from "./ansiToTokensConverter";

import './CliOutput.css';

const CliOutput = ({lines, lineStop, highlight, slideIdx, ...props}) => {
    const isPresentation = slideIdx !== undefined;
    const linesOfTokens = convertAnsiToTokenLines(lines);

    return (
        <SnippetContainer className="cli-output"
                          linesOfCode={reduceLinesForPresentation()}
                          snippetComponent={SimpleCodeSnippet}
                          highlight={reduceHighlightForPresentation()}
                          slideIdx={reduceSnippetContainerSlideIdx()}
                          {...props}/>
    )

    function reduceLinesForPresentation() {
        if (!lineStop || !isPresentation) {
            return linesOfTokens;
        }

        if (slideIdx >= lineStop.length) {
            return linesOfTokens;
        }

        return linesOfTokens.slice(0, lineStop[slideIdx] + 1);
    }

    // slides show first reveal of output
    // then highlight one line at a time
    // so we need to delay highlight we pass to SnippetContainer
    //
    function reduceHighlightForPresentation() {
        if (!lineStop || !isPresentation) {
            return highlight;
        }

        if (slideIdx < lineStop.length) {
            return []
        }

        return highlight;
    }

    function reduceSnippetContainerSlideIdx() {
        if (!lineStop) {
            return slideIdx;
        }

        return slideIdx - lineStop.length;
    }
}

const presentationCliOutput = {component: CliOutput,
    numberOfSlides: ({highlight, lineStop}) => {
        let numberOfSlides = 1;
        if (highlight) {
            numberOfSlides += highlight.length;
        }

        if (lineStop) {
            numberOfSlides += lineStop.length;
        }

        return numberOfSlides;
    }
}

export {CliOutput, presentationCliOutput}

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

import React, {Component} from 'react'

import {extractTextFromTokens, splitTokensIntoLines} from './codeUtils'
import LineOfTokens from './LineOfTokens.js'
import {convertToList} from '../propsUtils'
import {isAllAtOnce} from '../meta/meta';
import {mergeWithGlobalDocReferences} from '../references/globalDocReferences'

import {repeatChar} from "../../utils/strings";

import './tokens.css'
import './SimpleCodeSnippet.css'

class SimpleCodeSnippet extends Component {
    constructor(props) {
        super(props)

        this.processProps(props)
        this.state = {displayFully: !this.limitLines(props)}
    }

    // handles changes during preview
    componentWillReceiveProps(nextProps) {
        this.processProps(nextProps)

        this.setState({displayFully: !this.limitLines(nextProps)})
    }

    processProps({tokens, linesOfCode, highlight}) {
        this.linesOfTokens = !linesOfCode ? splitTokensIntoLines(tokens) : linesOfCode

        // highlight is either a single line index/substring or a collection of line indexes and substrings
        this.highlight = convertToList(highlight)
    }

    render() {
        const {displayFully} = this.state
        const {wrap, isPresentation, slideIdx, references} = this.props

        // slideIdx === 0 means no highlights, 1 - first highlight, etc
        const highlightIsVisible = !isPresentation || slideIdx > 0

        const visibleLines = this.limitLines && !displayFully && !isPresentation ?
            this.linesOfTokens.slice(0, this.readMoreVisibleLines(this.props)) :
            this.linesOfTokens

        const linesToRender = this.processLinesToRender(visibleLines)

        const mergedReferences = mergeWithGlobalDocReferences(references)

        return (
            <pre>
                {linesToRender.map((tokens, lineIdx) => (
                    <LineOfTokens key={lineIdx} tokens={tokens}
                                  references={mergedReferences}
                                  wrap={wrap}
                                  isHighlighted={highlightIsVisible && this.isHighlighted(lineIdx)}
                                  isPresentation={isPresentation}/>
                ))}
                <React.Fragment>{this.renderReadMore()}</React.Fragment>
            </pre>
        )
    }

    renderReadMore() {
        const {displayFully} = this.state
        const {isPresentation} = this.props

        if (displayFully || isPresentation) {
            return null
        }

        return (
            <div className="code-snippet-read-more" onClick={this.onReadMoreClick}>Read more...</div>
        )
    }

    // hides lines for presentation mode to reveal them later
    processLinesToRender(lines) {
        const {isPresentation, slideIdx, revealLineStop} = this.props

        if (!isPresentation) {
            return lines
        }

        if (!revealLineStop || slideIdx >= revealLineStop.length) {
            return lines
        }

        const maxLineWidth = lines.reduce((max, line) => Math.max(max, extractTextFromTokens(line).length), 0)
        const emptyLine = repeatChar(maxLineWidth - 1 /* new line symbol */, ' ')

        const upToIdx = revealLineStop[slideIdx]
        return lines.map((line, idx) => idx <= upToIdx ? line : [emptyLine])
    }

    onReadMoreClick = () => {
        this.setState({displayFully: true})
    }

    limitLines(props) {
        return this.linesOfTokens.length >= this.readMoreVisibleLines(props) && props.readMore
    }

    readMoreVisibleLines(props) {
        return props.readMoreVisibleLines || 8
    }

    isHighlighted(lineIdx) {
        const {meta, isPresentation, slideIdx, revealLineStop} = this.props

        const highlightSliceIdx = calcHighlightSliceIdx(this.highlight)

        const highlight = !isPresentation ?
            this.highlight:
            this.highlight.slice(0, highlightSliceIdx)

        if (!highlight) {
            return false
        }

        return highlight.indexOf(lineIdx) !== -1

        function calcHighlightSliceIdx(originalHighlight) {
            const allAtOnce = isAllAtOnce(meta)

            if (!revealLineStop || revealLineStop.length === 0) {
                return allAtOnce ? originalHighlight.length : slideIdx
            }

            if (slideIdx <= revealLineStop.length) {
                return 0
            }

            return allAtOnce ?
                originalHighlight.length:
                slideIdx - revealLineStop.length
        }
    }
}

export default SimpleCodeSnippet

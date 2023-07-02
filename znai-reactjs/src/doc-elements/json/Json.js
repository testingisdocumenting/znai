/*
 * Copyright 2023 znai maintainers
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

import {printJson} from './jsonPrinter'

import SnippetContainer from '../code-snippets/SnippetContainer'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import './Json.css'
import CodeSnippetWithCallouts from "../code-snippets/CodeSnippetWithCallouts";
import { SnippetBulletExplanations } from "../code-snippets/explanations/SnippetBulletExplanations";

class Json extends React.Component {
    state = {
        previouslyCollapsedPaths: []
    }

    render() {
        const {previouslyCollapsedPaths} = this.state
        const {data, highlightValues, highlightKeys, title, calloutsByPath, ...props} = this.props

        const {linesOfTokens, lineIdxByPath} = printJson({
            rootPath: 'root',
            data,
            highlightValues,
            highlightKeys,
            collapsedPaths: this.collapsedPaths,
            previouslyCollapsedPaths,
            onPathUncollapse: this.onPathUncollapse,
            onPathCollapse: this.onPathCollapse })

        const calloutsByLineIdx = {}
        if (calloutsByPath) {
            Object.entries(calloutsByPath).forEach(e => {
                calloutsByLineIdx[lineIdxByPath[e[0]]] = e[1];
            });
        }

        const snippetComponent = Object.keys(calloutsByLineIdx).length > 0 ?
          CodeSnippetWithCallouts :
          SimpleCodeSnippet

        return (
          <>
              <SnippetContainer linesOfCode={linesOfTokens}
                                title={title}
                                tokensForClipboardProvider={this.tokensForClipboardProvider}
                                numberOfVisibleLines={100}
                                snippetComponent={snippetComponent}
                                callouts={calloutsByLineIdx}
                                {...props}/>
              <Explanations calloutsByLineIdx={calloutsByLineIdx}
                            elementsLibrary={props.elementsLibrary}
                            spoiler={false}
                            isPresentation={props.isPresentation}/>
          </>
        )
    }

    get collapsedPaths() {
        const {previouslyCollapsedPaths} = this.state
        const collapsedPaths = this.props.collapsedPaths
        if (!collapsedPaths) {
            return []
        }

        return collapsedPaths.filter(p => previouslyCollapsedPaths.indexOf(p) === -1)
    }

    onPathUncollapse = (path) => {
        this.setState(prev => {
            return {
                previouslyCollapsedPaths: [...prev.previouslyCollapsedPaths, path]
            }
        })
    }

    onPathCollapse = (path) => {
        this.setState(prev => {
            return {
                previouslyCollapsedPaths: prev.previouslyCollapsedPaths.filter(p => p !== path),
            }
        })
    }

    tokensForClipboardProvider = () => {
        const {data} = this.props
        const {linesOfTokens} = printJson({rootPath: 'root', data})

        return linesOfTokens.reduce((acc, curr) => acc.concat(curr), [])
    }
}

function Explanations({spoiler, isPresentation, calloutsByLineIdx = {}, elementsLibrary}) {
    if (isPresentation || Object.keys(calloutsByLineIdx).length === 0) {
        return null
    }

    return <SnippetBulletExplanations spoiler={spoiler}
                                      callouts={calloutsByLineIdx}
                                      elementsLibrary={elementsLibrary}/>
}

export default Json
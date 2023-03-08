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

class Json extends React.Component {
    state = {
        previouslyCollapsedPaths: []
    }

    render() {
        const {previouslyCollapsedPaths} = this.state
        const {data, paths, title, ...props} = this.props

        const lines = printJson({
            rootPath: 'root',
            data,
            paths,
            collapsedPaths: this.collapsedPaths,
            previouslyCollapsedPaths,
            onPathUncollapse: this.onPathUncollapse,
            onPathCollapse: this.onPathCollapse })

        return (
            <SnippetContainer linesOfCode={lines}
                              title={title}
                              tokensForClipboardProvider={this.tokensForClipboardProvider}
                              numberOfVisibleLines={100}
                              snippetComponent={SimpleCodeSnippet}
                              {...props}/>
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
        const lines = printJson({rootPath: 'root', data})

        return lines.reduce((acc, curr) => acc.concat(curr), [])
    }
}

export default Json
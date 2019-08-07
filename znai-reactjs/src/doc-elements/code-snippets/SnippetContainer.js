/*
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

import * as React from 'react'

import * as ClipboardJS from 'clipboard'

import {extractTextFromTokens} from './codeUtils'
import CopyIcon from './SnippetCopyIcon'

import './SnippetContainer.css'

class SnippetContainer extends React.Component {
    state = { displayCopied: false }

    render() {
        return this.props.wide ?
            this.renderWideMode() : this.renderNormalMode()
    }

    renderNormalMode() {
        const {title} = this.props

        return (
            <div className="snippet-container content-block">
                {this.renderTitle(title)}
                {this.renderSnippet()}
            </div>
        )
    }

    renderWideMode() {
        const {title} = this.props

        const wideModePadding = <div className="padding"/>

        const className = "snippet-container wide-screen" + (title ? " with-title" : "")
        return (
            <div className={className}>
                {wideModePadding}
                {title && <div className="title-layer">
                    {this.renderTitle(title)}
                </div>}

                {wideModePadding}

                {this.renderSnippet()}
            </div>
        )
    }

    renderTitle(title) {
        if (!title) {
            return null
        }

        return (
            <div className="title-container content-block">
                <div className="title">{title}</div>
            </div>
        )
    }

    renderSnippet() {
        return (
            <div className={this.snippetClassName}>
                <this.props.snippetComponent {...this.props}/>
                {this.renderCopyToClipboard()}
            </div>
        )
    }

    renderCopyToClipboard() {
        const {displayCopied} = this.state

        const className = 'snippet-copy-to-clipboard ' + (displayCopied ? 'copied': 'copy')

        return (
            <div className={className}
                 ref={this.saveCopyToClipboardNode}>
                <CopyIcon/>
            </div>
        )
    }

    get snippetClassName() {
        const {title} = this.props
        return "snippet" + (title ? " with-title" : "")
    }

    saveCopyToClipboardNode = (node) => {
        this.copyToClipboardNode = node
    }

    componentDidMount() {
        this.setupClipboard()
    }

    componentWillUnmount() {
        this.clearTimer()
        this.destroyClipboard()
    }

    setupClipboard() {
        if (! this.copyToClipboardNode) {
            return
        }

        this.clipboard = new ClipboardJS(this.copyToClipboardNode, {
            text: () => {
                const {tokens, linesOfCode, tokensForClipboardProvider} = this.props
                this.setState({displayCopied: true})
                this.startRemoveFeedbackTimer()

                return extractTextFromTokens(tokensToUse())

                function tokensToUse() {
                    if (tokensForClipboardProvider) {
                        return tokensForClipboardProvider()
                    }

                    if (tokens) {
                        return tokens
                    }

                    return linesOfCode.reduce((acc, curr) => acc.concat(curr), [])
                }
            }
        })
    }

    destroyClipboard() {
        if (this.clipboard) {
            this.clipboard.destroy()
        }
    }

    startRemoveFeedbackTimer() {
        this.removeFeedbackTimer = setTimeout(() => {
            this.setState({displayCopied: false})
        }, 200)
    }

    clearTimer() {
        if (this.removeFeedbackTimer) {
            clearTimeout(this.removeFeedbackTimer)
        }
    }
}

export default SnippetContainer

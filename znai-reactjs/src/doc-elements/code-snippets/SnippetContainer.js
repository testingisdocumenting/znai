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

import * as React from "react";

import * as ClipboardJS from "clipboard";

import { extractTextFromTokens } from "./codeUtils";

import { Icon } from "../icons/Icon";
import { SnippetOptionallyScrollablePart } from "./SnippetOptionallyScrollablePart";

import { ContainerTitle } from "../title/ContainerTitle";

import "./SnippetContainer.css";

class SnippetContainer extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            displayCopied: false,
            collapsed: this.props.collapsed
        }
    }

    render() {
        const {wide, isPresentation} = this.props
        const renderWide = wide && !isPresentation

        return renderWide ?
            this.renderWideMode() : this.renderNormalMode()
    }

    renderNormalMode() {
        const {title, className, compact, next, prev} = this.props

        const fullClassName = "snippet-container content-block"
          + (className ? " " + className : "")
          + (noMargin(next) ? " no-margin-bottom" : "")
          + (noMargin(prev) ? " no-margin-top" : "")

        return (
            <div className={fullClassName}>
                {this.renderTitle(title)}
                {this.renderSnippet()}
            </div>
        )

        function noMargin(nextPrev) {
            return compact && nextPrev && nextPrev.type === "Snippet" && !!nextPrev.title && !!nextPrev.compact
        }
    }

    renderWideMode() {
        const {title, className} = this.props

        const wideModePadding = <div className="padding"/>

        const fullClassName = "snippet-container wide-screen" +
            (title ? " with-title" : "") +
            (className ? " " + className : "")

        return (
            <div className={fullClassName}>
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
            return null;
        }

        const anchorId = this.props.anchorId

        const { collapsed } = this.state;

        return (
          <ContainerTitle title={title}
                          anchorId={anchorId}
                          collapsed={collapsed}
                          additionalTitleClassNames="znai-snippet-container-title"
                          onCollapseToggle={this.collapseToggle}/>
        )
    }

     collapseToggle = () => {
        this.setState(prev => ({collapsed: !prev.collapsed}))
    }

    renderSnippet() {
        return (
            <div className={this.snippetClassName}>
                <SnippetOptionallyScrollablePart{...this.props}/>
                {this.renderCopyToClipboard()}
            </div>
        );
    }

    renderCopyToClipboard() {
        const {isPresentation} = this.props
        const {displayCopied} = this.state

        if (isPresentation) {
            return null
        }

        const className = 'snippet-copy-to-clipboard ' + (displayCopied ? 'copied': 'copy')

        return (
            <div className={className} ref={this.saveCopyToClipboardNode}>
                <Icon id="copy"/>
            </div>
        )
    }

    get snippetClassName() {
        const {title} = this.props
        const {collapsed} = this.state
        return "snippet"
          + (title ? " with-title" : "")
          + (collapsed ? " collapsed" : "")
    }

    saveCopyToClipboardNode = (node) => {
        this.copyToClipboardNode = node
    }

    componentDidMount() {
        this.setupClipboard()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (prevProps.collapsed !== this.props.collapsed) {
            this.setState({ collapsed: this.props.collapsed })
        }
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
                const {linesOfCode, tokensForClipboardProvider} = this.props
                this.setState({displayCopied: true})
                this.startRemoveFeedbackTimer()

                return extractTextFromTokens(tokensToUse())

                function tokensToUse() {
                    if (tokensForClipboardProvider) {
                        return tokensForClipboardProvider()
                    }

                    return linesOfCode.reduce((acc, curr) => acc.concat(curr).concat("\n"), [])
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

import * as React from 'react'

import ClipboardJS from 'clipboard'

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
        const {title} = this.props

        const noTitle = !title

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
            text: trigger => {
                const {tokens, linesOfCode, tokensForClipboardProvider} = this.props
                this.setState({displayCopied: true})
                this.startRemoveFeedbackTimer()

                if (tokensForClipboardProvider) {
                    return extractTextFromTokens(tokensForClipboardProvider())
                }

                if (tokens) {
                    return tokens
                }

                return extractTextFromTokens(linesOfCode.reduce((acc, curr) => acc.concat(curr), []))
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

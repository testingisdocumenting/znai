import React, {Component} from 'react'

import {extractTextFromTokens, splitTokensIntoLines} from './codeUtils'
import LineOfTokens from './LineOfTokens.js'
import SimpleCodeToken from './SimpleCodeToken.js'

import './tokens.css'
import './SimpleCodeSnippet.css'
import {convertToList} from '../propsUtils'

class SimpleCodeSnippet extends Component {
    constructor(props) {
        super(props)

        this.processProps(props)
        this.state = {displayFully: !this.limitLines}
    }

    componentWillReceiveProps(nextProps) {
        this.processProps(nextProps)
    }

    processProps({tokens, highlight}) {
        this.linesOfTokens = splitTokensIntoLines(tokens)

        // highlight is either a single line index/substring or a collection of line indexes and substrings
        this.highlight = convertToList(highlight)

        if (this.highlight.length === 0) {
            return
        }

        this.isLineHighlighted = this.linesOfTokens.map((tokens, idx) => this.isHighlighted(idx, tokens))

        if (this.isLineHighlighted.filter(highlighted => highlighted).length === 0) {
            throw new Error('none of the lines matches specified highlights: ' + this.highlight)
        }
    }

    render() {
        const {displayFully} = this.state
        const {isPresentation} = this.props

        const linesToRender = this.limitLines && !displayFully ?
            this.linesOfTokens.slice(0, this.readMoreVisibleLines) :
            this.linesOfTokens

        return (
            <div>
                <pre>
                    <code>
                        {linesToRender.map((tokens, idx) => <LineOfTokens key={idx} tokens={tokens}
                                                                          isHighlighted={this.isHighlighted(idx, tokens)}
                                                                          isPresentation={isPresentation}
                                                                          TokenComponent={SimpleCodeToken}/>)}
                    </code>
                    {this.renderReadMore()}
                </pre>
            </div>
        )
    }

    renderReadMore() {
        const {displayFully} = this.state

        if (displayFully) {
            return null
        }

        return (
            <div className="code-snippet-read-more"
                 onClick={this.onReadMoreClick}>
                Read more...
            </div>
        )
    }

    onReadMoreClick = () => {
        this.setState({displayFully: true})
    }

    get limitLines() {
        return this.linesOfTokens.length >= this.readMoreVisibleLines && this.props.readMore
    }

    get readMoreVisibleLines() {
        return this.props.readMoreVisibleLines || 8
    }

    isHighlighted(idx, tokens) {
        const highlight = this.highlight

        if (! highlight) {
            return false
        }

        if (highlight.indexOf(idx) !== -1) {
            return true
        }

        const text = extractTextFromTokens(tokens)
        const matches = highlight.filter(idxOrText => {
            if (typeof idxOrText === 'number') {
                return false
            }

            return text.indexOf(idxOrText) !== -1
        })

        return matches.length > 0
    }
}

export default SimpleCodeSnippet

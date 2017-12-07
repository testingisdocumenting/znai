import React, {Component} from 'react'

import {splitTokensIntoLines} from './codeUtils'
import LineOfTokens from './LineOfTokens.js'
import SimpleCodeToken from './SimpleCodeToken.js'

import './tokens.css'
import './SimpleCodeSnippet.css'

class SimpleCodeSnippet extends Component {
    constructor(props) {
        super(props)

        const {tokens} = this.props

        this.lines = splitTokensIntoLines(tokens)
        this.state = {displayFully: !this.limitLines}
    }

    componentWillReceiveProps(nextProps) {
        this.lines = splitTokensIntoLines(nextProps.tokens)
    }

    render() {
        const {displayFully} = this.state
        const {isPresentation} = this.props

        const linesToRender = this.limitLines && !displayFully ?
            this.lines.slice(0, this.readMoreVisibleLines) :
            this.lines

        return (
            <div>
                <pre>
                    <code>
                    {linesToRender.map((line, idx) => <LineOfTokens key={idx} line={line}
                                                                    isHighlighted={this.isHighlighted(idx)}
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
        return this.lines.length >= this.readMoreVisibleLines && this.props.readMore
    }

    get readMoreVisibleLines() {
        return this.props.readMoreVisibleLines || 8
    }

    isHighlighted(idx) {
        const {highlight} = this.props
        return highlight && highlight.indexOf(idx) !== -1
    }
}

export default SimpleCodeSnippet

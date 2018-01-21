import React, {Component} from "react"

import CliCommandToken from "./CliCommandToken"
import {splitParts} from "../../utils/strings"
import "./CliCommand.css"

class CliCommand extends Component {
    constructor(props) {
        super(props)

        this.updateTokens(props.command)
        this.state = this.initialState(props)
    }

    initialState(props) {
        const {isPresentation} = props
        return {lastTokenIdx: isPresentation ? 1 : this.tokens.length}
    }

    componentWillReceiveProps(nextProps) {
        this.updateTokens(nextProps.command)

        if (nextProps.command !== this.props.command) {
            this.setState(this.initialState(nextProps))
        }
    }

    render() {
        const {command} = this.props

        return (
            <div key={command} className="cli-command content-block">
                <pre>
                    <span className="prompt">$ </span>
                    <span key={command}>
                        {this.renderTokens()}
                    </span>
                </pre>
            </div>
        )
    }

    renderTokens() {
        const {paramsToHighlight, isPresentation} = this.props
        const {lastTokenIdx} = this.state
        const lines = splitParts(this.tokens, t => t.value.length, isPresentation ? 40 : 80)
        let tokenIdx = 0

        // presentation mode centers slides. if width is growing the effect of typing is affected
        return lines.map((line, lineIdx) => {
            const isLineVisible = lastTokenIdx > (tokenIdx + line.length)
            const isLastLine = lineIdx === lines.length - 1

            const renderedLine = line.map(token => {
                const isHidden = lastTokenIdx <= tokenIdx
                const isLast = tokenIdx === this.tokens.length - 1
                const isLastVisible = tokenIdx === lastTokenIdx - 1
                const key = tokenIdx + isHidden

                tokenIdx++

                return <CliCommandToken key={key} {...token}
                                        isHighlighted={isHighlighted(token)}
                                        isCursorVisible={isPresentation &&
                                        ((lastTokenIdx > this.tokens.length && isLast) || isLastVisible)}
                                        isPresentation={isPresentation}
                                        isHidden={isHidden}
                                        onFullReveal={this.revealNextToken}/>
            })

            return (
                <div key={lineIdx} className="tokens-line">{
                    [...renderedLine,
                        !isLastLine && isLineVisible ? <span key="separator" className="line-separator">\</span> : null]
                }</div>
            )
        })

        function isHighlighted(token) {
            return paramsToHighlight && paramsToHighlight.filter(p => token.value.indexOf(p) !== -1).length
        }
    }

    updateTokens(command) {
        this.tokens = tokenize(command)
    }

    revealNextToken = () => {
        setTimeout(() => {
            this.setState((prevState) => {
                return {lastTokenIdx: prevState.lastTokenIdx + 1}
            })
        }, 80 + Math.random() * 50)
    }
}

function tokenize(fullCommand) {
    const [command, ...params] = fullCommand.split(" ")
    return [{type: "command", value: command + " "}, ...params.map(p => {
        return {type: "param", value: p + " "}
    })]
}

const presentationCliCommandHandler = {
    component: CliCommand,
    numberOfSlides: () => 1
}

export {CliCommand, presentationCliCommandHandler}

import React, {Component} from "react";

import CliCommandToken from "./CliCommandToken";
import "./CliCommand.css";

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

    render() {
        const {paramsToHighlight, isPresentation, command} = this.props
        const {lastTokenIdx} = this.state

        // presentation mode centers slides. if width is growing the effect of typing is affected
        return (
            <div key={command} className="cli-command content-block">
                <pre>
                    <span className="prompt">$ </span>
                    <span>
                        {this.tokens.map((token, idx) => {
                            const isHidden = lastTokenIdx <= idx
                            const isLast = idx === this.tokens.length - 1
                            return <CliCommandToken key={idx + isHidden} {...token}
                                                    isHighlighted={isHighlighted(token)}
                                                    isCursorVisible={isPresentation &&
                                                        ((lastTokenIdx > this.tokens.length && isLast) || idx === lastTokenIdx - 1)}
                                                    isPresentation={isPresentation}
                                                    isHidden={isHidden}
                                                    onFullReveal={this.revealNextToken}/>
                        })
                        }
                    </span>
                </pre>
            </div>
        )

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
    return [{type: "command", value: command}, ...params.map((p, idx) => {
        return {type: "param", value: " " + p}
    })]
}

const presentationCliCommandHandler = {
    component: CliCommand,
    numberOfSlides: () => 1
}

export {CliCommand, presentationCliCommandHandler}

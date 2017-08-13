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

        const visibleTokens = this.tokens.slice(0, lastTokenIdx)
        const invisibleTokens = this.tokens.slice(lastTokenIdx, this.tokens.length)

        // split tokens into two groups so the width remains constant
        // presentation mode centers slides. if width is growing the effect of typing is affected
        return (
            <div key={command} className="cli-command content-block">
                <pre>
                    <span className="prompt">$ </span>
                    <span>
                        {visibleTokens.map((token, idx) => <CliCommandToken key={idx} {...token}
                                                                            isHighlighted={isHighlighted(token)}
                                                                            isCursorVisible={isPresentation && idx === visibleTokens.length - 1}
                                                                            isPresentation={isPresentation}
                                                                            isHidden={false}
                                                                            onFullReveal={this.revealNextToken}/>)
                        }
                    </span>
                    <span>{invisibleTokens.map((token, idx) => <CliCommandToken key={idx} {...token}
                                                                                highlight={isHighlighted(token)}
                                                                                isPresentation={isPresentation}
                                                                                isHidden={true}/>)}</span>
                </pre>
            </div>
        )

        function isHighlighted(token) {
            return paramsToHighlight && paramsToHighlight.filter(p => token.value.indexOf(p) !== -1).length
        }
    }

    componentWillReceiveProps(nextProps) {
        this.updateTokens(nextProps.command)
        this.setState(this.initialState(nextProps))
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

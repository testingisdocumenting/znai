import React, {Component} from 'react'
import classNames from 'classnames'

import './CliCommand.css'

const tokenize = (fullCommand) => {
    const [command, ...params] = fullCommand.split(" ")
    return [{type: "command", value: command}, ...params.map(p => {
        return {type: "param", value: p}
    })]
}

class Token extends Component {
    constructor(props) {
        super(props)

        const {isPresentation, value} = props
        this.state = {lastCharIdx: isPresentation ? 0 : value.length}
    }

    render() {
        const {type, value, highlight} = this.props
        const {lastCharIdx} = this.state

        const className = classNames("token " + type, {highlight: highlight})
        return <span className={className}>{value.substr(0, lastCharIdx)}</span>
    }

    componentDidMount() {
        const {isPresentation} = this.props

        if (isPresentation) {
            this.startRevealProcess()
        }
    }

    startRevealProcess() {
        const {value, onFullReveal} = this.props
        const {lastCharIdx} = this.state

        if (lastCharIdx === value.length) {
            onFullReveal()
        }

        if (lastCharIdx > value.length) {
            clearTimeout(this.timer)
            return
        }

        this.timer = setTimeout(() => {
            this.setState((prevState) => {
                return {lastCharIdx: prevState.lastCharIdx + 1}
            })

            this.startRevealProcess()
        }, 15 + Math.random() * 15)
    }
}

class CliCommand extends Component {
    constructor(props) {
        super(props)

        const {isPresentation} = props

        this.updateTokens(props.command)
        this.state = {lastTokenIdx: isPresentation ? 1 : this.tokens.length}
    }

    render() {
        const {paramsToHighlight, isPresentation} = this.props
        const {lastTokenIdx} = this.state

        const tokensToShow = this.tokens.slice(0, lastTokenIdx)

        return (
            <div className="cli-command">
                <pre>
                    <span className="prompt">$ </span>
                    <span>{tokensToShow.map((token, idx) => <Token key={idx} {...token}
                                                                   highlight={isHighlighted(token)}
                                                                   isPresentation={isPresentation}
                                                                   onFullReveal={this.revealNextToken}/>)}</span>
                    {isPresentation ? <span className="cursor">&nbsp;</span> : null}
                </pre>
            </div>
        )

        function isHighlighted(token) {
            return paramsToHighlight && paramsToHighlight.filter(p => token.value.indexOf(p) !== -1).length
        }
    }

    componentWillReceiveProps(nextProps) {
        this.updateTokens(nextProps.command)
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

const presentationCliCommandHandler = {
    component: CliCommand,
    numberOfSlides: () => 1
}

export {CliCommand, presentationCliCommandHandler}

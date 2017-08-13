import React, {Component} from 'react'
import classNames from 'classnames'

class CliCommandToken extends Component {
    constructor(props) {
        super(props)

        this.state = this.initialState(props)
    }

    initialState(props) {
        const {isPresentation, value} = props
        return {lastCharIdx: isPresentation ? 0 : value.length}
    }

    render() {
        const {type, value, isCursorVisible, isHighlighted, isHidden} = this.props
        const {lastCharIdx} = this.state

        const hasHiddenPart = lastCharIdx < value.length

        const hiddenClassName = "invisible"
        const visibleClassName = classNames("token " + type, {highlight: isHighlighted}, {[hiddenClassName]: isHidden})
        const invisibleClassName = visibleClassName + " " + hiddenClassName

        return (
            <span>
                <span className={visibleClassName}>{value.substr(0, lastCharIdx)}</span>
                {isCursorVisible ? <div className="cursor"/> : null}
                {hasHiddenPart ? <span className={invisibleClassName}>{value.substr(lastCharIdx, value.length)}</span> : null}
            </span>
        )
    }

    componentDidMount() {
        const {isPresentation, isHidden} = this.props

        if (isPresentation && !isHidden) {
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

export default CliCommandToken

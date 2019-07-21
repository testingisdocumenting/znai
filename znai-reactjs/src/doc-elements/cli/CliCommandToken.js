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

import React, {Component} from 'react'

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
        const visibleClassName = "token " + type + (isHighlighted ? " highlight": "") +
            (isHidden ? " " + hiddenClassName: "")
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

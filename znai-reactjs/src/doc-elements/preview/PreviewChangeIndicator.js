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

import './PreviewChangeIndicator.css'

class PreviewChangeIndicator extends Component {
    state = {}

    static getDerivedStateFromProps(nextProps, prevState) {
        if (nextProps.targetDom !== prevState.targetDom) {
            return {alreadyScrolled: false, targetDom: nextProps.targetDom}
        }

        return null
    }

    componentDidMount() {
        const {targetDom} = this.state

        if (!elementInViewport(targetDom)) {
            targetDom.scrollIntoView()
        }

        this.removeIndicatorTimer = setTimeout(() => {
            this.props.onIndicatorRemove()
        }, 2000)
    }

    componentWillUnmount() {
        clearTimeout(this.removeIndicatorTimer)
    }

    render() {
        const {targetDom} = this.state

        const boundingRect = targetDom.getBoundingClientRect()
        const indicatorStyle = {
            position: 'absolute',
            top: boundingRect.top,
            left: boundingRect.left,
            height: boundingRect.height,
            width: boundingRect.width}

        return (
            <div className="preview-change-indicator" style={indicatorStyle}/>
        )
    }
}

function elementInViewport(el) {
    const rect = el.getBoundingClientRect()
    return (rect.top >= 0 && rect.top <= window.innerHeight)
}

export default PreviewChangeIndicator
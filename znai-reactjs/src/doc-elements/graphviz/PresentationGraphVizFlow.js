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

import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'

import {expandId} from './gvUtils'

class PresentationGraphVizFlow extends Component {
    render() {
        const {diagram, colors} = this.props

        return <div ref={node => this.node = node}>
            <div className="diagram-area">
                <GraphVizSvg diagram={diagram} colors={colors} idsToDisplay={this.idsToDisplay()} />
            </div>
        </div>
    }

    idsToDisplay() {
        const {slides, slideIdx} = this.props
        const ids = []

        for (let i = 0, len = slides.length; i < Math.min(len, slideIdx + 1); i++) {
            slides[i].ids.forEach((id) => expandId(id).forEach((nid) => ids.push(nid)))
        }

        return ids
    }

    componentDidMount() {
        this.props.onLoad(this.node.offsetWidth, this.node.offsetHeight)
    }
}

export default {component: PresentationGraphVizFlow, numberOfSlides: ({slides}) => slides.length}

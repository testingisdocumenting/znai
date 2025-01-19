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

import GraphVizFlowFullScreen from './GraphVizFlowFullScreen'
import GraphVizFlowAllInfoAtOnce from './GraphVizFlowAllInfoAtOnce'

import './GraphvVizFlow.css'

class GraphVizFlow extends Component {
    constructor(props) {
        super(props)
        this.state = {fullScreen: false}

        this.onExpand = this.onExpand.bind(this)
        this.onExitFullScreen = this.onExitFullScreen.bind(this)
    }

    render() {
        const {fullScreen} = this.state

        return <div className="graphviz-diagram-flow">
            {fullScreen ?
                <GraphVizFlowFullScreen {...this.props} onClose={this.onExitFullScreen}/> :
                <DocumentationGraphVizFlow {...this.props} onExpand={this.onExpand}/> }
        </div>
    }

    onExpand() {
        this.setState({fullScreen: true})
    }

    onExitFullScreen() {
        this.setState({fullScreen: false})
    }
}

export default GraphVizFlow

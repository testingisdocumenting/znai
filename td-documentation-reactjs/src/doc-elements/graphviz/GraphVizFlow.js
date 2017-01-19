import React, { Component } from 'react'

import GraphVizFlowFullScreen from './GraphVizFlowFullScreen'
import GraphVizFlowAllInfoAtOnce from './GraphVizFlowAllInfoAtOnce'

import './GraphvVizFlow.css'

class GraphVizFlow extends Component {
    constructor(props) {
        super(props)
        this.state = {fullScreen: false}
    }

    render() {
        const {fullScreen} = this.state

        return <div className="graphviz-diagram">
            {fullScreen ?
                <GraphVizFlowFullScreen {...this.props}/> :
                <GraphVizFlowAllInfoAtOnce {...this.props}/> }
        </div>
    }
}

export default GraphVizFlow

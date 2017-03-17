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

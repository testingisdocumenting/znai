import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'
import elementsLibrary from '../DefaultElementsLibrary'
import {expandId} from './gvUtils'

class GraphVizFlowAllInfoAtOnce extends Component {
    constructor(props) {
        super(props)
        this.state = {currentSectionIdx: -1}
    }

    render() {
        const {diagram, colors, slides, onExpand, idGen} = this.props
        const {idsToHighlight} = this.state

        return <div className="graphviz-diagram-all-content">
            <div className="diagram-panel">
                <GraphVizSvg diagram={diagram} colors={colors} idsToHighlight={idsToHighlight} />
            </div>
            <div className="diagram-explanation-panel" onMouseLeave={() => this.clearHighlights()}>
                <div className="expand glyphicon glyphicon-resize-full" onClick={onExpand}></div>

                {
                    slides.map((slide, idx) => (
                        <div className="diagram-slide" key={idx} onMouseOver={() => this.highlightNodes(slide.ids)}>
                            <div className="diagram-slide-labels" >
                                {slide.ids.map((id) => <div>// {id}</div>)}
                            </div>
                            <elementsLibrary.DocElement content={slide.content} idGen={idGen}/>
                        </div>
                    ))
                }
            </div>
        </div>
    }

    clearHighlights() {
        this.setState({idsToHighlight: []})
    }

    highlightNodes(ids) {
        const finalIds = []
        ids.forEach((id) => expandId(id).forEach((nid) => finalIds.push(nid)))

        this.setState({idsToHighlight: finalIds})
    }
}

export default GraphVizFlowAllInfoAtOnce

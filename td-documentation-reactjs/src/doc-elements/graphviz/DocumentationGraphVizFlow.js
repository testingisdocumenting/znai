import React, { Component } from 'react'

import GraphVizSvg from './GraphVizSvg'
import {expandId} from './gvUtils'

import './GraphvVizFlow.css'

class DocumentationGraphVizFlow extends Component {
    constructor(props) {
        super(props)
        this.state = {currentSectionIdx: -1}
    }

    render() {
        const {elementsLibrary, diagram, colors, slides} = this.props
        const {idsToHighlight} = this.state

        return <div className="graphviz-diagram-all-content">
            <div className="diagram-panel">
                <GraphVizSvg diagram={diagram} colors={colors} idsToHighlight={idsToHighlight} />
            </div>
            <div className="diagram-explanation-panel" onMouseLeave={() => this.clearHighlights()}>
                {
                    slides.map((slide, idx) => (
                        <div className="diagram-slide" key={idx} onMouseOver={() => this.highlightNodes(slide.ids)}>
                            <div className="diagram-slide-labels" >
                                {slide.ids.map((id) => <div key="id">// {id}</div>)}
                            </div>
                            <elementsLibrary.DocElement content={slide.content}/>
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

export default DocumentationGraphVizFlow

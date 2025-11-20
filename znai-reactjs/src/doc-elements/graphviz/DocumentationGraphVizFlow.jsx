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

import React from 'react'

import GraphVizSvg from './GraphVizSvg'
import {expandId} from './gvUtils'

import './GraphvVizFlow.css'

class DocumentationGraphVizFlow extends React.Component {
    constructor(props) {
        super(props)
        this.state = {currentSectionIdx: -1}
    }

    render() {
        const {elementsLibrary, diagram, colors, slides} = this.props
        const {idsToHighlight} = this.state
        const slideLabelPrefix = '//'

        return <div className="graphviz-diagram-all-content">
            <div className="diagram-panel">
                <GraphVizSvg diagram={diagram} colors={colors} idsToHighlight={idsToHighlight} />
            </div>
            <div className="diagram-explanation-panel" onMouseLeave={() => this.clearHighlights()}>
                {
                    slides.map((slide, idx) => (
                        <div className="diagram-slide" key={idx} onMouseOver={() => this.highlightNodes(slide.ids)}>
                            <div className="diagram-slide-labels" >
                                {slide.ids.map((id) => <div key="id">{slideLabelPrefix} {id}</div>)}
                            </div>
                            <elementsLibrary.DocElement {...this.props} content={slide.content}/>
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

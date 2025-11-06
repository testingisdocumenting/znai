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

import './DiagramLegend.css'

class DiagramLegend extends React.Component {
    render() {
        const {clickableNodes} = this.props

        return (
            <div className="znai-diagram-legend content-block">
                <div className="znai-diagram-legend-single-line">
                    {this.renderLegend()}
                </div>

                {clickableNodes && (
                    <div className="znai-diagram-legend-message">diagram nodes are clickable</div>
                )}
            </div>
        )
    }

    renderLegend() {
        const {legend} = this.props

        return (
            Object.keys(legend).map(colorGroup => {
                return (
                    <div key={colorGroup}
                         className="znai-diagram-legend-entry">
                        <div className="znai-diagram-legend-box" style={styleForColorGroup(colorGroup)}/>
                        <div className="znai-diagram-legend-text">{legend[colorGroup]}</div>
                    </div>
                )
            })
        )

    }
}

function styleForColorGroup(colorGroup) {
    return {
        border: `1px solid var(--znai-diagram-${colorGroup}-line)`,
        color: `1px solid var(--znai-diagram-${colorGroup}-text)`,
        backgroundColor: `var(--znai-diagram-${colorGroup}-fill)`,
    }
}

export default DiagramLegend

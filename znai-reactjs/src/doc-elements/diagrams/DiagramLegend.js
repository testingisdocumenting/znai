import React, {Component} from 'react'

import './DiagramLegend.css'

class DiagramLegend extends Component {
    render() {
        const {clickableNodes} = this.props

        return (
            <div className="mdoc-diagram-legend content-block">
                <div className="mdoc-diagram-legend-single-line">
                    {this.renderLegend()}
                </div>

                {clickableNodes && (
                    <div className="mdoc-diagram-legend-message">diagram nodes are clickable</div>
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
                         className="mdoc-diagram-legend-entry">
                        <div className="mdoc-diagram-legend-box" style={styleForColorGroup(colorGroup)}/>
                        <div className="mdoc-diagram-legend-text">{legend[colorGroup]}</div>
                    </div>
                )
            })
        )

    }
}

function styleForColorGroup(colorGroup) {
    return {
        border: `1px solid var(--mdoc-diagram-${colorGroup}-line)`,
        color: `1px solid var(--mdoc-diagram-${colorGroup}-text)`,
        backgroundColor: `var(--mdoc-diagram-${colorGroup}-fill)`,
    }
}

export default DiagramLegend

import React, {Component} from 'react'

import './TocPanelSearch.css'

class TocPanelSearch extends Component {
    render() {
        return (
            <div className="mdoc-toc-panel-search" onClick={this.props.onClick} title="hotkey /">
                <div className="mdoc-toc-panel-search-icon-and-text">
                    <div className="mdoc-search-icon glyphicon glyphicon-search"/>
                    <div>Search...</div>
                </div>
            </div>
        )
    }
}

export default TocPanelSearch

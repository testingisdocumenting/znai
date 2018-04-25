import React, {Component} from 'react'

import HttpCall from './HttpCall'

const httpCallIdxsQueryParam = 'httpCallIdxs'

class HttpCalls extends Component {
    state = {}

    static getDerivedStateFromProps(props) {
        const callIdxs = props.urlState[httpCallIdxsQueryParam]

        const ids = callIdxs && callIdxs !== 'NA' ? callIdxs.split(',') : []
        const expandedByIdx = {}
        ids.forEach(id => expandedByIdx[id] = true)

        return {expandedByIdx}
    }

    render() {
        const {test} = this.props;

        return (
            <div className="http">
                {test.httpCalls.map((httpCall, idx) => <HttpCall key={idx}
                                                                 idx={idx}
                                                                 httpCall={httpCall}
                                                                 isExpanded={this.isExpanded(idx)}
                                                                 onCollapseToggleClick={this.onCollapseToggleClick}/>)}
            </div>
        )
    }

    isExpanded(idx) {
        return this.state.expandedByIdx.hasOwnProperty(idx)
    }

    onCollapseToggleClick = (idx) => {
        const {onInternalStateUpdate} = this.props;

        const newExpandedByIdx = {...this.state.expandedByIdx}

        if (this.isExpanded(idx)) {
            delete newExpandedByIdx[idx]
        } else {
            newExpandedByIdx[idx] = true
        }

        onInternalStateUpdate({
            [httpCallIdxsQueryParam]: Object.keys(newExpandedByIdx).join(',') || 'NA'
        })
    }
}

export default HttpCalls
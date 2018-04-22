import React, {Component} from 'react'

import HttpCall from './HttpCall'

class HttpCalls extends Component {
    constructor(props) {
        super(props)
        this.state = { expandedByIdx: {} }
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
        const newExpandedByIdx = {...this.state.expandedByIdx}

        if (this.isExpanded(idx)) {
            delete newExpandedByIdx[idx]
        } else {
            newExpandedByIdx[idx] = true
        }

        this.setState({expandedByIdx: newExpandedByIdx})
    }
}

export default HttpCalls
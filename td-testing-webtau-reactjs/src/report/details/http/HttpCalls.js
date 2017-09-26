import React, {Component} from 'react'

import HttpCall from './HttpCall'

class HttpCalls extends Component {
    render() {
        const {test} = this.props;

        return (
            <div className="http">
                {test.httpCalls.map((httpCall, idx) => <HttpCall httpCall={httpCall}/>)}
            </div>
        )
    }
}

export default HttpCalls
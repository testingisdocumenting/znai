import React from 'react'
import './HttpCall.css'

import Payload from './Payload'

const HttpCall = ({idx, httpCall, isExpanded, onCollapseToggleClick}) => {
    const renderedDetails = isExpanded && (
        <React.Fragment>
            <div className="request">
                <Payload caption="Request"
                         type={httpCall.requestType}
                         data={httpCall.requestBody}/>
            </div>

            <div className="response">
                <Payload caption="Response"
                         type={httpCall.responseType}
                         data={httpCall.responseBody}
                         checks={httpCall.responseBodyChecks}/>
            </div>
        </React.Fragment>
    )

    return (
        <div className="http-call">
            <div className="method-and-url">
                <div className="collapse-toggle"
                      onClick={() => onCollapseToggleClick(idx)}>
                    {isExpanded ? '-' : '+'}
                </div>
                <div className="method">{httpCall.method}</div>
                <div className="url">{httpCall.url}</div>
            </div>

            {renderedDetails}
        </div>
    )
}

export default HttpCall

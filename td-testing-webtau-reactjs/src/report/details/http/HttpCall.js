import React from 'react'

import Payload from './Payload'

import './HttpCall.css'

const HttpCall = ({idx, httpCall, isExpanded, onCollapseToggleClick}) => {
    const mismatches = httpCall.mismatches.map((m, idx) => <div key={idx} className="mismatch"><pre>{m}</pre></div>)

    const renderedDetails = isExpanded && (
        <React.Fragment>
            {mismatches}

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

    const className = 'http-call' + (httpCall.mismatches.length > 0 ? ' with-mismatches' : '')
    return (
        <div className={className}>
            <div className="method-and-url" onClick={() => onCollapseToggleClick(idx)}>
                <div className="collapse-toggle">
                    {isExpanded ? '-' : '+'}
                </div>
                <div className="method">{httpCall.method}</div>
                <div className="status-code">{httpCall.responseStatusCode}</div>
                <div className="url">{httpCall.url}</div>
            </div>

            {renderedDetails}
        </div>
    )
}

export default HttpCall

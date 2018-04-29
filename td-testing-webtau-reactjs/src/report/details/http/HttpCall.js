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
            <div className="http-call-info" onClick={() => onCollapseToggleClick(idx)}>
                <div className="collapse-toggle">
                    {isExpanded ? '-' : '+'}
                </div>
                <div className="method">{httpCall.method}</div>
                <div className="status-code">{httpCall.responseStatusCode}</div>
                <div className="url">{httpCall.url}</div>
                <ElapsedTime millis={httpCall.elapsedTime}/>
            </div>

            {renderedDetails}
        </div>
    )
}

function ElapsedTime({millis}) {
    const seconds = (millis / 1000) | 0
    const remainingMs = millis % 1000

    return (
        <div className="elapsed-time">
            <Seconds seconds={seconds}/>
            <Millis millis={remainingMs}/>
        </div>
    )
}

function Seconds({seconds}) {
    if (seconds === 0) {
        return null
    }

    return (
        <React.Fragment>
            <span className="elapsed-seconds">{seconds}</span>
            <span className="time-unit">s</span>
        </React.Fragment>
    )
}

function Millis({millis}) {
    if (millis === 0) {
        return null
    }

    return (
        <React.Fragment>
            <span className="elapsed-millis">{millis}</span>
            <span className="time-unit">ms</span>
        </React.Fragment>
    )
}

export default HttpCall

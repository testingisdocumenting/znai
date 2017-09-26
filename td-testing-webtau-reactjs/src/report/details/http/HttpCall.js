import React from 'react'
import './HttpCall.css'

import Payload from './Payload'

const HttpCall = ({httpCall}) => {
    return (
        <div className="http-call">
            <div className="method-and-url">
                <span className="method">{httpCall.method}</span>
                <span className="url">{httpCall.url}</span>
            </div>

            <div className="request">
                <Payload caption="Request"
                         type={httpCall.requestType}
                         data={httpCall.requestBody}/>
            </div>

            <div className="response">
                <Payload caption="Response"
                         type={httpCall.responseType}
                         data={httpCall.responseBody}/>
            </div>
        </div>
    )
}

export default HttpCall

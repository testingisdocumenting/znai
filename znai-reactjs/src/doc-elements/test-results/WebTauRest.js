import React from 'react'

import RestPayload from './RestPayload'

import './WebTauRest.css'

const WebTauRest = ({testArtifact}) => {
    return (
        <div className="webtau-rest content-block">
            <div className="method-and-url">
                <span className="method">{testArtifact.method}</span>
                <span className="url">{testArtifact.url}</span>
            </div>

            <RestPayload caption="Request" type={testArtifact.requestType}
                         data={testArtifact.requestBody} />

            <RestPayload caption="Response" type={testArtifact.responseType}
                         data={testArtifact.responseBody}
                         checks={testArtifact.responseBodyChecks}/>
        </div>
    )
}

export default WebTauRest

import React from 'react'

import './WebTauRest.css'
import RestPayload from './RestPayload'

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

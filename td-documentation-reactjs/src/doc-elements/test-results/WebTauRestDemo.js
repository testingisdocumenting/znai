import React from 'react'

import WebTauRest from './WebTauRest'

const testArtifact = {
    "method": "POST",
    "url": "http://localhost:8180/employee",
    "requestType": "application/json",
    "requestBody": "{\"firstName\":\"FN\",\"lastName\":\"LN\"}",
    "responseType": "application/json",
    "responseBody": "{\"id\":\"id-generated-2\"}\n",
    "responseBodyChecks": {
        "failedPaths": [],
        "passedPaths": []
    }
}

const WebTauRestDemo = () => {
    return <WebTauRest testArtifact={testArtifact}/>
}

export default WebTauRestDemo

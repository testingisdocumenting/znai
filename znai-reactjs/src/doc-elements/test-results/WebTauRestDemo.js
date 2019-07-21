/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

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

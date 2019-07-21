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

import * as React from 'react'

import './DocumentationPreparation.css'

function DocumentationPreparation({docId, statusMessage, progressPercent, keyValues}) {
    return (
        <div className="documentation-preparation">
            <div className="main-message">Preparing
                <div className="doc-id"> {docId} </div>documentation
            </div>
            <div className="status-and-progress">
                <Progress percent={progressPercent}/>
                <div className="status-message">{statusMessage}</div>
            </div>
            <KeyValues pairs={keyValues}/>
        </div>
    )
}

function Progress({percent}) {
    return (
        <div className="progress-bar-container">
            <div className="progress-bar" style={{width: percent + '%'}}>
            </div>
        </div>
    )
}

function KeyValues({pairs}) {
    return (
        <div className="key-value-pairs">
            {pairs.map(pair => (
                <React.Fragment key={pair.key}>
                    <div className="key">{pair.key}</div>
                    <div className="value">{pair.value}</div>
                </React.Fragment>
            ))}
        </div>
    )
}

export default DocumentationPreparation

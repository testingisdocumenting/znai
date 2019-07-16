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

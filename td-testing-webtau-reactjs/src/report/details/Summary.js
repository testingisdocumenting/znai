import React from 'react'
import SourceCode from './SourceCode'

const OptionalPreBlock = ({className, message}) => {
    if (!message) {
        return null
    }

    return (
        <div className={className}>
            <pre>
                {message}
            </pre>
        </div>
    )
}

const Summary = ({test}) => {
    return (
        <div className="summary">
            <div className="file-name">
                {test.fileName}
            </div>

            <div className="scenario">
                {test.scenario}
            </div>

            <OptionalPreBlock className="context-description" message={test.contextDescription}/>
            <OptionalPreBlock className="assertion" message={test.assertion}/>
            {
                !test.assertion ? <OptionalPreBlock className="exception-message" message={test.exceptionMessage}/> :
                    null
            }

            {test.failedCodeSnippets.map((cs, idx) => <SourceCode key={idx} {...cs}/>)}
        </div>
    )
}

export default Summary
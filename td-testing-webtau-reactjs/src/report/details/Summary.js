import React from 'react'
import SourceCode from './SourceCode'

import CardLabelAndNumber from '../widgets/CardLabelAndNumber'

import Report from '../Report'

import './Summary.css'

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
        <div className="single-summary">
            <div className="file-name-and-scenario">
                <div className="file-name">
                    {test.fileName}
                </div>

                <div className="scenario">
                    {test.scenario}
                </div>
            </div>

            <div className="single-summary-dashboard">
                <NumberOfHttpCalls test={test}/>
                <AverageHttpCallsTime test={test}/>
                <OverallHttpCallsTime test={test}/>
            </div>

            <OptionalPreBlock className="context-description" message={test.contextDescription}/>
            <OptionalPreBlock className="assertion" message={test.assertion}/>
            {
                !test.assertion ? <OptionalPreBlock className="exception-message" message={test.exceptionMessage}/> :
                    null
            }

            {test.failedCodeSnippets && test.failedCodeSnippets.map((cs, idx) => <SourceCode key={idx} {...cs}/>)}
        </div>
    )
}

function NumberOfHttpCalls({test}) {
    if (!test.httpCalls) {
        return null
    }

    const number = test.httpCalls.length

    return (
        <CardLabelAndNumber label={httpCallsLabel(number)} number={number}/>
    )
}

function OverallHttpCallsTime({test}) {
    if (!test.httpCalls) {
        return null
    }

    return (
        <CardLabelAndNumber label="Overall Time (ms)"
                            number={Report.overallHttpCallTimeForTest(test)}/>
    )
}

function AverageHttpCallsTime({test}) {
    if (!test.httpCalls) {
        return null
    }

    return (
        <CardLabelAndNumber label="Average Time (ms)"
                            number={Report.averageHttpCallTimeForTest(test).toFixed(2)}/>
    )
}

function httpCallsLabel(number) {
    return number === 1 ? 'HTTP call' : 'HTTP calls'
}

export default Summary
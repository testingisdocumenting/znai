import React from 'react'

import AdditionalDetailsSelection from './details/AdditionalDetailsSelection'
import NoDetailsDefined from './details/NoDetailsDefined'

import './TestDetails.css'

const OptionalPreBlock = ({className, message}) => {
    if (! message) {
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

const TestDetails = ({test, detailTabs, selectedDetailTabName, onDetailsTabSelection}) => {
    const DetailTab = detailTab()
    const tabNames = detailTabs.map(r => r.tabName)

    return (
        <div className="test-details">
            <div className="file-name">
                {test.fileName}
            </div>

            <div className="scenario">
                {test.scenario}
            </div>

            <OptionalPreBlock className="context-description" message={test.contextDescription}/>
            <OptionalPreBlock className="assertion" message={test.assertion}/>
            {
                ! test.assertion ? <OptionalPreBlock className="exception-message" message={test.exceptionMessage}/> :
                    null
            }

            <AdditionalDetailsSelection tabs={tabNames}
                                        selectedTabName={selectedDetailTabName}
                                        onTabSelection={onDetailsTabSelection}/>

            <div className="detail">
                <DetailTab test={test} detailTabName={selectedDetailTabName}/>
            </div>
        </div>
    )

    function detailTab() {
        const entry = detailTabs.filter(r => r.tabName === selectedDetailTabName)
        return entry ? entry[0].component : NoDetailsDefined
    }
}

export default TestDetails
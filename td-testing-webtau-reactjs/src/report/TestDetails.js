import React from 'react'

import AdditionalDetailsSelection from './details/AdditionalDetailsSelection'
import NoDetailsDefined from './details/NoDetailsDefined'

import './TestDetails.css'

const TestDetails = ({test, detailTabs, selectedDetailTabName, onDetailsTabSelection}) => {
    const DetailTab = detailTab()
    const tabNames = detailTabs.map(r => r.tabName)

    return (
        <div className="test-details">
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
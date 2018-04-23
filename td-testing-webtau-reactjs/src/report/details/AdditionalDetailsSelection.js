import React from 'react'

const AdditionalDetailsSelection = ({tabs, selectedTabName, onTabSelection}) => {
    return (
        <div className="additional-details-selection">
            <div className="tab-names">
                {tabs.map(t => {
                    const className = "tab-name" + (selectedTabName === t ? " selected" : "")
                    return <div key={t} className={className} onClick={() => onTabSelection(t)}>{t}</div>
                })}
            </div>
        </div>
    )
}

export default AdditionalDetailsSelection

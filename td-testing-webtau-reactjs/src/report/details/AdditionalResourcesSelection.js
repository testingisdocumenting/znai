import React from 'react'

const AdditionalResourcesSelection = ({tabs, selectedTabName, onTabSelection}) => {
    return (
        <div className="additional-resources-selection">
            <div className="tab-names">
                {tabs.map(t => {
                    const className = "tab-name" + (selectedTabName === t ? " selected" : "")
                    return <div key={t} className={className} onClick={() => onTabSelection(t)}>{t}</div>
                })}
            </div>
        </div>
    )
}

export default AdditionalResourcesSelection

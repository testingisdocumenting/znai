import React from 'react'

import './Header.css'

const SummaryEntry = ({number, label, selectedLabel, onTestStatusSelect}) => {
    const className = "summary" + (selectedLabel === label ? " selected" : "")
    return (
        <div className={className} onClick={() => onTestStatusSelect(label)}>
            <span className="entry total">{number + " " + label}</span>
        </div>
    )
}

const Navigation = ({summary, onTestStatusSelect, selectedLabel}) => {
    const labels = ["Total", "Passed", "Skipped", "Failed", "Errored"]
    return (
        <div className="navigation">
            { labels.map(l => <SummaryEntry key={l} label={l} number={summary[l.toLowerCase()]}
                                            selectedLabel={selectedLabel}
                                            onTestStatusSelect={onTestStatusSelect}/>) }
        </div>
    )
}

const Header = ({summary, onTitleClick, onTestStatusSelect, selectedStatusFilter}) => {
    return (
        <div className="header">
            <div className="title" onClick={onTitleClick}>WebTau Report</div>
            <Navigation summary={summary}
                        onTestStatusSelect={onTestStatusSelect}
                        selectedLabel={selectedStatusFilter}/>
        </div>
    )
}

export default Header

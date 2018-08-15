import React from 'react'
import SettingsButton from './SettingsButton'

export default function TocSettingsTogglePanel({onSettingsClick, activated}) {
    return (
        <div className="toc-panel-settings">
            <SettingsButton onClick={onSettingsClick} activated={activated}/>
        </div>
    )
}
import React from 'react'
import SettingsButton from './SettingsButton'

export default function TocSettingsTogglePanel({onSettingsClick, active}) {
    return (
        <div className="toc-panel-settings">
            <SettingsButton onClick={onSettingsClick} active={active}/>
        </div>
    )
}
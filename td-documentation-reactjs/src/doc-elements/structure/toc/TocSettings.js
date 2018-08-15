import React from 'react'
import {themeRegistry} from '../../../theme/ThemeRegistry'

export default function TocSettings() {
    return (
        <div className="toc-settings">
            <div className="toc-section">
                <div className="title">Settings</div>
            </div>

            <div className="toc-section">
                <div className="title">Theme</div>
                <Themes/>
            </div>
        </div>
    )
}

function Themes() {
    return themeRegistry.themes.map(theme =>
        <Theme key={theme.name} theme={theme}/>)
}

function Theme({theme}) {
    const className = 'toc-item' + (themeRegistry.currentTheme.name === theme.name ? ' selected' : '')
    return (
        <div className={className}>
            <a href="#" onClick={(e) => selectTheme(e, theme.name)}>{theme.name}</a>
        </div>
    )
}

function selectTheme(e, name) {
    e.preventDefault()
    themeRegistry.selectTheme(name)
}
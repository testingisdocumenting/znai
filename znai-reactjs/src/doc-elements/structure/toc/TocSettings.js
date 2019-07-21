/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'
import {themeRegistry} from '../../../theme/ThemeRegistry'
import TocSettingsTogglePanel from './TocSettingsTogglePanel'

import './TocSettings.css'

const collapsedHeight = 48
export default class TocSettings extends React.Component {
    state = {
        maxHeight: undefined
    }

    render() {
        const {active, onSettingsToggle} = this.props
        const className = 'toc-settings' + (active ? ' active' : '')

        return (
            <div className={className} ref={node => this.node = node} style={{maxHeight: this.maxHeightToUse}}>
                <TocSettingsTogglePanel onSettingsClick={onSettingsToggle} active={active}/>

                <div className="toc-section">
                    <div className="title">Theme</div>
                    <Themes/>
                </div>
            </div>
        )
    }

    get maxHeightToUse() {
        const {active} = this.props
        const {maxHeight} = this.state

        if (! maxHeight) {
            return undefined // to have initial rendering to get the real height of the content
        }

        if (active) {
            return maxHeight
        }

        return collapsedHeight
    }

    componentDidMount() {
        this.setState({maxHeight: this.node.clientHeight})
    }
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
/*
 * Copyright 2020 znai maintainers
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

import './DarkLightThemeSwitcher.css'

const darkThemeName = 'znai-dark'

export class DarkLightThemeSwitcher extends React.Component {
    render() {
        const themeName = window.znaiTheme.name

        const themeLabel = (themeName === darkThemeName ? 'dark' : 'light')
        const sliderClassName = 'znai-theme-switcher-slider ' + themeLabel
        const switcherNameClassName = 'znai-theme-switcher-name ' + themeLabel

        const forceUpdate = () => this.forceUpdate()

        return (
            <div className="znai-theme-switcher-panel" onClick={toggleTheme}>
                <div className="znai-theme-switcher">
                    <div className={sliderClassName}/>
                    <div className={switcherNameClassName}>{themeLabel}</div>
                </div>
            </div>
        )

        function toggleTheme() {
            window.znaiTheme.toggle()
            forceUpdate()
        }
    }

    onThemeChange = () => {
        this.forceUpdate()
    }

    componentDidMount() {
        window.znaiTheme.addChangeHandler(this.onThemeChange)
    }

    componentWillUnmount() {
        window.znaiTheme.removeChangeHandler(this.onThemeChange)
    }
}

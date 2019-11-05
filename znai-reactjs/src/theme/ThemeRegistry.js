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

import Theme from './Theme'
import {znaiSettings} from '../settings/ZnaiSettings'

class ThemeRegistry {
    _themeChangeListeners = []
    _selectedTheme = null

    _selectedThemeName = znaiSettings.loadSelectedThemeName()

    themes = []
    baseTheme = null

    registerAsBase(theme) {
        this.baseTheme = theme
        this.register(theme)
    }

    get currentTheme() {
        return this._selectedTheme
    }

    overrideElement(elementId, newElement) {
        this.themes.forEach(theme => {
            theme.elementsLibrary[elementId] = newElement
        })
    }

    register(theme) {
        const found = this.themes.filter(t => t.name === theme.name)
        if (found.length > 0) {
            throw new Error('theme with name "' + theme.name + "' is already registered")
        }

        const mergedWithBase = mergeThemes(this.baseTheme, theme)
        if (! this._selectedTheme || this._selectedThemeName === mergedWithBase.name) {
            this._selectedTheme = mergedWithBase
        }

        this.themes.push(mergedWithBase)
    }

    selectTheme(name) {
        const theme = this.findByName(name)
        this._selectedTheme = theme

        znaiSettings.saveSelectedThemeName(name)
        this._themeChangeListeners.forEach(l => l(name, theme))
    }

    findByName(name) {
        const found = this.themes.filter(t => t.name === name)
        if (found.length !== 1) {
            throw new Error(`expected one theme with name "${name}", found: ${found.length}, ` +
                `available themes: ${this.themes.map(t => t.name).join(";")}`)
        }

        return found[0]
    }

    addOnThemeChangeListener(listener) {
        this._themeChangeListeners.push(listener)
    }

    removeOnThemeChangeListener(listener) {
        const idx = this._themeChangeListeners.indexOf(listener)
        if (idx === -1) {
            return
        }

        this._themeChangeListeners.splice(idx, 1)
    }
}

function mergeThemes(baseTheme, theme) {
    if (!theme.name) {
        throw new Error('theme name is not specified')
    }

    const className = theme.themeClassName || theme.name

    return new Theme({
        name: theme.name,
        themeClassName: className,
        elementsLibrary:
            {...baseTheme.elementsLibrary, ...(theme.elementsLibrary || {})},
        presentationElementHandlers:
            {...baseTheme.presentationElementHandlers, ...(theme.presentationElementHandlers || {})},
    });
}

const themeRegistry = new ThemeRegistry()

export {themeRegistry}

import Theme from './Theme'

class ThemeRegistry {
    _themeChangeListeners = []
    _selectedTheme = null

    themes = []
    baseTheme = null

    registerAsBase(theme) {
        this.baseTheme = theme
        this.register(theme)
    }

    get currentTheme() {
        return this._selectedTheme
    }

    register(theme) {
        const found = this.themes.filter(t => t.name === theme.name)
        if (found.length > 0) {
            throw new Error('theme with name "' + theme.name + "' is already registered")
        }

        const mergedWithBase = mergeThemes(this.baseTheme, theme)
        if (! this._selectedTheme) {
            this._selectedTheme = mergedWithBase
        }

        this.themes.push(mergedWithBase)
    }

    selectTheme(name) {
        const theme = this.findByName(name)
        this._selectedTheme = theme

        this._themeChangeListeners.forEach(l => l(name, theme))
    }

    findByName(name) {
        const found = this.themes.filter(t => t.name === name)
        if (found.length !== 1) {
            throw new Error(`expected one theme with name "${name}", found: ${found.length}`)
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
global.themeRegistry = themeRegistry

export {themeRegistry}

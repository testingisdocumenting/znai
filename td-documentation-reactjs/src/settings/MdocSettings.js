const themeNameKey = 'mdocTheme'

class MdocSettings {
    saveSelectedThemeName(themeName) {
        if (window.localStorage) {
            localStorage.setItem(themeNameKey, themeName)
        }
    }

    loadSelectedThemeName() {
        return window.localStorage ?
            localStorage.getItem(themeNameKey):
            undefined
    }
}

const mdocSettings = new MdocSettings()

export {mdocSettings}

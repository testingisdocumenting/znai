class SelectedTextExtensions {
    constructor() {
        this._extensions = []
    }

    register({name, action}) {
        this._extensions.push({name: name, action: action})
    }

    hasExtensions() {
        return this._extensions.length > 0
    }

    extensions() {
        return this._extensions
    }
}

const selectedTextExtensions = new SelectedTextExtensions()

export {selectedTextExtensions}

function nestedPath(nestLevel, path) {
    if (nestLevel === 0) {
        return path
    }

    return [new Array(nestLevel)].map((idx) => "..").join("/") +
        (nestLevel === 0 ? "" : "/") + path
}

export {nestedPath}

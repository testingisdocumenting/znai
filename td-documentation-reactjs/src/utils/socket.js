export function socketUrl(relativeUrl) {
    let currentLocation = document.location.toString()
    const hashIdx = currentLocation.lastIndexOf("#")
    if (hashIdx !== -1) {
        currentLocation = currentLocation.substr(0, hashIdx)
    }

    const isSecure = currentLocation.indexOf("https://") !== -1
    const protocol = isSecure ? "wss" : "ws"

    return protocol + "://" + location.hostname + ":" + location.port + "/" + relativeUrl
}

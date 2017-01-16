import Promise from "promise"

export {jsonPromise}

function jsonPromise(url) {
    return new Promise((resolve, reject) => {
        fetch(url).then((response) => {
            response.json().then((json) => {
                resolve(json)
            }, (error) => {
                reject("can't parse data from: " + url + "; " + error)
            })
        }, (response) => reject("can't read data from: " + response))
    })
}
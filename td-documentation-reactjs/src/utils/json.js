import Promise from "promise"
import {fullResourcePath} from './resourcePath'

export {jsonPromise}

function jsonPromise(docMeta, url) {
    url = fullResourcePath(docMeta.id, url)

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

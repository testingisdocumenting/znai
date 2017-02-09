import Promise from "promise"
import {nestedPath} from './renderContext'

export {jsonPromise}

function jsonPromise(renderContext, url) {
    url = nestedPath(renderContext.nestLevel, url)

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

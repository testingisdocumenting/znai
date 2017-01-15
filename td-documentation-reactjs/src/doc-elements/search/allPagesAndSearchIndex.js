export {getAllPagesPromise, getSearchIndexPromise}
import Promise from "promise"

// we don't load all-pages and index json using regular javascript script tag
// this is because we want to ui provide feedback while data loads in a background
//

let pagesPromise = null
let indexPromise = null

function getAllPagesPromise() {
    if (pagesPromise) {
        return pagesPromise
    }

    pagesPromise = jsonPromise("../../all-pages.json")
    return pagesPromise
}

function getSearchIndexPromise() {
    if (indexPromise) {
        return indexPromise
    }

    indexPromise = jsonPromise("../../search-index.json")
    return indexPromise
}

function jsonPromise(url) {
// TODO use render context to figure out path back
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

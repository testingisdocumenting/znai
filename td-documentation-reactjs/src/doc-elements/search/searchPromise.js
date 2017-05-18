import {getSearchIndexPromise} from "./searchIndex"
import {getAllPagesPromise} from "../allPages"

import Promise from "promise"
import Search from './Search'

export {getSearchPromise}

let searchPromise = null

function getSearchPromise(docMeta) {
    // server side rendering guard
    if (! window.setTimeout) {
        return null
    }

    if (searchPromise) {
        return searchPromise
    }

    searchPromise = Promise.all([getAllPagesPromise(docMeta), getSearchIndexPromise(docMeta)]).then((values) => {
        return new Search(values[0], values[1])
    }, (error) => {
        console.error(error)
        return new Promise.reject("both search index and pages needs to be loaded")
    })

    return searchPromise
}

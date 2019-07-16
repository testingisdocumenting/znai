import {getAllPagesPromise} from "../allPages"

import * as Promise from 'promise'
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

    searchPromise = getAllPagesPromise(docMeta).then((allPages) => {
        return new Search(allPages)
    }, (error) => {
        console.error(error)
        return new Promise.reject("both search index and pages needs to be loaded")
    })

    return searchPromise
}

import {getSearchIndexPromise} from "./searchIndex"
import {getAllPagesPromise} from "../allPages"

import Promise from "promise"
import Search from './Search'

export {getSearchPromise}

let searchPromise = null

function getSearchPromise() {
    if (searchPromise) {
        return searchPromise
    }

    searchPromise = Promise.all([getAllPagesPromise(), getSearchIndexPromise()]).then((values) => {
        return new Search(values[0], values[1])
    }, (error) => {
        console.error(error)
        return new Promise.reject("both search index and pages needs to be loaded")
    })

    return searchPromise
}

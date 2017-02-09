import {jsonPromise} from '../utils/json'

export {getAllPagesPromise}

// we don't load all-pages using regular javascript script tag
// this is because we want to ui provide to be responsive while data is loading in the background
//

let pagesPromise = null
function getAllPagesPromise(renderContext) {
    if (pagesPromise) {
        return pagesPromise
    }

    pagesPromise = jsonPromise(renderContext, "all-pages.json")
    return pagesPromise
}

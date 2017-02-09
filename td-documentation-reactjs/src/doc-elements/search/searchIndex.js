import {jsonPromise} from '../../utils/json'

export {getSearchIndexPromise}


// we don't load search-index using regular javascript script tag
// this is because we want to ui provide to be responsive while data is loading in the background
//

let indexPromise = null
function getSearchIndexPromise(renderContext) {
    if (indexPromise) {
        return indexPromise
    }

    indexPromise = jsonPromise(renderContext, "search-index.json")
    return indexPromise
}

import {jsonPromise} from '../../utils/json'

export {getSearchIndexPromise}


// we don't load search-index using regular javascript script tag
// this is because we want to ui provide to be responsive while data is loading in the background
//

let indexPromise = null
function getSearchIndexPromise() {
    if (indexPromise) {
        return indexPromise
    }

// TODO use render context to figure out path back
    indexPromise = jsonPromise("../search-index.json")
    return indexPromise
}

import {getDocId} from '../docMeta'
import {documentationNavigation} from './DocumentationNavigation'

function isLocalUrl(url) {
    if (!window.document) {
        return false
    }

    return url.startsWith('/' + getDocId())
}

function onLocalUrlClick(e, url) {
    e.preventDefault()
    documentationNavigation.navigateToUrl(url)
}


export {isLocalUrl, onLocalUrlClick}

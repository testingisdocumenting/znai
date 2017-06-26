import React from 'react'
import {documentationNavigation} from '../structure/DocumentationNavigation'
import {getDocId} from '../docMeta'

const Link = ({url, ...props}) => {
    const onClick = isLocalUrl(url) ? (e) => onLocalUrlClick(e, url) : null
    return <a href={url} onClick={onClick}><props.elementsLibrary.DocElement {...props}/></a>
}

function isLocalUrl(url) {
    if (! window.document) {
        return false
    }

    return url.startsWith('/' + getDocId())
}

function onLocalUrlClick(e, url) {
    e.preventDefault()
    documentationNavigation.navigateToUrl(url)
}

export default Link

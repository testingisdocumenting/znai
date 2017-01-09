import * as React from 'react'

/**
 * uses given set of components to render DocElements like links, paragraphs, code blocks, etc
 */
const DocElement = ({content, elementsLibrary}) => {
    return (<span>{!content ? null : content.map((item, idx) => {
        const ElementToUse = elementsLibrary[item.type]
        if (!ElementToUse) {
            console.warn("can't find component to display", item)
            return null
        } else {
            return <ElementToUse key={idx} {...item} />
        }
    })}</span>)
}

export default DocElement

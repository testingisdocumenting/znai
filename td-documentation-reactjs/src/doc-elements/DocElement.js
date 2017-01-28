import * as React from 'react'

/**
 * uses given set of components to render DocElements like links, paragraphs, code blocks, etc
 *
 * @param content content to render
 * @param idGen id generator for content
 * @param elementsLibrary library of elements to use to render
 */
const DocElement = ({content, idGen, elementsLibrary}) => {
    return (<span>{!content ? null : content.map((item, idx) => {
        const ElementToUse = elementsLibrary[item.type]
        if (!ElementToUse) {
            console.warn("can't find component to display", item)
            return null
        } else {
            return <ElementToUse key={idx} idGen={idGen} {...item} />
        }
    })}</span>)
}

export default DocElement

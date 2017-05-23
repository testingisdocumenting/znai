import * as React from 'react'

/**
 * uses given set of components to render DocElements like links, paragraphs, code blocks, etc
 *
 * @param content content to render
 * @param elementsLibrary library of elements to use to render
 * @param renderingMeta additional information about how to render elements
 */
const DocElement = ({content, elementsLibrary, renderingMeta}) => {
    return (<span>{!content ? null : content.map((item, idx) => {
        if (item.type === 'Meta' && renderingMeta) {
            renderingMeta = renderingMeta.register(item.target, item)
            return null
        }

        if (! elementsLibrary) {
            console.warn(content)
        }

        const ElementToUse = elementsLibrary[item.type]
        if (!ElementToUse) {
            console.warn("can't find component to display", item)
            return null
        } else {
            return <ElementToUse key={idx} {...item}
                                 elementsLibrary={elementsLibrary}
                                 renderingMeta={renderingMeta}/>
        }
    })}</span>)
}

export default DocElement

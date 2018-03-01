import React from 'react'

const DefaultPageContent = ({elementsLibrary, content}) => {
    return (
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary}
                                    content={content}/>
    )
}

export default DefaultPageContent
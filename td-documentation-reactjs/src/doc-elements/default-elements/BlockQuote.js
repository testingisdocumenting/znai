import React from 'react'

const BlockQuote = ({elementsLibrary, content}) => (
    <blockquote className="content-block"><elementsLibrary.DocElement content={content}/></blockquote>
)

const presentationBlockQuoteHandler = {component: BlockQuote,
    numberOfSlides: () => 1}

export {BlockQuote, presentationBlockQuoteHandler}

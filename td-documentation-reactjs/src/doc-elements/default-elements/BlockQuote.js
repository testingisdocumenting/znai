import React from 'react'

import './BlockQuote.css'

const BlockQuote = ({elementsLibrary, content}) => (
    <blockquote className="content-block"><elementsLibrary.DocElement content={content}/></blockquote>
)

const PresentationBlockQuote = ({elementsLibrary, content}) =>
    <blockquote>
        <elementsLibrary.DocElement content={content}/>
    </blockquote>

const presentationBlockQuoteHandler = {component: PresentationBlockQuote,
    numberOfSlides: () => 1}

export {BlockQuote, presentationBlockQuoteHandler}

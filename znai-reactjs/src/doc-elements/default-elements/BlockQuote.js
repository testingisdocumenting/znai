import React from 'react'

import './BlockQuote.css'

const BlockQuote = (props) => (
    <blockquote className="content-block"><props.elementsLibrary.DocElement {...props}/></blockquote>
)

const PresentationBlockQuote = (props) =>
    <blockquote>
        <props.elementsLibrary.DocElement {...props}/>
    </blockquote>

const presentationBlockQuoteHandler = {component: PresentationBlockQuote,
    numberOfSlides: () => 1}

export {BlockQuote, presentationBlockQuoteHandler}

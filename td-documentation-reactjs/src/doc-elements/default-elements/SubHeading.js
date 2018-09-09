import React from 'react'
import {extractTextFromContent} from './textContent'

import Icon from '../icons/Icon'

import './SubHeading.css'

const SubHeading = ({level, ...props}) => {
    const id = makeId(props.content)
    const Element = `h${level}`

    return (
        <Element className="content-block" id={id}>
            <props.elementsLibrary.DocElement {...props}/>
            <a href={"#" + id}><Icon id="link"/></a>
        </Element>
    )
}

function makeId(content) {
    const text = extractTextFromContent(content)

    return text.replace(/[^a-zA-Z0-9-_ ]/g, '')
        .replace(/\s+/g, '-')
        .toLowerCase()
}

export default SubHeading

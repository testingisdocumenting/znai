import React from 'react'

import Icon from '../icons/Icon'

import {extractTextFromContent} from './textContent'

import './SubHeading.css'

export default function SubHeading(props) {
    return props.id ?
        <NewSubHeading {...props}/>:
        <DeprecatedSubHeading {...props}/>
}

function NewSubHeading({level, title, id}) {
    const Element = `h${level}`

    return (
        <Element className="content-block" id={id}>
            {title}
            <a href={"#" + id}><Icon id="link"/></a>
        </Element>
    )
}

// TODO remove once everyone has their docs re-deployed
function DeprecatedSubHeading({level, title, ...props}) {
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

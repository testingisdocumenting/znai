import React from 'react'

import Icon from '../icons/Icon'

import './SubHeading.css'

export default function SubHeading({level, title, id}) {
    const Element = `h${level}`

    return (
        <Element className="content-block" id={id}>
            {title}
            <a href={"#" + id}><Icon id="link"/></a>
        </Element>
    )
}

import React from 'react'

import Icon from '../icons/Icon'
import {startsWithIcon, removeIcon, extractIconId} from './bulletUtils'

import './ListItem.css'

const ListItem = (props) => {
    let content = props.content
    const hasIcon = startsWithIcon(content)

    const className = "list-item" + (hasIcon ? " icon-based" : "")
    const childrenContent = hasIcon ? removeIcon(content) : content
    const children = <props.elementsLibrary.DocElement {...props} content={childrenContent}/>

    return <li className={className}>
        {hasIcon ? <Icon id={extractIconId(content)}/> : null}
        {children}
    </li>
}

export default ListItem

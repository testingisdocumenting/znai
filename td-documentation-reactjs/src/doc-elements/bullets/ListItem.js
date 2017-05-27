import React from 'react'
import {startsWithIcon} from './bulletUtils'
import './ListItem.css'

const ListItem = (props) => {
    let content = props.content
    const hasIcon = startsWithIcon(content)

    const className = "list-item" + (hasIcon ? " icon-based" : "")
    const children = <props.elementsLibrary.DocElement {...props}/>

    return <li className={className}>
        {hasIcon ? <span className="list-item-content">{children}</span>: children}
    </li>
}

export default ListItem
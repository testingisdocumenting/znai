import React from 'react'
import {startsWithIcon} from '../bulletUtils'

const DefaultBulletList = (props) => {
    const {tight, content} = props

    const hasIcon = content.length && startsWithIcon(content[0].content)
    const className = "content-block" + (tight ? " tight" : "") + (hasIcon ? " icon-based" : "")

    return (<ul className={className}><props.elementsLibrary.DocElement {...props}/></ul>)
}

export default DefaultBulletList

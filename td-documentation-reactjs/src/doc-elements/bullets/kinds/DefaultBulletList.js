import React from 'react'
import {startsWithIcon} from '../bulletUtils'

const DefaultBulletList = ({tight, ...props}) => {
    const {content} = props

    const hasIcon = content.length && startsWithIcon(content[0].content)
    const className = "content-block" + (tight ? " tight" : "") + (hasIcon ? " icon-based" : "")

    return (<ul className={className}><props.elementsLibrary.DocElement {...props}/></ul>)
}

export default DefaultBulletList

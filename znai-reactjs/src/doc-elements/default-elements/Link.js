import React from 'react'
import {isLocalUrl, onLocalUrlClick} from '../structure/links'

const Link = ({url, isFile, ...props}) => {
    const isLocalNavigation = isLocalUrl(url) && !isFile;
    const onClick = isLocalNavigation ? (e) => onLocalUrlClick(e, url) : null
    const targetProp = isLocalNavigation ? {} : {target: "_blank"}

    return (
        <a href={url} onClick={onClick} {...targetProp}>
            <props.elementsLibrary.DocElement {...props}/>
        </a>
    )
}

export default Link

import React from 'react'

import {isLocalUrl, onLocalUrlClick} from '../structure/links'

const LinkWrap = ({url, children}) => {
    if (!url) {
        return children
    }

    const isLocal = isLocalUrl(url);
    const onClick = isLocal ? (e) => onLocalUrlClick(e, url) : null
    const targetProp = isLocal ? {} : {target: "_blank"}

    return <a href={url} onClick={onClick} {...targetProp}>{children}</a>
}

export default LinkWrap
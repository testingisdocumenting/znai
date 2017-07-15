import React from 'react'

import {isPreviewEnabled} from '../docMeta'

const Image = ({destination, inlined, isNarrow}) => {
    const className = "image" + (inlined ? " inlined" : "")
        + (isNarrow ? " content-block" : "")

    const srcToUse = destination + (isPreviewEnabled() ? "?time=" + new Date().getTime() : "")
    const alt = `image ${destination} not found`

    return (<div className={className}><img alt={alt} src={srcToUse}/></div>)
}

export default Image

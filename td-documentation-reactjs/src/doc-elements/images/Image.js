import React from 'react'

import {isPreviewEnabled} from '../docMeta'

const Image = ({destination, inlined}) => {
    const className = "image" + (inlined ? " inlined" : "")
    const srcToUse = destination + (isPreviewEnabled() ? "?time=" + new Date().getTime() : "")
    const alt = `image ${destination} not found`

    return (<div className={className}><img alt={alt} src={srcToUse}/></div>)
}

export default Image

import React from 'react'
import {imageAdditionalPreviewUrlParam} from './imagePreviewAdditionalUrlParam'

const Image = ({destination, inlined, isNarrow, timestamp}) => {
    const className = "image" + (inlined ? " inlined" : "")
        + (isNarrow ? " content-block" : "")

    const alt = `image ${destination} was not found`

    return (
        <div className={className}>
            <img alt={alt} src={destination + imageAdditionalPreviewUrlParam(timestamp)}/>
        </div>
    )
}

export default Image

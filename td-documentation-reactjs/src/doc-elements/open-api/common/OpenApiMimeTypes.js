import React from 'react'

import './OpenApiMimeTypes.css'

export default function OpenApiMimeTypes({types}) {
    const renderedTypes = types.map(t => <div>{t}</div>)
    return (
        <div className="open-api-mime-types">
            {renderedTypes}
        </div>
    )
}

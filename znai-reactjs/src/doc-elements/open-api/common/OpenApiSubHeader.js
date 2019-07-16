import React from 'react'

import './OpenApiSubHeader.css'

export default function OpenApiSubHeader({title, description, elementsLibrary}) {
    return (
        <div className="open-api-sub-header">
            <div className="header-part">
                {title}
            </div>

            {description &&
            <div className="description-part">
                <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary}/>
            </div>
            }
        </div>
    )
}

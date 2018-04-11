import React from 'react'

import Jsx from './Jsx'

import './JsxGroup.css'

const JsxGroup = ({declarations}) => {
    return (
        <div className="jsx-group content-block">
            {declarations.map((declaration, idx) => <Jsx key={idx} declaration={declaration}/>)}
        </div>
    )
}

export default JsxGroup

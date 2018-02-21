import React from 'react'

import './LangClass.css'
import LangFunction from './LangFunction'

function LangClass({name, description, methods, ...props}) {
    return (
        <div className="lang-class">
            <div className="class-name token class-name">{name}</div>
            <div className="description-and-methods">
                <div className="class-description">{description}</div>
                {methods.map(m => <LangFunction key={methodKey(m)} {...m} {...props}/>)}
            </div>
        </div>
    )
}

function methodKey(m) {
    return m.name + ' ' + m.params.map(p => p.name).join(',')
}

export default LangClass

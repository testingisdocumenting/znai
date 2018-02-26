import React from 'react'

import './LangFunction.css'
import {splitFuncNameAndParams} from './langUtils'

function LangFunction({name, description, params, ...props}) {
    const {funcName, funcParamsAsStr} = splitFuncNameAndParams(name)

    return (
        <div className="lang-function">
            <div className="function-name">
                <span className="token function">{funcName}</span>
                <span className="token punctuation">(</span>
                <span className="function-params-as-str">{funcParamsAsStr}</span>
                <span className="token punctuation">)</span>
            </div>

            <div className="description-and-params">
                <div className="function-description">{description}</div>
                <div className="function-params">
                    <FunctionParams params={params} {...props}/>
                </div>
            </div>
        </div>
    )
}

function FunctionParams({params}) {
    return params.map(p => <FunctionParam key={p.name} param={p}/>)
}

function FunctionParam({param}) {
    return (
        <React.Fragment>
            <div className="name-and-type">
                <div className="name">{param.name}</div>
                {param.type && <div className="type">{param.type}</div>}
            </div>
            <div className="description">
                {param.description}
            </div>
        </React.Fragment>
    )
}

export default LangFunction

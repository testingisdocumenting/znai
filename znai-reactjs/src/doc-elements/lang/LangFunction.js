/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

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
import OpenApiResponses from '../response/OpenApiResponses'
import OpenApiParameters from '../parameter/OpenApiParameters'

import OpenApiBodyParameter from '../parameter/OpenApiBodyParameter'

import './OpenApiOperation.css'

function OpenApiOperation({elementsLibrary, showConsumes, showProduces, operation}) {
    const parameters = operation.parameters || []

    const pathParameters = parameters.filter(p => p.in === 'path')
    const queryParameters = parameters.filter(p => p.in === 'query')
    const formDataParameters = parameters.filter(p => p.in === 'formData')

    const bodyParams = parameters.filter(p => p.in === 'body')
    const bodyParameter = bodyParams.length ? bodyParams[0] : null

    const consumes = showConsumes ? operation.consumes : []
    const produces = showProduces ? operation.produces : []

    return (
        <div className="open-api-operation content-block">
            <div className="url-and-summary">
                <div className="method">{operation.method}</div>
                <div className="path"><Path path={operation.path}/></div>
            </div>

            <Description description={operation.description} elementsLibrary={elementsLibrary}/>

            <div className="open-api-operation-parameters">
                <OpenApiParameters label="Path parameters" parameters={pathParameters} elementsLibrary={elementsLibrary}/>
                <OpenApiParameters label="Query parameters" parameters={queryParameters} elementsLibrary={elementsLibrary}/>
                <OpenApiParameters label="Form Data parameters" parameters={formDataParameters} elementsLibrary={elementsLibrary}/>
                <OpenApiBodyParameter parameter={bodyParameter} consumes={consumes} elementsLibrary={elementsLibrary}/>
                <OpenApiResponses responses={operation.responses} produces={produces} elementsLibrary={elementsLibrary}/>
            </div>
        </div>
    )
}

function Description({description, elementsLibrary}) {
    if (!description || description.length === 0) {
        return null
    }

    return (
        <div className="open-api-description">
            <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary}/>
        </div>
    )
}

function Path({path}) {
    const parts = path.split("/")

    return parts
        .filter(part => part.length)
        .map((part, idx) => {
            const className = part.indexOf('{') === 0 ?
                'path-part-parameter' : 'path-part'

            return (
                <React.Fragment key={idx}>
                    <div className="path-part-delimiter">/</div>
                    <div className={className}>{part}</div>
                </React.Fragment>)
        })
}

export default OpenApiOperation

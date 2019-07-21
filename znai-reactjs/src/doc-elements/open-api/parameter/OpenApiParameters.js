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

import OpenApiSubHeader from '../common/OpenApiSubHeader'

import {openApiParameterToApiParameter} from './openApiParameterToApiParameter'
import ApiParameters from '../../api/ApiParameters'

import './OpenApiParameters.css'

function OpenApiParameters({label, parameters, elementsLibrary}) {
    if (parameters.length === 0) {
        return null
    }

    const apiParameters = parameters.map(p => openApiParameterToApiParameter(p))

    return (
        <React.Fragment>
            <OpenApiSubHeader title={label}/>
            <div className="open-api-parameters">
                <ApiParameters parameters={apiParameters} elementsLibrary={elementsLibrary}/>
            </div>
        </React.Fragment>
    )
}

export default OpenApiParameters

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
import OpenApiSchema from './OpenApiSchema'

import {elementsLibrary} from '../../DefaultElementsLibrary'

import {checkSchema, customerArraySchema, customerIdsSchema, customerSchema} from './OpenApiSchema.data.demo'

export function openApiSchemaDemo(registry) {
    registry
        .add('array of strings', () => <OpenApiSchema schema={customerIdsSchema} elementsLibrary={elementsLibrary}/>)
        .add('array of objects', () => <OpenApiSchema schema={customerArraySchema} elementsLibrary={elementsLibrary}/>)
        .add('flat object', () => <OpenApiSchema schema={customerSchema} elementsLibrary={elementsLibrary}/>)
        .add('nested object', () => <OpenApiSchema schema={checkSchema} elementsLibrary={elementsLibrary}/>)
}


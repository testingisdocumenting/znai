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

import {elementsLibrary} from '../DefaultElementsLibrary'
import LangFunction from './LangFunction'

const nameAndDesc = {
    'name': 'functionName(param1, longerParam2)',
    'description': 'function description',
}

const paramsWithoutType = [
    {
        'name': 'param1',
        'description': 'param1 description',
    },
    {
        'name': 'longerParam2',
        'description': 'longer param2 description'
    }
]

const paramsWithType = [
    {
        'name': 'param1',
        'description': 'param1 description',
        'type': 'str',
    },
    {
        'name': 'longerParam2',
        'description': 'longer param2 description',
        'type': 'integer or None or string',
    }
]

export function langFunctionDemo(registry) {
    registry
        .add('simple function', () => <LangFunction {...nameAndDesc} params={paramsWithoutType}
                                                    elementsLibrary={elementsLibrary}/>)
        .add('param types', () => <LangFunction {...nameAndDesc} params={paramsWithType}
                                                elementsLibrary={elementsLibrary}/>)
}

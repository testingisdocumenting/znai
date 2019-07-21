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

import {Columns} from './Columns'

const testData = {
    config: {
        border: true,
        left: {portion: 3}
    },
    columns: [{
        content: [
            {
                "text": `It_is_very_`,
                // "text": `It_is_very_easy_to_add_a_code_snippet_or_an_output_result_It_is_very_easy_to_add_a_code_snipp.`,
                "type": "SimpleText"
            },
        ]
    },
        {
            content: [
                {
                    "text": "one liner",
                    "type": "SimpleText"
                }
            ]
        }]
}


const ColumnsDemo = () => {
    return (
        <Columns elementsLibrary={elementsLibrary} {...testData}/>
    )
}

export default ColumnsDemo

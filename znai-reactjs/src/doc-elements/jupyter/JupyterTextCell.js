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
import {splitAndTrimEmptyLines} from '../../utils/strings'

const JupyterTextCell = ({text, elementsLibrary}) => {
    const lines = convertToLines(text)

    return (
        <div className="jupyter-cell jupyter-text content-block">
            <elementsLibrary.CliOutput lines={lines}/>
        </div>
    )
}

function convertToLines(text) {
    const lines = splitAndTrimEmptyLines(text)
    if (lines.length > 0 && lines[lines.length - 1].length === 0) {
        lines.pop()
    }

    return lines
}

export default JupyterTextCell

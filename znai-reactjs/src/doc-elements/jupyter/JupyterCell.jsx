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
import JupyterCodeCell from './JupyterCodeCell'
import JupyterHtmlCell from './JupyterHtmlCell'
import JupyterTextCell from './JupyterTextCell'
import JupyterEmptyCell from './JupyterEmptyCell'
import JupyterSvgCell from './JupyterSvgCell'
import JupyterImgCell from './JupyterImgCell'

const JupyterCell = (props) => {
    const Cell = cellComponent(props)
    return (
        <Cell {...props} elementsLibrary={props.elementsLibrary}/>
    )
}

function cellComponent(cell) {
    switch (cell.cellType) {
        case 'code':
            return JupyterCodeCell
        case 'empty-output':
            return JupyterEmptyCell
        case 'output':
            if (cell.html) {
                return JupyterHtmlCell
            }

            if (cell.svg) {
                return JupyterSvgCell
            }

            if (cell.img) {
                return JupyterImgCell
            }

            return JupyterTextCell
        default:
            return JupyterEmptyCell
    }
}

export default JupyterCell

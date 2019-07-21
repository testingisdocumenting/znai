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
import JupyterCell from './JupyterCell'

import {elementsLibrary} from '../DefaultElementsLibrary'
import {parseCode} from '../code-snippets/codeParser'

const simpleNotebook = {
    "cells": [
        {
            sourceTokens: parseCode('python',
                'from pandas import read_csv\n' +
                'from IPython.display import display')
        },
        {
            text:
            "   a   b   c\n" +
            "0  1   2   3\n" +
            "1  4   5   6\n"
        },
        {
            "cell_type": "code",
            "execution_count": 14,
            "html": `
                            <div>
                            <style scoped>
                                .dataframe tbody tr th:only-of-type {
                                    vertical-align: middle;
                                }
                            
                                .dataframe tbody tr th {
                                    vertical-align: top;
                                }
                            
                                .dataframe thead th {
                                    text-align: right;
                                }
                            </style>
                            <table border="1" class="dataframe">
                              <thead>
                                <tr style="text-align: right;">
                                  <th></th>
                                  <th>a</th>
                                  <th>b</th>
                                  <th>c</th>
                                </tr>
                              </thead>
                              <tbody>
                                <tr>
                                  <th>0</th>
                                  <td>1</td>
                                  <td>2</td>
                                  <td>3</td>
                                </tr>
                                <tr>
                                  <th>1</th>
                                  <td>4</td>
                                  <td>5</td>
                                  <td>6</td>
                                </tr>
                              </tbody>
                            </table>
                            </div> `
        }
    ]
}

export function jupyterDemo(registry) {
    registry
        .add('code cell', () => <JupyterCell elementsLibrary={elementsLibrary} cell={simpleNotebook.cells[0]}/>)
        .add('output cell', () => <JupyterCell elementsLibrary={elementsLibrary} cell={simpleNotebook.cells[1]}/>)
        .add('html cell', () => <JupyterCell elementsLibrary={elementsLibrary} cell={simpleNotebook.cells[2]}/>)
}

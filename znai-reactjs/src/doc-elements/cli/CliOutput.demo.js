/*
 * Copyright 2020 znai maintainers
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

import React from "react";

import {CliOutput} from "./CliOutput";

export function cliOutputDemo(registry) {
    registry.add('no highlight', () => (
        <CliOutput lines={generateNonAnsiLines()}/>
    ))
    registry.add('with highlight', () => (
        <CliOutput lines={generateNonAnsiLines()} highlight={[1, 'line number 7']}/>
    ))
}

function generateNonAnsiLines() {
    return [
        'line number 1',
        'line number 2',
        'line number 3',
        'line number 4',
        'line number 5',
        'line number 6',
        'line number 7',
        'line number 8',
        'line number 9',
        'line number 10',
        'line number 11',
        'line number 12',
    ]
}

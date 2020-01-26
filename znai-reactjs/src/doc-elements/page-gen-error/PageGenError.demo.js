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

import * as React from 'react'
import {PageGenError} from './PageGenError'

export function pageGenErrorDemo(registry) {
    registry
        .add('small error', () => <PageGenError error={{message: "File is not found", stackTrace: "stackTrace"}}/>)

        .add('large error', () => <PageGenError error={{
            message: generateLargeText('error line #'),
            stackTrace: generateLargeText("stack trace")
        }}/>)
}

function generateLargeText(prefix) {
    let lines = []
    for (let idx = 0; idx < 50; idx++) {
        lines.push(prefix + (idx + 1))
    }

    return lines.join("\n")
}
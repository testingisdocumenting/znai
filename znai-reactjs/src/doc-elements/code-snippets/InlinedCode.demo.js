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

import {InlinedCode} from './InlinedCode'
import Link from '../default-elements/Link'
import {elementsLibrary} from '../DefaultElementsLibrary'

export function inlinedCodeDemo(registry) {
    registry
        .add('regular', () => <InlinedCode code="ClassName"/>)
        .add('with global doc ref', () => <InlinedCode code="package.SuperClass"/>)
        .add('with local doc ref', () => <InlinedCode code="another.SuperClass" references={{
            'another.SuperClass': {
                pageUrl: '#another-url'
            }
        }

        }/>)
        .add('inside link tet', () => (
            <Link url="#url"
                  content={linkContent()}
                  elementsLibrary={elementsLibrary}/>
        ))
}

function linkContent() {
    return [
        {
            type: 'SimpleText',
            text: 'text '
        },
        {
            type: 'InlinedCode',
            code: 'superCode'
        },
        {
            type: 'SimpleText',
            text: ' text '
        }
    ]
}
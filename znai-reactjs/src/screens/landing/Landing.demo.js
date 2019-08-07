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
import {Landing} from './Landing'

const documentations = [
    {
        id: 'hipchat',
        name: 'HipChat',
        category: 'Collaboration',
        description: 'short description of HipChat. short description of HipChat'
    },
    {
        id: null,
        name: 'Slack',
        category: 'Collaboration',
        url: 'https://slack.com',
        description: 'external link example'
    },
    {
        id: 'mdoc',
        name: 'MDoc',
        category: 'Documentation',
        description: 'short description of mdoc. short description of mdoc. '
    },
    {
        id: 'python',
        name: 'Python',
        category: 'Languages',
        description: 'short description of python. short description of python. '
    },
    {
        id: 'java',
        name: 'Java',
        category: 'Languages',
        description: 'short description of java. short description of java. '
    },
    {
        id: 'javascript',
        name: 'JavaScript',
        category: 'Languages',
        description: 'short description of javascript. short description of javascript. '
    },
    {
        id: 'kotlin',
        name: 'Kotlin',
        category: 'Languages',
        description: 'short description of kotlin. short description of kotlin. short description of kotlin. short description of kotlin. '
    },
]

export function landingDemo(registry) {
    registry
        .add('landing', () => <Landing documentations={documentations} title="Company" type="Guides"/>)
        .add('landing dark theme', () => <div className="with-theme theme-znai-dark"><Landing
            documentations={documentations} title="Company" type="Guides"/></div>)
}

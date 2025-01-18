/*
 * Copyright 2020 znai maintainers
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

import {printXml} from './xmlPrinter'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'
import SnippetContainer from '../code-snippets/SnippetContainer'

const Xml = ({xmlAsJson, paths, title, ...props}) => {
    const lines = printXml({xmlAsJson, singleLineAttrs: true, pathsToHighlight: paths})

    return (
        <SnippetContainer linesOfCode={lines}
                          title={title}
                          numberOfVisibleLines={100}
                          snippetComponent={SimpleCodeSnippet}
                          {...props}/>
    )
}

export default Xml
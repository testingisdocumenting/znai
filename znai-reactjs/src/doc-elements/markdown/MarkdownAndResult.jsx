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
import {Columns} from '../columns/Columns'

import './MarkdownAndResult.css'

const TwoColumns = ({markdown, result, ...props}) => {
    const column1 = {content: [markdown]}
    const column2 = {content: result}

    return <Columns columns={[column1, column2]}
                    config={{border: false}}
                    {...props}/>
}

const MarkdownAndResult = ({markdown, result, ...props}) => {
    return (
        <div className="markdown-and-result">
            <TwoColumns markdown={markdown} result={result} {...props}/>
        </div>
    )
}

const presentationMarkdownAndResultHandler = {component: MarkdownAndResult,
    numberOfSlides: () => 2}

export {MarkdownAndResult, presentationMarkdownAndResultHandler}


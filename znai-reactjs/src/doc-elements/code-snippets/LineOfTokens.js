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

import {enhanceMatchedTokensWithMeta} from './codeUtils'

import './LineOfTokens.css'

const LineOfTokens = ({tokens, references, isHighlighted, isPresentation, TokenComponent}) => {
    const className = "code-line" + (isHighlighted ? " highlight" : "")
    const enhancedTokens = enhanceTokens()

    return (
        <span className={className}>
            {enhancedTokens.map((t, idx) => <TokenComponent key={idx} token={t} isPresentation={isPresentation}/>)}
        </span>
    )

    function enhanceTokens() {
        if (!references) {
            return tokens
        }

        return enhanceMatchedTokensWithMeta(tokens, Object.keys(references), () => 'link', (referenceText) => {
            const reference = references[referenceText]
            return reference.pageUrl
        })
    }
}

export default LineOfTokens

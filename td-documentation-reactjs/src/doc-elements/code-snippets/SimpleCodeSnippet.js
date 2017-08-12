import React from 'react'

import {splitTokensIntoLines} from './codeUtils'
import LineOfTokens from './LineOfTokens.js'
import SimpleCodeToken from './SimpleCodeToken.js'
import './CodeSnippet.css'

const SimpleCodeSnippet = ({tokens, isPresentation, highlight}) => {
    const lines = splitTokensIntoLines(tokens)

    return (
        <div>
            <pre><code>
                {lines.map((line, idx) => <LineOfTokens key={idx} line={line}
                                                        isHighlighted={isHighlighted(idx)}
                                                        isPresentation={isPresentation}
                                                        TokenComponent={SimpleCodeToken}/>)}
            </code></pre>
        </div>
    )

    function isHighlighted(idx) {
        return highlight && highlight.indexOf(idx) !== -1
    }
}

export default SimpleCodeSnippet

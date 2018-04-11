import React from 'react'

import SimpleCodeToken from '../code-snippets/SimpleCodeToken'
import LineOfTokens from '../code-snippets/LineOfTokens'

import {printJsx} from './jsxPrinter'

import './Jsx.css'

const Jsx = ({declaration}) => {
    const lines = printJsx(declaration)

    return (
        <div className="jsx content-block">
            {lines.map((tokens, idx) => <LineOfTokens key={idx}
                                                      tokens={tokens}
                                                      isHighlighted={false}
                                                      isPresentation={false}
                                                      TokenComponent={SimpleCodeToken}/>)
            }
        </div>
    )
}

export default Jsx
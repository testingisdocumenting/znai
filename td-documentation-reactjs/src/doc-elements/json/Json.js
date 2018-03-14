import React from 'react'

import SimpleCodeToken from '../code-snippets/SimpleCodeToken'
import LineOfTokens from '../code-snippets/LineOfTokens'

import {printJson} from './jsonPrinter'

import './Json.css'

const Json = ({data, paths}) => {
    const lines = printJson('root', data, paths)

    return (
        <div className="json content-block">
            {lines.map((tokens, idx) => <LineOfTokens key={idx}
                                                      tokens={tokens}
                                                      isHighlighted={false}
                                                      isPresentation={false}
                                                      TokenComponent={SimpleCodeToken}/>)
            }
        </div>
    )
}

export default Json
import React from 'react'

import {printJson} from './jsonPrinter'

import SnippetContainer from '../code-snippets/SnippetContainer'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'

import './Json.css'

const Json = ({data, paths, title}) => {
    const lines = printJson('root', data, paths)

    return (
        <SnippetContainer linesOfCode={lines}
                          title={title}
                          snippetComponent={SimpleCodeSnippet} />
    )
}

export default Json
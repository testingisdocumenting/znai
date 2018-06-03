import React from 'react'

import {printXml} from './xmlPrinter'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'
import SnippetContainer from '../code-snippets/SnippetContainer'

const Xml = ({xmlAsJson, paths, title, ...props}) => {
    const lines = printXml({xmlAsJson, singleLineAttrs: true, pathsToHighlight: paths})

    return (
        <SnippetContainer linesOfCode={lines}
                          title={title}
                          snippetComponent={SimpleCodeSnippet}
                          {...props}/>
    )
}

export default Xml
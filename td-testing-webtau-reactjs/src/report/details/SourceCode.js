import React from 'react'

import Prism from 'prismjs'

import 'prismjs/components/prism-groovy'
import 'prismjs/plugins/line-highlight/prism-line-highlight'

import 'prismjs/themes/prism.css'
import 'prismjs/plugins/line-highlight/prism-line-highlight.css'

import './SourceCode.css'

class SourceCode extends React.Component {
    render() {
        const {filePath, lineNumber, snippet} = this.props

        return (
            <div className="source-code">
                <div className="file-path">{filePath}</div>
                <pre data-line={lineNumber} className="language-groovy">
                    <code>
                        {snippet}
                    </code>
                </pre>
            </div>
        )
    }

    componentDidMount() {
        Prism.highlightAll()
    }
}

export default SourceCode
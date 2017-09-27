import React from 'react'
import JSONTree from 'react-json-tree'

import './JsonPayload.css'

const jsonTheme = {
    scheme: 'default',
    author: 'chris kempson (http://chriskempson.com)',
    base00: '#181818',
    base01: '#282828',
    base02: '#383838',
    base03: '#585858',
    base04: '#b8b8b8',
    base05: '#d8d8d8',
    base06: '#e8e8e8',
    base07: '#f8f8f8',
    base08: '#ab4642',
    base09: '#dc9656',
    base0A: '#f7ca88',
    base0B: '#a1b56c',
    base0C: '#86c1b9',
    base0D: '#7cafc2',
    base0E: '#ba8baf',
    base0F: '#a16946'
}

const jsonValueRenderer = (checks) => {
    checks = checks || {}
    checks.failedPaths = checks.failedPaths || []
    checks.passedPaths = checks.passedPaths || []

    return (pretty, raw, ...path) => {
        const fullPath = path.reverse().slice(1).join('.')
        const isFailed = checks.failedPaths.indexOf(fullPath) !== -1
        const isPassed = checks.passedPaths.indexOf(fullPath) !== -1

        const isHighlighted = isFailed || isPassed
        const className = isHighlighted ?
            'json-value-highlight' + (isFailed ? ' failed' : ' passed') : null

        return isHighlighted ? <span className={className}>{pretty}</span> : pretty;
    }
}

const expandNode = () => true

const JsonPayload = ({json, checks}) => {
    return (
        <div className="data json">
            <JSONTree data={json}
                      theme={jsonTheme}
                      valueRenderer={jsonValueRenderer(checks)}
                      shouldExpandNode={expandNode}/>
        </div>
    )
}

export default JsonPayload

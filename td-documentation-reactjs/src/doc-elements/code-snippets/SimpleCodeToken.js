import React from 'react'

import 'prismjs/themes/prism-coy.css'

const SimpleCodeToken = ({token}) => {
    if (isSimpleValue(token)) {
        return <React.Fragment>{token}</React.Fragment>
    }

    const className = (token.type === 'text') ? '' : 'token ' + token.type
    return (
        <span className={className} onClick={token.onClick}>
            {renderData(token)}
        </span>
    )
}

function renderData(token) {
    if (isSimpleValue(token)) {
        return token
    }

    if (Array.isArray(token.content)) {
        return token.content.map((d, idx) => <SimpleCodeToken key={idx} token={d}/>)
    }

    if (typeof token === 'object') {
        return <SimpleCodeToken token={token.content} onClick={token.onClick}/>
    }

    return JSON.stringify(token)
}

function isSimpleValue(token) {
    return typeof token === 'string' || typeof token === 'number'
}

export default SimpleCodeToken
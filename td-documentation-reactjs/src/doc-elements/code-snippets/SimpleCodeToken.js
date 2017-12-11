import React from 'react'

import 'prismjs/themes/prism-coy.css'

const SimpleCodeToken = ({token}) => {
    if (typeof token === 'string') {
        return <span>{token}</span>
    }

    const className = (token.type === 'text') ? '' : 'token ' + token.type
    return (
        <span className={className}>
            {renderData(token)}
        </span>
    )
}

function renderData(token) {
    if (typeof token === 'string') {
        return token
    }

    if (Array.isArray(token.content)) {
        return token.content.map((d, idx) => <SimpleCodeToken key={idx} token={d}/>)
    }

    if (typeof token === 'object') {
        return <SimpleCodeToken token={token.content}/>
    }

    return JSON.stringify(token)

}

export default SimpleCodeToken
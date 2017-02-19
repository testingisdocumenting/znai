import React from 'react'

import 'prismjs/themes/prism.css'

const SimpleCodeToken = ({token}) => {
    const className = (token.type === 'text') ? '' : "token " + token.type
    return (<span className={className}>{token.data}</span>)
}

export default SimpleCodeToken
import React from 'react'

const LineOfTokens = ({tokens, isHighlighted, isPresentation, TokenComponent}) => {
    const className = "code-line" + (isHighlighted ? " highlight" : "")

    return (
        <span className={className}>
            {tokens.map((t, idx) => <TokenComponent key={idx} token={t} isPresentation={isPresentation}/>)}
        </span>
    )
}

export default LineOfTokens

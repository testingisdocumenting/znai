import React from 'react'
import {Columns} from '../columns/Columns'

import './MarkdownAndResult.css'

const TwoColumns = ({markdown, result, ...props}) => {
    const column1 = {content: [markdown]}
    const column2 = {content: result}

    return <Columns columns={[column1, column2]}
                    config={{border: true}}
                    {...props}/>
}

const MarkdownAndResult = ({markdown, result, ...props}) => {
    return (
        <div className="markdown-and-result">
            <TwoColumns markdown={markdown} result={result} {...props}/>
        </div>
    )
}

const presentationMarkdownAndResultHandler = {component: MarkdownAndResult,
    numberOfSlides: ({}) => 2}

export {MarkdownAndResult, presentationMarkdownAndResultHandler}


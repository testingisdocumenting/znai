import * as React from "react"

import CodeSnippetWithInlineComments from '../code-snippets/CodeSnippetWithInlineComments'
import SimpleCodeSnippet from '../code-snippets/SimpleCodeSnippet'
import {presentationRegistry} from '../presentation/PresentationRegistry'

import './Snippet.css'

// TODO a different way of registering for presentation

const Code = ({tokens, maxLineLength, commentsType}) => {
    const CodeSnippet = commentsType === 'inline' ? CodeSnippetWithInlineComments : SimpleCodeSnippet
    const divClassName = "snippet " + (maxLineLength && maxLineLength > 90 ? "wide-screen" : "content-block")

    return (<div className={divClassName}>
        <CodeSnippet tokens={tokens}/>
    </div>)
}

class Snippet extends React.Component {
    constructor(props) {
        super(props)
        presentationRegistry.register(Code, {...props}, 1)
    }

    render() {
        return <Code {...this.props}/>
    }
}

export default Snippet;

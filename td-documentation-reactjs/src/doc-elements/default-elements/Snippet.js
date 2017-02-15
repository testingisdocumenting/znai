import * as React from "react"
import hljs from 'highlightjs'
import 'highlightjs/styles/atom-one-light.css'
import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const {snippet, lang, maxLineLength} = this.props

        const divClassName = "snippet " + (maxLineLength && maxLineLength > 90 ? "wide-screen" : "content-block")
        const preClassName = "hljs" + ((lang && lang.length) ? " " + lang : "")

        return (<div className={divClassName}>
            <pre className={preClassName} ref={(dn) => this.codeNode = dn}>
                {snippet}
            </pre>
        </div>)
    }

    componentDidMount() {
        // delay highlight as the first time is a warm-up period and may delay react rendering
        // it will be especially noticeable during search preview

        // server side rendering guard
        if (window.setTimeout) {
            this.timeOut = setTimeout(() => hljs.highlightBlock(this.codeNode), 0);
        }
    }

    componentWillUnmount() {
        if (this.timeOut && window.clearTimeout) {
            clearTimeout(this.timeOut)
        }
    }
}

export default Snippet;

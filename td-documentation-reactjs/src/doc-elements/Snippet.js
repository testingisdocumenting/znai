import * as React from "react"
import hljs from 'highlightjs'
import 'highlightjs/styles/atom-one-light.css'
import './Snippet.css'

class Snippet extends React.Component {
    render() {
        const className = "hljs" + (this.props.lang.length ? " " + this.props.lang : "")
        return (<div className="snippet">
            <pre className={className} ref={(dn) => this.codeNode = dn}>
                {this.props.snippet}
            </pre>
        </div>)
    }

    componentDidMount() {
        setTimeout(() => hljs.highlightBlock(this.codeNode), 0)
    }
}

export default Snippet;

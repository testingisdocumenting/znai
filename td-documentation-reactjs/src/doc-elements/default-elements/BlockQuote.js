import React, {Component} from 'react'

const BlockQuoteBody = ({content, library}) => <blockquote className="content-block"><library.DocElement content={content}/></blockquote>

const BlockQuoteBodyWithLibrary = (library) => class BlockQuote extends Component {
    render() {
        return <BlockQuoteBody {...this.props} library={library}/>
    }
}

export default BlockQuoteBodyWithLibrary

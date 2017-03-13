import React, {Component} from 'react'
import {presentationRegistry} from '../presentation/PresentationRegistry'

const BlockQuoteBody = ({content, library}) => <blockquote className="content-block"><library.DocElement content={content}/></blockquote>

const BlockQuoteBodyWithLibrary = (library) => class Section  extends Component {
    constructor(props) {
        super(props)
        presentationRegistry.register(BlockQuoteBody, {...props, library: library}, 1)
    }

    render() {
        return <BlockQuoteBody {...this.props} library={library}/>
    }
}

export default BlockQuoteBodyWithLibrary

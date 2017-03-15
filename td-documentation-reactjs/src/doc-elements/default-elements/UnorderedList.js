import React, {Component} from 'react'

const UnorderedListWithLibrary = (library) => class UnorderedList extends Component {
    render() {
        const {tight, bulletMarker, content} = this.props
        const className = "content-block" + (tight ? " tight" : "")
        return (<ul className={className}><library.DocElement content={content}/></ul>)
    }
}

export default UnorderedListWithLibrary

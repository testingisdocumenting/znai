import React, {Component} from 'react'
import {presentationRegistry} from '../presentation/PresentationRegistry'

const UL = ({tight, bulletMarker, content, library}) => {
    const className = "content-block" + (tight ? " tight" : "")
    return (<ul className={className}><library.DocElement content={content}/></ul>)
}

const UnorderedListWithLibrary = (library) => class UnorderedList  extends Component {
    constructor(props) {
        super(props)
        presentationRegistry.register(UL, {...props, library: library}, 1)
    }

    render() {
        return <UL {...this.props} library={library}/>
    }
}

export default UnorderedListWithLibrary

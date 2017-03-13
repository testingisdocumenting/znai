import React, {Component} from 'react'
import {presentationRegistry} from '../presentation/PresentationRegistry'

const SectionBody = ({id, title, content, library}) => {
    return (
        <div className="section" key={title}>
            <div className="content-block">
                <div className="section-title" id={id}>{title}</div>
            </div>
            <library.DocElement content={content}/>
        </div>)
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const SectionWithLibrary = (library) => class Section  extends Component {
    constructor(props) {
        super(props)
        presentationRegistry.register(PresentationTitle, {...props}, 1)
    }

    render() {
        return <SectionBody {...this.props} library={library}/>
    }
}

export default SectionWithLibrary

import React, {Component} from 'react'

const SectionBody = ({id, title, content, library}) => {
    return (
        <div className="section" key={title}>
            <div className="content-block">
                <div className="section-title" id={id}>{title}</div>
            </div>
            <library.DocElement content={content}/>
        </div>)
}

const SectionWithLibrary = (library) => class Section  extends Component {
    render() {
        return <SectionBody {...this.props} library={library}/>
    }
}

export default SectionWithLibrary

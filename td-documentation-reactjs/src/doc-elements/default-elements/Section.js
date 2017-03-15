import React, {Component} from 'react'

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const Section = (library) => class BoundSection extends Component {
    render() {
        const {id, title, content} = this.props

        return (<div className="section" key={title}>
                <div className="content-block">
                    <div className="section-title" id={id}>{title}</div>
                </div>
                <library.DocElement content={content}/>
            </div>)

    }
}

const presentationSectionHandler = {component: PresentationTitle,
    numberOfSlides: () => 1}

export {Section, presentationSectionHandler}

import React, {Component} from 'react'
import {textSelection} from '../selected-text-extensions/TextSelection'

import './Section.css'

class Section extends Component {
    render() {
        const {id, title, ...props} = this.props

        const sectionTitle = (
            <div className="content-block">
                <props.elementsLibrary.SectionTitle id={id} title={title || ''}/>
            </div>
        )

        return (
            <div className="section" key={title} ref={n => this.node = n}>
                {sectionTitle}
                <props.elementsLibrary.DocElement {...props}/>
            </div>
        )
    }

    componentDidMount() {
        this.node.addEventListener('mousedown', this.mouseDownHandler)
    }

    componentWillUnmount() {
        this.node.removeEventListener('mousedown', this.mouseDownHandler)
    }

    mouseDownHandler = () => {
        const {id, title} = this.props

        textSelection.startSelection({pageSectionId: id, pageSectionTitle: title})
    }
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const presentationSectionHandler = {
    component: PresentationTitle,
    numberOfSlides: () => 1,
    slideInfoProvider: ({title}) => {
        return {sectionTitle: title}
    }
}

export {Section, presentationSectionHandler}

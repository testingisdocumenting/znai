import React, {Component} from 'react'
import {textSelection} from '../selected-text-extensions/TextSelection'

class Section extends Component {
    render() {
        const {id, title, ...props} = this.props

        const sectionTitle = title ? (
            <div className="content-block">
                <h1 className="section-title" id={id}>{title}</h1>
            </div>
        ) : null

        return (
            <div className="section" key={title} ref={n => this.node = n}>
                {sectionTitle}
                <props.elementsLibrary.DocElement {...props}/>
            </div>
        )
    }

    componentDidMount() {
        this.node.addEventListener('mouseup', this.mouseUpHandler)
    }

    componentWillUnmount() {
        this.node.removeEventListener('mouseup', this.mouseUpHandler)
    }

    mouseUpHandler = () => {
        const {title} = this.props

        const selection = window.getSelection()
        const rangeAt = selection.getRangeAt(0)

        const selectionStartNode = selection.isCollapsed ? null : rangeAt.startContainer.parentNode
        textSelection.notify({sectionTitle: "TODO",
            pageTitle: "TODO",
            pageSectionTitle: title,
            startNode: selectionStartNode,
            text: "todo text"})
    }
}

const PresentationTitle = ({title}) => {
    return <h1>{title}</h1>
}

const presentationSectionHandler = {component: PresentationTitle,
    numberOfSlides: () => 1,
    slideInfoProvider: ({title}) => {return {sectionTitle: title}}}

export {Section, presentationSectionHandler}

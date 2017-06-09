import React, {Component} from 'react'

class Page extends Component {
    render() {
        const {tocItem, onPresentationOpen, elementsLibrary} = this.props

        const displayTitle = tocItem.dirName.length && tocItem.fileName !== "index"
        const title = displayTitle ? [<span key="title" className="page-title">{tocItem.pageTitle}</span>,
            <span key="button" className="presentation-button glyphicon glyphicon-resize-full" onClick={onPresentationOpen}/>] : []

        return (<div className="page-content">
            <div className="page-title-block content-block">
                {title}
            </div>
            <elementsLibrary.DocElement key={tocItem.pageTitle}
                                        {...this.props}/>
        </div>)
    }

    shouldComponentUpdate(nextProps, nextState) {
        return this.props.previewEnabled ||
            this.props.tocItem.dirName !== nextProps.tocItem.dirName ||
            this.props.tocItem.fileName !== nextProps.tocItem.fileName
    }
}

const PresentationTitle = ({tocItem}) => {
    return <h1 className="presentation-title">{tocItem.pageTitle}</h1>
}

const presentationPageHandler = {component: PresentationTitle,
    numberOfSlides: () => 1,
    slideInfoProvider: ({tocItem}) => {return {pageTitle: tocItem.pageTitle}}}

export {Page, PresentationTitle, presentationPageHandler}

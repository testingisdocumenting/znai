import React, {Component} from 'react'

import './Page.css'

class Page extends Component {
    render() {
        const {tocItem, onPresentationOpen, elementsLibrary} = this.props

        const displayTitle = tocItem.dirName.length && tocItem.fileName !== "index"
        const title = displayTitle ? [<span key="title" className="page-title">{tocItem.pageTitle}</span>,
            onPresentationOpen ?
                <span key="button" className="presentation-button glyphicon glyphicon-resize-full"
                      onClick={onPresentationOpen}/> : null] : []

        return (<div className="page-content">
            <div className="page-title-block content-block">
                {title}
            </div>
            { this.renderModifiedTime() }

            <elementsLibrary.DocElement key={tocItem.pageTitle}
                                        {...this.props}/>
        </div>)
    }

    renderModifiedTime() {
        const {lastModifiedTime} = this.props

        if (!lastModifiedTime) {
            return null
        }

        const modifiedTimeAsStr = new Date(lastModifiedTime).toDateString()
        return (
            <div className="last-update-time content-block">
                {modifiedTimeAsStr}
            </div>
        )
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

import React, {Component} from 'react'

import './Page.css'
import {pageTypesRegistry} from './PageTypesRegistry'

class Page extends Component {
    render() {
        const {tocItem, onPresentationOpen, elementsLibrary, content} = this.props

        const displayTitle = tocItem.dirName.length && tocItem.fileName !== "index"
        const title = displayTitle ? [<span key="title" className="page-title">{tocItem.pageTitle}</span>,
            onPresentationOpen ?
                <span key="button" className="presentation-button glyphicon glyphicon-resize-full"
                      onClick={onPresentationOpen}/> : null] : []

        const PageContent = this.pageContentComponent()

        return (
            <div className="page-content">
                <div className="page-title-block content-block">
                    {title}
                </div>
                <div className="page-meta-block content-block">
                    { this.renderModifiedTime() }
                    { this.renderViewOn() }
                </div>

                <PageContent key={tocItem.pageTitle}
                             elementsLibrary={elementsLibrary}
                             content={content}/>
            </div>
        )
    }

    pageContentComponent() {
        const {tocItem} = this.props
        return pageTypesRegistry.pageContentComponent(tocItem)
    }

    renderModifiedTime() {
        const {lastModifiedTime} = this.props

        if (!lastModifiedTime) {
            return null
        }

        const modifiedTimeAsStr = new Date(lastModifiedTime).toDateString()
        return (
            <div className="last-update-time">
                {modifiedTimeAsStr}
            </div>
        )
    }

    renderViewOn() {
        const {docMeta} = this.props

        const viewOn = docMeta.viewOn
        if (!viewOn || !viewOn.link || !viewOn.title) {
            return null
        }

        return (
            <div className="view-on">
                <a href={this.buildViewOnLink(viewOn.link)} target="_bank">{viewOn.title}</a>
            </div>
        )
    }

    buildViewOnLink(link) {
        const {tocItem} = this.props
        return `${link}/${tocItem.dirName}/${tocItem.fileName}.md`
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

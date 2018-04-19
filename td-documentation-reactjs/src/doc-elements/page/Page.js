import React, {Component} from 'react'

import {pageTypesRegistry} from './PageTypesRegistry'

import './Page.css'

class Page extends Component {
    render() {
        const {tocItem} = this.props

        const PageContent = pageTypesRegistry.pageContentComponent(tocItem)
        const PageBottomPadding = pageTypesRegistry.pageBottomPaddingComponent(tocItem)

        return (
            <React.Fragment>
                <div className="page-content">
                    <PageContent key={tocItem.pageTitle}
                                 {...this.props}/>
                </div>
                <PageBottomPadding/>
            </React.Fragment>
        )
    }

    shouldComponentUpdate(nextProps) {
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

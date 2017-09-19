import React, { Component } from 'react'

import TocPanel from './structure/TocPanel'
import SelectedTextActionSelection from './selected-text-extensions/SelectedTextActionSelection'
import {selectedTextExtensions} from './selected-text-extensions/SelectedTextExtensions'

class DocumentationLayout extends Component {
    constructor(props) {
        super(props)

        this.state = {
            tocCollapsed: false,
            tocSelected: false,
            searchActive: false}

        this.onTocToggle = this.onTocToggle.bind(this)
        this.onSearchClick = this.onSearchClick.bind(this)
        this.onPanelSelect = this.onPanelSelect.bind(this)
    }

    render() {
        const {
            previewTracker,
            searchPopup,
            renderedPage,
            renderedFooter,
            docMeta,
            selectedTocItem,
            toc,
            onHeaderClick,
            onTocItemClick,
            onNextPage,
            onPrevPage,
            textSelection,
            pageGenError} = this.props

        const {
            tocCollapsed,
            tocSelected} = this.state

        const displaySelectedTextActions = textSelection && textSelection.startNode && selectedTextExtensions.hasExtensions()
        const pageGenErrorPanel = pageGenError ? (<div className="page-gen-error">{pageGenError}</div>) : null

        return (
            <div className="documentation">
                <div className="side-panel" onClick={this.onTocSelect}>
                    <TocPanel toc={toc} collapsed={tocCollapsed} selected={tocSelected}
                              docMeta={docMeta}
                              onToggle={this.onTocToggle}
                              selectedItem={selectedTocItem}
                              onHeaderClick={onHeaderClick}
                              onTocItemClick={onTocItemClick}
                              onNextPage={onNextPage}
                              onPrevPage={onPrevPage}/>
                </div>

                {previewTracker}

                <div className="search-button glyphicon glyphicon-search" onClick={this.onSearchClick}/>

                {searchPopup}

                <div className="main-panel" onClick={this.onPanelSelect}>
                    <div className="page-content-wrapper">
                        {renderedPage}
                    </div>

                    <div className="page-bottom">
                        <div className="next-prev-buttons content-block">
                            {this.renderPreviousPageButton()}
                            {this.renderNextPageButton()}
                        </div>
                        {renderedFooter}
                    </div>
                </div>

                {pageGenErrorPanel}
                {displaySelectedTextActions && <SelectedTextActionSelection textSelection={textSelection}/>}
            </div>
        )
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }

    onSearchClick() {
        this.setState({tocSelected: false})
        this.props.onSearchClick();
    }

    onPanelSelect() {
        this.setState({tocSelected: false})
    }

    renderNextPageButton() {
        const {nextPageToc, onNextPage} = this.props

        return (DocumentationLayout.needRenderNavigationButton(nextPageToc) ? (
            <div className="page-navigation-button-and-text" onClick={onNextPage}>
                <span className="next-prev-page-title">{nextPageToc.pageTitle} </span>
                <span className="glyphicon glyphicon-chevron-right"/>
            </div>) : <div/>)
    }

    renderPreviousPageButton() {
        const {prevPageToc, onPrevPage} = this.props

        return (DocumentationLayout.needRenderNavigationButton(prevPageToc) ? (
            <div className="page-navigation-button-and-text" onClick={onPrevPage}>
                <span className="glyphicon glyphicon-chevron-left"/>
                <span className="next-prev-page-title">{prevPageToc.pageTitle} </span>
            </div>) : <div/>)
    }

    static needRenderNavigationButton(tocItem) {
        // we don't render next/prev buttons that will point to items without dir name (e.g. index page)
        return tocItem && tocItem.dirName;
    }
}

export default DocumentationLayout
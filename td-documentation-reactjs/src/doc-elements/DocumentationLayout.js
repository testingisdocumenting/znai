import React, { Component } from 'react'

import TocPanel from './structure/TocPanel'
import SelectedTextActionSelection from './selected-text-extensions/SelectedTextActionSelection'
import {selectedTextExtensions} from './selected-text-extensions/SelectedTextExtensions'

class DocumentationLayout extends Component {
    state = {
        tocCollapsed: false,
        tocSelected: false,
        searchActive: false
    }

    render() {
        const {
            previewTracker,
            searchPopup,
            renderedPage,
            renderedNextPrevNavigation,
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
                    <TocPanel toc={toc}
                              collapsed={tocCollapsed}
                              selected={tocSelected}
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
                    {renderedPage}

                    <div className="page-bottom">
                        {renderedNextPrevNavigation}
                        {renderedFooter}
                    </div>
                </div>

                {pageGenErrorPanel}
                {displaySelectedTextActions && <SelectedTextActionSelection textSelection={textSelection}
                                                                            extensions={selectedTextExtensions.extensions()}/>}
            </div>
        )
    }

    onTocToggle = (collapsed) => {
        this.setState({ tocCollapsed: collapsed })
    }

    onSearchClick = () => {
        this.setState({tocSelected: false})
        this.props.onSearchClick();
    }

    onPanelSelect = () => {
        this.setState({tocSelected: false})
    }
}

export default DocumentationLayout
import React, {Component} from 'react'

import TocPanel from './structure/toc/TocPanel'
import SelectedTextActionSelection from './selected-text-extensions/SelectedTextActionSelection'
import {selectedTextExtensions} from './selected-text-extensions/SelectedTextExtensions'

class DocumentationLayout extends Component {
    state = {
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
            onTocItemPageSectionClick,
            onNextPage,
            onPrevPage,
            textSelection,
            pageGenError} = this.props

        const displaySelectedTextActions = textSelection && textSelection.startNode && selectedTextExtensions.hasExtensions()
        const pageGenErrorPanel = pageGenError ? (<div className="page-gen-error">{pageGenError}</div>) : null

        console.log('selectedTocItem', selectedTocItem)

        return (
            <div className="documentation">
                <div className="side-panel" onClick={this.onTocSelect}>
                    <TocPanel toc={toc}
                              docMeta={docMeta}
                              selectedItem={selectedTocItem}
                              onHeaderClick={onHeaderClick}
                              onTocItemClick={onTocItemClick}
                              onTocItemPageSectionClick={onTocItemPageSectionClick}
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

    onSearchClick = () => {
        this.setState({tocSelected: false})
        this.props.onSearchClick();
    }

    onPanelSelect = () => {
        this.setState({tocSelected: false})
    }
}

export default DocumentationLayout
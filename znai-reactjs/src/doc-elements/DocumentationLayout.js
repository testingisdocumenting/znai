/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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

        return (
            <div className="documentation">
                <div className="side-panel" onClick={this.onTocSelect}>
                    <TocPanel toc={toc}
                              docMeta={docMeta}
                              selectedItem={selectedTocItem}
                              onHeaderClick={onHeaderClick}
                              onTocItemClick={onTocItemClick}
                              onTocItemPageSectionClick={onTocItemPageSectionClick}
                              onSearchClick={this.onSearchClick}
                              onNextPage={onNextPage}
                              onPrevPage={onPrevPage}/>
                </div>

                {previewTracker}

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
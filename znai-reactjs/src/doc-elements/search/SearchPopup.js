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

import SearchBox from './SearchBox'
import SearchToc from './SearchToc'
import SearchPreview from './SearchPreview'
import Search from './Search'

class SearchPopup extends Component {
    constructor(props) {
        super(props)

        this.state = { selectedIdx: 0, search: null }
        this.onQueryChange = this.onQueryChange.bind(this)
        this.keyDownHandler = this.keyDownHandler.bind(this)
    }

    queryResultIds() {
        const queryResult = this.state.queryResult
        return queryResult ? queryResult.getIds() : []
    }

    startResolvingSearch() {
        // search is a promise because it requires json data for index and all pages
        //
        this.props.searchPromise.then((search) => {
            this.setState({search})
        }, (error) => {
            console.error("can't resolve search: " + error)
        })
    }

    render() {
        const {onClose, tocCollapsed} = this.props
        const {search} = this.state

        const ids = this.queryResultIds()

        this.ids = ids

        const hasResult = ids.length > 0
        const selectedIdx = this.state.selectedIdx
        const firstId = hasResult ? ids[selectedIdx] : null
        const previewDetails = hasResult ? search.previewDetails(firstId, this.state.queryResult) : null

        const className = "znai-search-popup" + (tocCollapsed ? "" : " visible-toc") + (hasResult ? " with-results" : "")
        const searchBox = search ? <SearchBox onChange={this.onQueryChange} /> : null

        return (
            <div className={className}>
                <div className="znai-search-overlay" onClick={onClose} />

                <div className="znai-search-popup-panel">
                    {searchBox}
                    {previewDetails ? this.renderPreview(ids, selectedIdx, previewDetails) : null}
                </div>
            </div>
        );
    }

    renderPreview(ids, selectedIdx, previewDetails) {
        const {elementsLibrary} = this.props
        const {search} = this.state

        return (
            <div className="toc-and-preview">
                <div className="znai-search-toc-panel">
                    <SearchToc ids={ids}
                               selectedIdx={selectedIdx}
                               search={search}
                               onSelect={this.changeSelectedIdx}
                               onJump={this.jumpToIdx}/>
                </div>
                <div className="znai-search-preview-panel">
                    <SearchPreview key={selectedIdx} elementsLibrary={elementsLibrary} {...previewDetails}/>
                </div>
            </div>
        )
    }

    onQueryChange(query) {
        const queryResult = this.state.search.search(query)
        const selectedIdx = 0
        this.setState({queryResult, selectedIdx})
    }

    componentDidMount() {
        this.startResolvingSearch()
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }

    changeSelectedIdx = (idx) => {
        this.setState({selectedIdx: idx})
    }

    jumpToIdx = (idx) => {
        const ids = this.queryResultIds()
        const tocToNavigate = Search.convertIndexIdToSectionCoords(ids[idx])

        this.props.onSearchSelection(tocToNavigate)
    }

    keyDownHandler(e) {
        const ids = this.queryResultIds()
        let selectedIdx = this.state.selectedIdx

        if (e.key === 'Escape') {
            if (this.props.onClose) {
                this.props.onClose()
            }
        }

        if (e.key === 'Enter' && ids.length > 0) {
            this.jumpToIdx(selectedIdx)
        }

        if (e.key !== 'ArrowUp' && e.key !== 'ArrowDown') {
            return
        }

        e.preventDefault()

        if (e.key === 'ArrowUp') {
            selectedIdx -= 1
        }

        if (e.key === 'ArrowDown') {
            selectedIdx += 1
        }

        if (selectedIdx < 0) {
            selectedIdx = 0
        }

        if (selectedIdx >= ids.length) {
            selectedIdx = this.ids.length - 1
        }

        this.setState({selectedIdx})
    }
}

export default SearchPopup
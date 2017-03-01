import React, { Component } from 'react'

import SearchBox from './SearchBox'
import SearchToc from './SearchToc'
import SearchPreview from './SearchPreview'

class SearchPopup extends Component {
    constructor(props) {
        super(props)

        this.state = { searchQuery: "", selectedIdx: 0, search: null }
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
        const {onClose} = this.props
        const ids = this.queryResultIds()

        this.ids = ids

        const hasResult = ids.length > 0
        const selectedIdx = this.state.selectedIdx
        const firstId = hasResult ? ids[selectedIdx] : null
        const previewDetails = hasResult ? this.state.search.previewDetails(firstId, this.state.queryResult) : null

        const searchBox = this.state.search ? <SearchBox onChange={this.onQueryChange} /> : null

        return (<div className="search-popup">
            <div className="overlay" onClick={onClose} />

            <div className="popup-panel">
                {searchBox}

                <div className="close glyphicon glyphicon-remove" onClick={onClose}/>
                {previewDetails ? this.renderPreview(ids, selectedIdx, previewDetails) : null}
            </div>
        </div>);
    }

    renderPreview(ids, selectedIdx, previewDetails) {
        return (<div className="toc-and-preview">
            <div className="search-toc-panel">
                <SearchToc ids={ids} selectedIdx={selectedIdx}/>
            </div>
            <div className="search-preview-panel">
                <SearchPreview {...previewDetails}/>
            </div>
        </div>)
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

    keyDownHandler(e) {
        const ids = this.queryResultIds()
        let selectedIdx = this.state.selectedIdx

        if (e.key === 'Escape') {
            if (this.props.onClose) {
                this.props.onClose()
            }
        }

        if (e.key === 'Enter' && ids.length > 0) {
            const tocToNavigate = JSON.parse(ids[selectedIdx])
            this.props.onSearchSelection(tocToNavigate)
        }

        if (e.key !== 'ArrowUp' && e.key !== 'ArrowDown') {
            return
        }

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
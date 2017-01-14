import React, { Component } from 'react'

import SearchBox from './SearchBox'
import SearchToc from './SearchToc'
import SearchPreview from './SearchPreview'

class SearchPopup extends Component {
    constructor(props) {
        super(props)

        this.search = this.props.search
        this.state = { searchQuery: "", selectedIdx: 0 }
        this.onQueryChange = this.onQueryChange.bind(this)
        this.keyDownHandler = this.keyDownHandler.bind(this)
    }

    queryResultIds() {
        const queryResult = this.state.queryResult
        return queryResult ? queryResult.getIds() : []
    }

    render() {
        const {onClose} = this.props
        const ids = this.queryResultIds()

        this.ids = ids

        const hasResult = ids.length > 0
        const selectedIdx = this.state.selectedIdx
        console.log("selectedIdx", selectedIdx)
        const firstId = hasResult ? ids[selectedIdx] : null
        const previewDetails = hasResult ? this.search.previewDetails(firstId, this.state.queryResult) : null

        return (<div className="search-popup">
            <div className="overlay" onClick={onClose} />

            <div className="popup-panel">
                <SearchBox onChange={this.onQueryChange} />
                <div className="close" onClick={onClose}>&times;</div>

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
        const queryResult = this.search.search(query)
        const selectedIdx = 0
        this.setState({queryResult, selectedIdx})

        console.log(query)
        console.log(queryResult)
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }

    keyDownHandler(e) {
        let selectedIdx = this.state.selectedIdx
        console.log(e)

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

        const ids = this.queryResultIds()

        if (selectedIdx >= ids.length) {
            selectedIdx = this.ids.length - 1
        }

        this.setState({selectedIdx})
    }
}

export default SearchPopup
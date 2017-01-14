import React, { Component } from 'react'
import SearchPreview from './SearchPreview'

class SearchBox extends Component {
    constructor(props) {
        super(props)
        this.state = { value: "" }
        this.onInputChange = this.onInputChange.bind(this)
    }

    render() {
        return <div className="search-box">
            <input
                ref={(dom) => this.dom = dom}
                placeholder="Type to search..."
                value={this.state.value} onChange={this.onInputChange} />

        </div>;
    }

    componentDidMount() {
        this.dom.focus();
    }

    // TODO debounce? 
    onInputChange(e) {
        const value = e.target.value
        this.props.onChange(value)
        this.setState({ value })
    }
}

class SearchPopup extends Component {
    constructor(props) {
        super(props)

        this.search = this.props.search
        this.state = { searchQuery: "maven" }
        this.onQueryChange = this.onQueryChange.bind(this)
    }

    render() {
        const {onClose} = this.props;
        const queryResult = this.state.queryResult
        const hasResult = queryResult && queryResult.getIds().length > 0
        const firstId = hasResult ? queryResult.getIds()[0] : null
        const previewDetails = hasResult ? this.search.previewDetails(firstId, queryResult) : null

        console.log("previewDetails", previewDetails)

        return (<div className="search-popup">
            <div className="overlay" onClick={onClose} />

            <div className="popup-panel">
                <SearchBox onChange={this.onQueryChange} />
                <div className="close" onClick={onClose}>&times;</div>
                <div className="toc-and-preview">
                    <div className="search-toc-panel">
                        TOC
                    </div>
                    <div className="search-preview-panel">
                        {hasResult ? <SearchPreview {...previewDetails}/> : <span>no preview to display</span>}
                    </div>
                </div>
            </div>
        </div>);
    }

    onQueryChange(query) {
        const queryResult = this.search.search(query)
        this.setState({queryResult})

        console.log(query)
        console.log(queryResult)
    }
}

export default SearchPopup
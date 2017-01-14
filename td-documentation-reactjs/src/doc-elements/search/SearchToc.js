import React, {Component} from 'react'

const SearchTocItem = ({sectionTitle, pageTitle, pageSectionTitle, isSelected}) => {
    const className = "search-toc-item" + (isSelected ? " selected" : "")

    return (<div className={className}>
        <span className="search-toc-page-title">{pageTitle}</span>
        <span className="search-toc-page-section-title">{pageSectionTitle}</span>
        <span className="search-toc-section-title">{sectionTitle}</span>
    </div>)
}

class SearchToc extends Component {
    render() {
        const {ids, selectedIdx} = this.props

        return (<div className="search-toc-items">{ids.map((id, idx) => <SearchTocItem key={id} {...JSON.parse(id)} isSelected={idx === selectedIdx}/>)}</div>)
    }
}

export default SearchToc
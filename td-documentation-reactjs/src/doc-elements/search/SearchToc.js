import React, {Component} from 'react'

const SearchTocItem = ({pt, pst, isSelected, idx, onSelect, onJump}) => {
    const className = "search-toc-item" + (isSelected ? " selected" : "")

    return (
        <div className={className}
             onClick={() => onSelect(idx)}
             onDoubleClick={() => onJump(idx)}>
            <span className="search-toc-page-title">{pt}</span>
            <span className="search-toc-page-section-title">{pst}</span>
        </div>
    )
}

class SearchToc extends Component {
    render() {
        const {ids, selectedIdx, onSelect, onJump} = this.props

        return (
            <div className="search-toc-items">
                {ids.map((id, idx) => <SearchTocItem key={id}
                                                     idx={idx}
                                                     {...JSON.parse(id)}
                                                     isSelected={idx === selectedIdx}
                                                     onSelect={onSelect}
                                                     onJump={onJump}/>)}
            </div>
        )
    }
}

export default SearchToc
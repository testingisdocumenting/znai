import React, {Component} from 'react'

export default class SearchToc extends Component {
    render() {
        const {ids, selectedIdx, onSelect, onJump, search} = this.props

        return (
            <div className="search-toc-items">
                {ids.map((id, idx) => {
                    const searchEntry = search.findSearchEntryById(id)
                    return (
                        <SearchTocItem key={id}
                                       idx={idx}
                                       pageTitle={searchEntry.pageTitle}
                                       pageSection={searchEntry.pageSection}
                                       isSelected={idx === selectedIdx}
                                       onSelect={onSelect}
                                       onJump={onJump}/>
                    )
                })}
            </div>
        )
    }
}

function SearchTocItem({pageTitle, pageSection, isSelected, idx, onSelect, onJump}) {
    const className = "search-toc-item" + (isSelected ? " selected" : "")

    return (
        <div className={className}
             onClick={() => onSelect(idx)}
             onDoubleClick={() => onJump(idx)}>
            <span className="search-toc-page-title">{pageTitle}</span>
            <span className="search-toc-page-section-title">{pageSection}</span>
        </div>
    )
}
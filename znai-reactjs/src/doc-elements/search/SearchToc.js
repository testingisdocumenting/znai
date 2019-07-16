import React, {Component} from 'react'
import SearchTocItem from './SearchTocItem'

export default class SearchToc extends Component {
    render() {
        const {ids, selectedIdx, onSelect, onJump, search} = this.props

        return (
            <div className="mdoc-search-toc-items">
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

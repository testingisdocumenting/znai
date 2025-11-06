/** @jsx React.createElement */
import React from 'react'
import SearchTocItem from './SearchTocItem'

const SearchToc = ({ids, search, onClick}) => {
    return (
        <div className="znai-search-toc">
            {ids.map((id, idx) => {
                const searchEntry = search.findSearchEntryById(id)
                return (
                    <SearchTocItem 
                        key={id}
                        idx={idx}
                        pageTitle={searchEntry.pageTitle}
                        section={searchEntry.section}
                        onSearch={onClick}
                    />
                )
            })}
        </div>
    )
}

export default SearchToc
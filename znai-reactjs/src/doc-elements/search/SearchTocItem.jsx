import React from 'react'

const SearchTocItem = ({ section, onSearch }) => {
    const handleClick = React.useCallback(() => {
        if (onSearch) {
            onSearch(section)
        }
    }, [section, onSearch])

    if (!section) {
        return null
    }

    return React.createElement('div', 
        { 
            className: 'znai-search-toc-item',
            onClick: handleClick 
        },
        React.createElement('div', 
            { className: 'znai-search-toc-section' },
            section.title
        ),
        section.pageTitle && React.createElement('div', 
            { className: 'znai-search-toc-page-title' },
            section.pageTitle
        )
    )
}

export default SearchTocItem
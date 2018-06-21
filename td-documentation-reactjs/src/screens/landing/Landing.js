import React from 'react'

import FilterInput from './FilterInput'

import './Landing.css'

export default class Landing extends React.Component {
    state = {filterText: ''}

    render() {
        const {documentations} = this.props
        const {filterText} = this.state

        const filteredDocumentations = filterDocumentations(documentations, filterText)

        const categoriesWithDocs = groupByCategory(filteredDocumentations)

        return (
            <div className="mdoc-landing">
                <FilterInput filterText={filterText} onChange={this.onFilterChange}/>
                {
                    categoriesWithDocs.map(categoryWithDocs => <CategoryWithDocs key={categoryWithDocs.category}
                                                                                 category={categoryWithDocs.category}
                                                                                 documentations={categoryWithDocs.documentations}/>)
                }
            </div>
        )
    }

    onFilterChange = (e) => {
        this.setState({filterText: e.target.value})
    }
}

function CategoryWithDocs({category, documentations}) {
    return (
        <div className="mdoc-landing-category-with-documentations">
            <Category category={category}/>
            <Documentations documentations={documentations}/>
        </div>
    )
}

function Category({category}) {
    return (
        <div className="mdoc-landing-category">
            <div className="small-line"/>
            <div className="category">{category}</div>
            <div className="large-line"/>
        </div>
    )
}

function Documentations({documentations}) {
    return (
        <div className="mdoc-landing-documentations">
            {documentations.map(d => <Documentation key={d.id} documentation={d}/>)}
        </div>
    )
}

function Documentation({documentation}) {
    return (
        <div className="mdoc-landing-documentation" onClick={() => navigateToDoc(documentation.id)}>
            <div className="name">{documentation.name}</div>
            <div className="description">{documentation.description}</div>
        </div>
    )
}

function navigateToDoc(id) {
    window.location = '/' + id;
}

function filterDocumentations(documentations, filterText) {
    filterText = filterText.toLowerCase()
    return documentations.filter(d => {
        return textMatch(d.name, filterText) ||
            textMatch(d.category, filterText) ||
            textMatch(d.description, filterText)
    })
}

function textMatch(text, lowerCaseFilter) {
    return text.toLowerCase().indexOf(lowerCaseFilter) !== -1
}

function groupByCategory(documentations) {
    const groups = {}
    documentations.forEach(d => {
        let entries = groups[d.category]
        if (typeof entries === 'undefined') {
            entries = []
            groups[d.category] = entries
        }

        entries.push(d)
    })

    const sortedKeys = Object.keys(groups).sort()
    return sortedKeys.map(category => ({category, documentations: groups[category].sort()}))
}

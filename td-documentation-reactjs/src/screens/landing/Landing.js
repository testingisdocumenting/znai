import React from 'react'

import FilterInput from './FilterInput'

import TocPanel from '../../doc-elements/structure/TocPanel'

import './Landing.css'

export default class Landing extends React.Component {
    state = {filterText: '', tocCollapsed: false}

    render() {
        const {documentations, type, title} = this.props
        const {filterText, tocCollapsed} = this.state

        const landingDocMeta = {type: type, title: title}

        const filteredDocumentations = filterDocumentations(documentations, filterText)

        const categoriesWithDocs = groupByCategory(filteredDocumentations)
        const documentationsToc = buildToc(categoriesWithDocs)

        return (
            <div className="mdoc-landing">
                <div className="mdoc-landing-categories-toc-area">
                    <TocPanel toc={documentationsToc}
                              docMeta={landingDocMeta}
                              collapsed={tocCollapsed}
                              onToggle={this.tocCollapseToggle}/>
                </div>

                <div className="mdoc-landing-documentations-area">
                    <FilterInput filterText={filterText} onChange={this.onFilterChange}/>

                    <div className="mdoc-landing-categories">
                        {
                            categoriesWithDocs.map(categoryWithDocs => <CategoryWithDocs key={categoryWithDocs.category}
                                                                                         category={categoryWithDocs.category}
                                                                                         documentations={categoryWithDocs.documentations}/>)
                        }
                    </div>
                </div>
            </div>
        )
    }

    tocCollapseToggle = () => {
        this.setState(prev => ({
            tocCollapsed: !prev.tocCollapsed
        }))
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
        <div className="mdoc-landing-category" id={anchorIdFromName(category)}>
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
    const url = documentation.url ?
        documentation.url:
        documentation.id

    return (
        <div className="mdoc-landing-documentation">
            <a href={url} target="_blank" rel="noopener noreferrer">
                <div className="name">{documentation.name}</div>
                <div className="description">{documentation.description}</div>
            </a>
        </div>
    )
}

function buildToc(categoriesWithDocs) {
    const items = categoriesWithDocs.map(withDocs => ({
        sectionTitle: "Categories",
        pageTitle: withDocs.category,
        fileName: withDocs.category,
        dirName: "categories",
        pageSectionIdTitles: [],
        href: "#" + anchorIdFromName(withDocs.category)
    }))

    return [
        {
            sectionTitle: "Categories",
            dirName: "categories",
            items: items
        }
    ]
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

function anchorIdFromName(name) {
    return name.toLowerCase().replace(' ', '-')
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

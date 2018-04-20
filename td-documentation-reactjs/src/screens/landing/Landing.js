import React from 'react'

import './Landing.css'

export default function Landing({documentations}) {
    const categoriesWithDocs = groupByCategory(documentations)

    return (
        <div className="landing">
            {
                categoriesWithDocs.map(categoryWithDocs => <CategoryWithDocs key={categoryWithDocs.category}
                                                                             category={categoryWithDocs.category}
                                                                             documentations={categoryWithDocs.documentations}/>)
            }
        </div>
    )
}

function CategoryWithDocs({category, documentations}) {
    return (
        <div className="landing-category-with-documentations">
            <Category category={category}/>
            <Documentations documentations={documentations}/>
        </div>
    )
}

function Category({category}) {
    return (
        <div className="landing-category">
            <div className="small-line"/>
            <div className="category">{category}</div>
            <div className="large-line"/>
        </div>
    )
}

function Documentations({documentations}) {
    return (
        <div className="landing-documentations">
            {documentations.map(d => <Documentation key={d.id} documentation={d}/>)}
        </div>
    )
}

function Documentation({documentation}) {
    return (
        <div className="landing-documentation" onClick={() => navigateToDoc(documentation.id)}>
            <div className="name">{documentation.name}</div>
            <div className="description">{documentation.description}</div>
        </div>
    )
}

function navigateToDoc(id) {
    window.location = '/' + id;
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

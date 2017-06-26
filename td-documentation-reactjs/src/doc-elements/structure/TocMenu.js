import React from 'react'
import {documentationNavigation} from '../structure/DocumentationNavigation'

const PageSections = ({pageSectionIdTitles, selected}) => {
    return (<div className="page-sections">
        {pageSectionIdTitles.map((idTitle, idx) => {
            const isSelected = idTitle.id === selected.pageSectionId
            const className = "page-section" + (isSelected ? " selected" : "")
            const href = "#" + idTitle.id

            return (<div className={className} key={idx}><a href={href}>{idTitle.title}</a></div>)
        })
        }
    </div>)
}

const Item = ({item, selected, isSelected, onClickHandler}) => {
    const className = "toc-item " + (isSelected ? "selected" : "")
    const href = documentationNavigation.buildUrl(item)
    return (
        <div className={className}>
            <a href={href} onClick={ (e) => { e.preventDefault(); onClickHandler(item.dirName, item.fileName)}}>{item.pageTitle}</a>
            {isSelected ? <PageSections pageSectionIdTitles={item.pageSectionIdTitles}
                                        selected={selected}/> : null}
        </div>
    );
};

const Section = ({section, selected, onClickHandler}) => {
    const className = "toc-section " + (section.dirName === selected.dirName ? "selected" : "")

    return (<div className={className}>
        <div className="title">{section.sectionTitle}</div>
        {section.items.map((item) => <Item key={item.fileName}
                                           item={item}
                                           selected={selected}
                                           isSelected={section.dirName === selected.dirName && item.fileName === selected.fileName}
                                           onClickHandler={onClickHandler} />)}
    </div>);
};

const TocMenu = ({toc, selected, onClickHandler}) => {
    selected = selected || {dirName: "", fileName: ""}

    // we won't render items that don't belong to a section. it includes things like top index.html or other misc filesD
    return (
        <div className="toc-menu">
            {toc.filter(sectionEntry => sectionEntry.dirName.length > 0).map((sectionEntry) =>
                <Section key={sectionEntry.sectionTitle}
                         selected={selected}
                         onClickHandler={onClickHandler}
                         section={sectionEntry} />)}
        </div>
    );
};

export default TocMenu
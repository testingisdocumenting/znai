import React, {PureComponent} from 'react'
import {documentationNavigation} from '../DocumentationNavigation'
import {pageTypesRegistry} from '../../page/PageTypesRegistry'

const PageSections = ({pageSectionIdTitles, selected, onTocItemPageSectionClick}) => {
    return (<div className="page-sections">
        {pageSectionIdTitles.map((idTitle, idx) => {
            const onClick = (e) => { e.preventDefault(); onTocItemPageSectionClick(idTitle.id) }

            const isSelected = idTitle.id === selected.pageSectionId
            const className = "page-section" + (isSelected ? " selected" : "")
            const href = "#" + idTitle.id

            return (
                <div className={className} key={idx} onClick={onClick}>
                    <a href={href}>{idTitle.title}</a>
                </div>
            )
        })
        }
    </div>)
}

class Item extends PureComponent {
    render() {
        const {item, selected, isSelected, onTocItemClick, onTocItemPageSectionClick} = this.props

        const className = 'toc-item' + (isSelected ? ' selected' : '')
        const href = item.href || documentationNavigation.buildUrl(item)

        const displayPageSections = isSelected && pageTypesRegistry.expandToc(item)

        const onClick = onTocItemClick ? (e) => { e.preventDefault(); onTocItemClick(item.dirName, item.fileName)} : null

        return (
            <div className={className} ref={this.saveNodeRef}>
                <a href={href} onClick={onClick}>{item.pageTitle}</a>
                {displayPageSections && <PageSections pageSectionIdTitles={item.pageSectionIdTitles}
                                                      selected={selected}
                                                      onTocItemPageSectionClick={onTocItemPageSectionClick}/>}
            </div>
        )
    }

    saveNodeRef = (node) => {
        this.node = node
    }

    componentDidMount() {
        this.scrollIntoView()
    }

    componentDidUpdate() {
        this.scrollIntoView()
    }

    scrollIntoView() {
        const {isSelected} = this.props

        if (isSelected) {
            this.node.scrollIntoView({block: 'nearest'})
        }
    }
}

const Section = ({section, selected, onTocItemClick, onTocItemPageSectionClick}) => {
    const className = 'toc-section' + (section.dirName === selected.dirName ? ' selected' : '')

    return (
        <div className={className}>
            <div className="title">{section.sectionTitle}</div>
            {section.items.map((item) => <Item key={item.fileName}
                                               item={item}
                                               selected={selected}
                                               isSelected={item.dirName === selected.dirName && item.fileName === selected.fileName}
                                               onTocItemClick={onTocItemClick}
                                               onTocItemPageSectionClick={onTocItemPageSectionClick}/>)}
        </div>
    )
}

const TocMenu = ({toc, selected, onTocItemClick, onTocItemPageSectionClick}) => {
    selected = selected || {dirName: "", fileName: ""}

    // we won't render items that don't belong to a section. it includes things like top index.html or other misc files
    return (
        <div className="toc-menu">
            {toc.filter(sectionEntry => sectionEntry.dirName.length > 0).map((sectionEntry) =>
                <Section key={sectionEntry.sectionTitle}
                         selected={selected}
                         onTocItemClick={onTocItemClick}
                         onTocItemPageSectionClick={onTocItemPageSectionClick}
                         section={sectionEntry} />)}
        </div>
    )
}

export default TocMenu
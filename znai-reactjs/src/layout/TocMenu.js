/*
 * Copyright 2022 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React, {PureComponent} from 'react'
import {documentationNavigation} from '../structure/DocumentationNavigation'
import {pageTypesRegistry} from '../doc-elements/page/PageTypesRegistry'

const PageSections = ({pageSectionIdTitles, selected, onTocItemPageSectionClick}) => {
    return (<div className="page-sections">
        {pageSectionIdTitles.map((idTitle, idx) => {
            const onClick = () => onTocItemPageSectionClick(idTitle.id)

            const isSelected = idTitle.id === selected.anchorId
            const className = "page-section" +
              (isSelected ? " selected" : "") +
              (idTitle.style ? " " + idTitle.style : "")

            const href = "#" + idTitle.id

            return (
                <div className={className} key={idx} onClick={onClick}>
                    <a href={href}>{idTitle.title}</a>
                </div>
            )
        })}
    </div>)
}

class Item extends PureComponent {
    render() {
        const {item, selected, isSelected, onTocItemClick, onTocItemPageSectionClick} = this.props

        const className = 'toc-item' + (isSelected ? ' selected' : '')
        const href = item.href || documentationNavigation.buildUrl(item)

        const displayPageSections = isSelected && pageTypesRegistry.expandToc(item)

        const onClick = onTocItemClick ? (e) => {
            e.preventDefault();
            onTocItemClick(item.dirName, item.fileName)
        } : null

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
            <div className="title">{section.chapterTitle}</div>
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
                <Section key={sectionEntry.chapterTitle}
                         selected={selected}
                         onTocItemClick={onTocItemClick}
                         onTocItemPageSectionClick={onTocItemPageSectionClick}
                         section={sectionEntry}/>)}
        </div>
    )
}

export default TocMenu
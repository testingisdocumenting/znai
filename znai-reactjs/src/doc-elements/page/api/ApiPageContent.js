/*
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

import React, {Component} from 'react'
import ApiSection from './ApiSection'

import {documentationNavigation} from '../../../structure/DocumentationNavigation'

import './ApiPageContent.css'

/**
 * for API pages we treat each section as a single unit definition (function, rest call, etc)
 * @param elementsLibrary elements library to use to render page content
 * @param content content that consist of list of doc elements
 * @returns API page content
 */
class ApiPageContent extends Component {
    state = {
        activeSectionId: '',
    }

    nodeBySectionId = {}
    heightBySectionId = {}

    render() {
        const {elementsLibrary, content} = this.props
        const {activeSectionId} = this.state

        return (
            <div className="api-sections">
                {content.map(section => {
                    const isSelected = activeSectionId === section.id
                    return (
                        <div key={section.id}
                             ref={this.onSectionRefCallBack(section.id)}
                             className="api-section content-block">
                            <ApiSection isSelected={isSelected}
                                        height={this.sectionHeight(section.id)}
                                        elementsLibrary={elementsLibrary}
                                        {...section}/>
                        </div>)
                })}
            </div>
        )
    }

    sectionHeight = (id) => {
        const height = this.heightBySectionId[id]
        return height ? height: null
    }

    onSectionRefCallBack = (id) => (node) => {
        this.nodeBySectionId[id] = node
    }

    componentDidMount() {
        this.calcSectionHeights()
        this.updateActiveSectionId()
        documentationNavigation.addUrlChangeListener(this.onUrlChange)
    }

    componentWillUnmount() {
        documentationNavigation.removeUrlChangeListener(this.onUrlChange)
    }

    onUrlChange = () => {
        this.updateActiveSectionId()
    }

    calcSectionHeights() {
        Object.keys(this.nodeBySectionId).forEach(id => {
            const node = this.nodeBySectionId[id]
            this.heightBySectionId[id] = node.offsetHeight
        })
    }

    updateActiveSectionId() {
        this.setState({activeSectionId: documentationNavigation.currentPageLocation().anchorId})
    }
}

export default ApiPageContent

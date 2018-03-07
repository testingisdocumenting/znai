import React, {Component} from 'react'
import ApiSection from './ApiSection'

import {documentationNavigation} from '../../structure/DocumentationNavigation'

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

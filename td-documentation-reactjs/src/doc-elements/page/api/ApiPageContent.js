import React, {Component} from 'react'
import ApiSection from './ApiSection'

import './ApiPageContent.css'
import {documentationNavigation} from '../../structure/DocumentationNavigation'

/**
 * for API pages we treat each section as a single unit definition (function, rest call, etc)
 * @param elementsLibrary elements library to use to render page content
 * @param content content that consist of list of doc elements
 * @returns API page content
 */
class ApiPageContent extends Component {
    state = {activeSectionId: 'part-of-workflow'}

    render() {
        const {elementsLibrary, content} = this.props
        const {activeSectionId} = this.state

        return (
            <div className="api-sections">
                {content.map(section => {
                    const isSelected = activeSectionId === section.id
                    return <ApiSection key={section.title}
                                       isSelected={isSelected}
                                       elementsLibrary={elementsLibrary}
                                       {...section}/>
                })}
            </div>
        )
    }

    componentDidMount() {
        this.updateActiveSectionId()
        documentationNavigation.addUrlChangeListener(this.onUrlChange)
    }

    componentWillUnmount() {
        documentationNavigation.removeUrlChangeListener(this.onUrlChange)
    }

    onUrlChange = () => {
        this.updateActiveSectionId()
    }

    updateActiveSectionId() {
        this.setState({activeSectionId: documentationNavigation.currentPageLocation().anchorId})
    }
}

export default ApiPageContent

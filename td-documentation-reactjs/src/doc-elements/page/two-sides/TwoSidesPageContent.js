import React, {Component} from 'react'
import TwoSidesSection from './TwoSidesSection'
import {TwoSidesLayout, LeftPart, RightPart} from './TwoSidesLayout'
import PageTitle from '../PageTitle'

import './TwoSidesPageContent.css'

/**
 * Two Sides page content
 * @param elementsLibrary elements library to use to render page content
 * @param content content that consist of list of doc elements
 * @returns two sides page content
 */
class TwoSidesPageContent extends Component {
    render() {
        const {elementsLibrary, content, ...props} = this.props

        return (
            <div className="two-sides-page-content">
                <TwoSidesLayout>
                    <LeftPart>
                        <PageTitle {...props} elementsLibrary={elementsLibrary}/>
                    </LeftPart>

                    <RightPart/>
                </TwoSidesLayout>

                {content.map(section =>
                    <TwoSidesSection key={section.id}
                                     elementsLibrary={elementsLibrary}
                                     {...section}/>
                )}
            </div>
        )
    }
}

export default TwoSidesPageContent

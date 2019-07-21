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
import TwoSidesSection from './TwoSidesSection'
import {TwoSidesLayout, TwoSidesLayoutLeftPart, TwoSidesLayoutRightPart} from './TwoSidesLayout'
import PageTitle from '../PageTitle'

import {contentTabNames} from '../../tabs/tabsUtils'

import TwoSidesTabs from './TwoSidesTabs'
import TwoSidesTabsSelection from './TwoSidesTabsSelection'

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

        const updatedElementsLibrary = {...elementsLibrary, Tabs: TwoSidesTabs}
        const tabNames = contentTabNames(content)

        const hasTabs = tabNames.length > 0
        const className = 'two-sides-page-content' + (hasTabs ? ' with-tabs' : '')

        return (
            <div className={className}>
                <TwoSidesLayout>
                    <TwoSidesLayoutLeftPart>
                        <PageTitle {...props} elementsLibrary={elementsLibrary}/>
                    </TwoSidesLayoutLeftPart>

                    <TwoSidesLayoutRightPart>
                        {hasTabs && <TwoSidesTabsSelection tabNames={tabNames}/>}
                    </TwoSidesLayoutRightPart>
                </TwoSidesLayout>

                {content.map(section =>
                    <TwoSidesSection key={section.id}
                                     elementsLibrary={updatedElementsLibrary}
                                     {...section}/>
                )}
            </div>
        )
    }
}

export default TwoSidesPageContent

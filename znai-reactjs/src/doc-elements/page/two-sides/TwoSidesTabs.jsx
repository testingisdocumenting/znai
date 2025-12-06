/*
 * Copyright 2025 znai maintainers
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

import React from 'react'
import {tabsRegistration} from '../../tabs/TabsRegistration'

class TwoSidesTabs extends React.Component {
    constructor(props) {
        super(props)

        const {tabsContent} = this.props
        const names = tabsContent.map(t => t.name)

        const tabName = tabsRegistration.firstMatchFromHistory(names);
        const idx = names.indexOf(tabName)

        this.state = {activeIdx: idx >= 0 ? idx : 0}
    }

    render() {
        const {elementsLibrary, tabsContent} = this.props

        const activeIdx = this.state.activeIdx
        const tabContent = tabsContent[activeIdx].content

        return (
            <elementsLibrary.DocElement {...this.props} content={tabContent}/>
        )
    }

    onTabSwitch = ({tabName}) => {
        const {tabsContent} = this.props
        const names = tabsContent.map(t => t.name)

        const idx = names.indexOf(tabName)
        if (idx !== -1) {
            this.setState({activeIdx: idx})
        }
    }

    componentDidMount() {
        tabsRegistration.addTabSwitchListener(this.onTabSwitch)
    }

    componentWillUnmount() {
        tabsRegistration.removeTabSwitchListener(this.onTabSwitch)
    }
}

export default TwoSidesTabs
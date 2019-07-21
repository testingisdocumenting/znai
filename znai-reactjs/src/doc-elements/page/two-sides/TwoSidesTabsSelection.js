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

import React from 'react'

import {tabsRegistration} from '../../tabs/TabsRegistration'
import './TwoSidesTabsSelection.css'

class TwoSidesTabsSelection extends React.Component {
    constructor(props) {
        super(props)

        const {tabNames} = this.props
        const tabName = tabsRegistration.firstMatchFromHistory(tabNames);

        const idx = tabNames.indexOf(tabName)
        this.state = {activeTabName: idx >= 0 ? tabNames[idx] : tabNames[0]}
    }

    render() {
        const {tabNames} = this.props
        const {activeTabName} = this.state

        return (
            <div className="two-sides-tabs-selection">
                {tabNames.map(name => <Tab key={name} name={name} isActive={name === activeTabName}/>)}
            </div>
        )
    }

    onTabSwitch = ({tabName}) => {
        const {tabNames} = this.props

        const idx = tabNames.indexOf(tabName)
        if (idx !== -1) {
            this.setState({activeTabName: tabName})
        }
    }

    componentDidMount() {
        tabsRegistration.addTabSwitchListener(this.onTabSwitch)
    }

    componentWillUnmount() {
        tabsRegistration.removeTabSwitchListener(this.onTabSwitch)
    }
}

function Tab({name, isActive}) {
    const onClick = () => tabsRegistration.notifyNewTab({tabName: name})
    const className = 'two-sides-tab-name' + (isActive ? ' active' : '')

    return (
        <div className={className} onClick={onClick}>
            {name}
        </div>
    )
}

export default TwoSidesTabsSelection

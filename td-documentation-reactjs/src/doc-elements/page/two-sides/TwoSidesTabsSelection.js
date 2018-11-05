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

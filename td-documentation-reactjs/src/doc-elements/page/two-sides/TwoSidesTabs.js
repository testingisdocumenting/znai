import React, {Component} from 'react'
import {tabsRegistration} from '../../tabs/TabsRegistration'

class TwoSidesTabs extends Component {
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

    onTabSwitch = (tabName) => {
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
import React, {Component} from 'react'

import './Tabs.css'

class TabsRegistration {
    constructor() {
        this.listeners = []
        this.tabsSelectionHistory = []
    }

    addTabSwitchListener(listener) {
        this.listeners.push(listener)
    }

    removeTabSwitchListener(listener) {
        removeFromArray(this.listeners, listener)
    }

    firstMatchFromHistory(names) {
        const matches = this.tabsSelectionHistory.filter(n => names.indexOf(n) >= 0)
        return matches ? matches[0] : names[0]
    }

    notifyNewTab(name) {
        removeFromArray(this.tabsSelectionHistory, name)
        this.tabsSelectionHistory.unshift(name)

        this.listeners.forEach(l => l(name))
    }
}

const tabsRegistration = new TabsRegistration()

const TabNames = ({names, activeIdx, onClick}) => {
    return <div className="tabs">
        <div className="tabs-names-area">
            <div className="tabs-names">
                {names.map((name, idx) => {
                    const className = "tab-name" + (idx === activeIdx ? " active" : "")
                    return <span key={idx} className={className} onClick={() => onClick(idx)}>{name}</span>
                })}
            </div>
        </div>
    </div>
}

class Tabs extends Component {
    constructor(props) {
        super(props)

        const {tabsContent} = this.props
        const names = tabsContent.map(t => t.name)

        const tabName = tabsRegistration.firstMatchFromHistory(names);

        const idx = names.indexOf(tabName)
        this.state = {activeIdx: idx >= 0 ? idx : 0}

        this.onClick = this.onClick.bind(this)
        this.onTabSwitch = this.onTabSwitch.bind(this)
    }

    componentDidMount() {
        tabsRegistration.addTabSwitchListener(this.onTabSwitch)
    }

    componentWillUnmount() {
        tabsRegistration.removeTabSwitchListener(this.onTabSwitch)
    }

    render() {
        const {elementsLibrary, tabsContent} = this.props
        const {activeIdx} = this.state

        const names = tabsContent.map(t => t.name)
        const tabContent = tabsContent[activeIdx].content

        return (<div className="tabs-area">
            <TabNames names={names} activeIdx={activeIdx} onClick={this.onClick}/>
            <div className="tabs-content">
                <elementsLibrary.DocElement content={tabContent}/>
            </div>
            </div>)
    }

    onClick(idx) {
        const {tabsContent} = this.props
        tabsRegistration.notifyNewTab(tabsContent[idx].name)
    }

    onTabSwitch(tabName) {
        const {tabsContent} = this.props
        const names = tabsContent.map(t => t.name)

        const idx = names.indexOf(tabName)
        if (idx !== -1) {
            this.setState({activeIdx: idx})
        }
    }
}

function removeFromArray(array, value) {
    const idx = array.indexOf(value)
    if (idx !== -1) {
        array.splice(idx, 1)
    }
}

export default Tabs
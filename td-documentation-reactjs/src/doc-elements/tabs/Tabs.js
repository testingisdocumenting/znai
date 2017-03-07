import React, {Component} from 'react'

import './Tabs.css'

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

const Tabs = (elementsLibrary) => class Tabs extends Component {
    constructor(props) {
        super(props)

        this.state = {activeIdx: 0}
        this.onClick = this.onClick.bind(this)
    }

    render() {
        const {tabsContent} = this.props
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
        this.setState({activeIdx: idx})
    }
}

export default Tabs
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

import {tabsRegistration} from './TabsRegistration'
import {findParentWithScroll} from '../../utils/domNodes'

import './Tabs.css'

const TabNames = ({names, activeIdx, onClick}) => {
    return (
        <div className="tabs">
            <div className="tabs-names-area">
                <div className="tabs-names content-block">
                    {names.map((name, idx) => {
                        const className = "tab-name" + (idx === activeIdx ? " active" : "")
                        return <span key={idx} className={className} onClick={() => onClick(idx)}>{name}</span>
                    })}
                </div>
            </div>
        </div>
    )
}

class Tabs extends Component {
    constructor(props) {
        super(props)

        const {tabsContent, forcedTabIdx} = this.props
        const names = tabsContent.map(t => t.name)

        const tabName = tabsRegistration.firstMatchFromHistory(names);

        const idx = typeof forcedTabIdx !== 'undefined' ?
            forcedTabIdx:
            names.indexOf(tabName)

        this.state = {activeIdx: idx >= 0 ? idx : 0}
    }

    componentDidMount() {
        tabsRegistration.addTabSwitchListener(this.onTabSwitch)
    }

    componentWillUnmount() {
        tabsRegistration.removeTabSwitchListener(this.onTabSwitch)
    }

    render() {
        const {elementsLibrary, tabsContent, forcedTabIdx} = this.props

        const activeIdx = typeof forcedTabIdx !== 'undefined' ?
            forcedTabIdx :
            this.state.activeIdx

        const names = tabsContent.map(t => t.name)
        const tabContent = tabsContent[activeIdx].content

        return (
            <div className="tabs-area" ref={this.saveNode}>
                <TabNames names={names} activeIdx={activeIdx} onClick={this.onClick}/>
                <div className="tabs-content">
                    <elementsLibrary.DocElement {...this.props} content={tabContent}/>
                </div>
            </div>
        )
    }

    saveNode = (node) => {
        this.node = node
    }

    onClick = (idx) => {
        const {tabsContent} = this.props
        const {activeIdx} = this.state

        if (activeIdx === idx) {
            return
        }

        tabsRegistration.notifyNewTab({tabName: tabsContent[idx].name, triggeredNode: this.node})
    }

    onTabSwitch = ({tabName, triggeredNode}) => {
        const {tabsContent} = this.props
        const names = tabsContent.map(t => t.name)

        const idx = names.indexOf(tabName)
        if (idx !== -1) {
            this.setState({activeIdx: idx, triggeredNode})
        }
    }

    getSnapshotBeforeUpdate(prevProps, prevState) {
        if (prevState.activeIdx !== this.state.activeIdx &&
            this.node === this.state.triggeredNode) {

            const nodeRect = this.node.getBoundingClientRect()

            const parentWithScrollNode = findParentWithScroll(this.node)
            if (parentWithScrollNode === null) {
                return null
            }

            return {parentWithScrollNode: parentWithScrollNode, clientRect: nodeRect}
        }

        return null;
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (!snapshot) {
            return
        }

        const diffY = this.node.getBoundingClientRect().y - snapshot.clientRect.y
        snapshot.parentWithScrollNode.scrollTop += diffY
    }
}

const PresentationTabs = ({tabsContent, slideIdx, elementsLibrary}) => {
    return <Tabs elementsLibrary={elementsLibrary}
                 tabsContent={tabsContent}
                 forcedTabIdx={slideIdx}/>
}

const presentationTabsHandler = {component: PresentationTabs,
    numberOfSlides: ({tabsContent}) => tabsContent.length}

export {Tabs, presentationTabsHandler}

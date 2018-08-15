import React, {Component} from 'react'

import TocMenu from './TocMenu'
import TocHeader from './TocHeader'
import TocSettingsTogglePanel from './TocSettingsTogglePanel'
import TocSettings from './TocSettings'

class TocPanel extends Component {
    state = {
        collapsed: false,
        selected: false,
        displaySettings: false
    }

    render() {
        const {docMeta, onHeaderClick} = this.props
        const {collapsed, selected, displaySettings} = this.state

        const panelClass = 'toc-panel' + (collapsed ? ' collapsed' : '') + (selected ? ' selected' : '')

        return (
            <div className={panelClass}>
                <TocHeader docMeta={docMeta}
                           collapsed={collapsed}
                           onHeaderClick={onHeaderClick}
                           onCollapseToggle={this.collapseToggle}/>

                {this.renderPanelContent()}

                <TocSettingsTogglePanel onSettingsClick={this.onSettingsToggle} activated={displaySettings}/>
            </div>
        )
    }

    renderPanelContent() {
        const {
            toc,
            selectedItem,
            onTocItemClick,
            onTocItemPageSectionClick} = this.props

        const {displaySettings} = this.state

        if (displaySettings) {
            return <TocSettings/>
        }

        return (
            <TocMenu toc={toc}
                     selected={selectedItem}
                     onTocItemPageSectionClick={onTocItemPageSectionClick}
                     onTocItemClick={onTocItemClick}/>
        )
    }

    collapseToggle = () => {
        this.setState(prev => ({collapsed: !prev.collapsed}))
    }

    onSettingsToggle = () => {
        this.setState(prev => ({displaySettings: !prev.displaySettings}))
    }

    keyDownHandler = (e) => {
        const {selected, collapsed, onNextPage, onPrevPage} = this.props
        const {displaySettings} = this.state

        if (displaySettings && e.code === "Escape") {
            this.setState({displaySettings: false})
            return
        }

        if (!selected || collapsed) {
            return
        }

        if (e.key === 'ArrowUp') {
            onPrevPage()
        } else if (e.key === 'ArrowDown') {
            onNextPage()
        }
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }
}

export default TocPanel
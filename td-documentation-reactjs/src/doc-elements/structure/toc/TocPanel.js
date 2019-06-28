import React, {Component} from 'react'

import TocMenu from './TocMenu'
import TocHeader from './TocHeader'
import TocSettings from './TocSettings'
import TocPanelSearch from './TocPanelSearch'

class TocPanel extends Component {
    state = {
        collapsed: false,
        selected: false,
        displaySettings: false
    }

    render() {
        const {
            docMeta,
            onHeaderClick,
            toc,
            selectedItem,
            onTocItemClick,
            onTocItemPageSectionClick,
            onSearchClick,
        } = this.props

        const {
            collapsed,
            selected,
            displaySettings
        } = this.state

        const panelClass = 'toc-panel' +
            (collapsed ? ' collapsed' : '') +
            (selected ? ' selected' : '')

        return (
            <div className={panelClass}>
                <TocHeader docMeta={docMeta}
                           collapsed={collapsed}
                           onHeaderClick={onHeaderClick}
                           onCollapseToggle={this.collapseToggle}/>

                <div className="toc-panel-search-and-menu">
                    <TocPanelSearch onClick={onSearchClick}/>

                    <TocMenu toc={toc}
                             selected={selectedItem}
                             onTocItemPageSectionClick={onTocItemPageSectionClick}
                             onTocItemClick={onTocItemClick}/>
                </div>
                {!collapsed && <TocSettings active={displaySettings} onSettingsToggle={this.onSettingsToggle}/>}
            </div>
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

        if (displaySettings && e.code === 'Escape') {
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
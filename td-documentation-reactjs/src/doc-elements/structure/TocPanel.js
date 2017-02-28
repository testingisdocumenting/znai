import React, { Component } from 'react';
import TocMenu from './TocMenu';

import {documentationNavigation} from './DocumentationNavigation'

class TocPanel extends Component {
    constructor(props) {
        super(props);

        this.onTocItemClick = this.onTocItemClick.bind(this)
        this.toggle = this.toggle.bind(this)
        this.keyDownHandler = this.keyDownHandler.bind(this)
    }

    render() {
        const {docMeta, collapsed, selected} = this.props
        const panelClass = "toc-panel" + (collapsed ? " collapsed" : "") + (selected ? " selected" : "")
        const expandButtonClass = "toc-panel-expand-button glyphicon glyphicon-chevron-right " + (collapsed ? "appeared" : "")
        const collapseButtonClass = "toc-panel-collapse-button glyphicon glyphicon-chevron-left " + (!collapsed ? "appeared" : "")

        return (<div className={panelClass}>
                <div className="header">
                    <span className="toc-panel-header-title">{docMeta.title + " " + docMeta.type}</span>
                    <span className={collapseButtonClass} onClick={this.toggle}/>
                </div>
                <div className={expandButtonClass} onClick={this.toggle}/>
                <TocMenu toc={this.props.toc}
                         selected={this.props.selectedItem}
                         currentPageSection={this.props.currentPageSection}
                         onClickHandler={this.onTocItemClick} />
            </div>
        )
    }

    onTocItemClick(dirName, fileName) {
        documentationNavigation.navigateToPage({dirName, fileName})
    }

    toggle() {
        const collapsed = !this.props.collapsed
        this.props.onToggle(collapsed)
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }

    keyDownHandler(e) {
        const {selected, collapsed, onNextPage, onPrevPage} = this.props

        if (! selected || collapsed) {
            return
        }

        if (e.key === 'ArrowUp') {
            onPrevPage()
        } else if (e.key === 'ArrowDown') {
            onNextPage()
        }
    }
}

export default TocPanel
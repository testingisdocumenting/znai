import React, {Component} from 'react';
import TocMenu from './TocMenu';
import PanelCollapseButton from './PanelCollapseButton'

class TocPanel extends Component {
    render() {
        const {
            docMeta,
            toc,
            collapsed,
            selected,
            selectedItem,
            onTocItemClick,
            onTocItemPageSectionClick,
            onHeaderClick} = this.props

        const panelClass = "toc-panel" + (collapsed ? " collapsed" : "") + (selected ? " selected" : "")

        return (
            <div className={panelClass}>
                <div className="toc-panel-header">
                    <div className="toc-panel-header-logo-and-title">
                        <div className="mdoc-documentation-logo"/>
                        <div className="toc-panel-header-title"
                             onClick={onHeaderClick}>
                            {docMeta.title + " " + docMeta.type}
                        </div>
                    </div>
                    <PanelCollapseButton isCollapsed={collapsed} onClick={this.toggle}/>
                </div>
                <TocMenu toc={toc}
                         selected={selectedItem}
                         onTocItemPageSectionClick={onTocItemPageSectionClick}
                         onTocItemClick={onTocItemClick}/>
            </div>
        )
    }

    toggle = () => {
        const collapsed = !this.props.collapsed
        this.props.onToggle(collapsed)
    }

    keyDownHandler = (e) => {
        const {selected, collapsed, onNextPage, onPrevPage} = this.props

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
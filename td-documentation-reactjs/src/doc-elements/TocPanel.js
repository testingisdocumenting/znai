import React, { Component } from 'react';
import TocMenu from './TocMenu';

class TocPanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedItem: { sectionTitle: "", fileName: "" },
        };

        this.onTocItemClick = this.onTocItemClick.bind(this);
        this.toggle = this.toggle.bind(this);
    }

    render() {
        const {collapsed} = this.props
        const panelClass = "toc-panel " + (collapsed ? "collapsed" : "")
        const expandButtonClass = "toc-panel-expand-button " + (collapsed ? "appeared" : "")
        const collapseButtonClass = "toc-panel-collapse-button " + (!collapsed ? "appeared" : "")

        return (<div className={panelClass}>
            <div className="header">
                <span className="toc-panel-header-title">Table of Contents</span>
                <span className={collapseButtonClass} onClick={this.toggle}>&times;</span>
            </div>
            <div className={expandButtonClass} onClick={this.toggle}>&#9776;</div>
            <TocMenu toc={this.props.toc}
                selected={this.state.selectedItem}
                onClickHandler={this.onTocItemClick} />
        </div>
        )
    }

    onTocItemClick(sectionTitle, fileName) {
        console.log(sectionTitle + " " + fileName);
        this.setState({ selectedItem: { sectionTitle: sectionTitle, fileName: fileName } });
    }

    toggle() {
        const collapsed = !this.props.collapsed;
        this.props.onToggle(collapsed);
    }
};

export default TocPanel;
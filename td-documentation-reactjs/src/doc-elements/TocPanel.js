import React, { Component } from 'react';
import TocMenu from './TocMenu';

class TocPanel extends Component {
    constructor(props) {
        super(props);
        this.state = {
            selectedItem: { sectionTitle: "", fileName: "" },
            collapsed: props.collapsed
        };

        this.onTocItemClick = this.onTocItemClick.bind(this);
        this.toggle = this.toggle.bind(this);
    }

    render() {
        const panelClass = "toc-panel " + (this.state.collapsed ? "collapsed" : "");
        const expandButtonClass = "toc-panel-expand-button " + (this.state.collapsed ? "appeared" : "");
        const collapseButtonClass = "toc-panel-collapse-button " + (! this.state.collapsed ? "appeared" : "")

        return (<div>
            <div className={expandButtonClass} onClick={this.toggle}>&#9776;</div>
            <div className={panelClass}>
                <div className={collapseButtonClass} onClick={this.toggle}>&#9664;</div>
                <TocMenu toc={this.props.toc}
                    selected={this.state.selectedItem}
                    onClickHandler={this.onTocItemClick} />
            </div>
        </div>
        )
    }

    onTocItemClick(sectionTitle, fileName) {
        console.log(sectionTitle + " " + fileName);
        this.setState({ selectedItem: { sectionTitle: sectionTitle, fileName: fileName } });
    }

    toggle() {
        const collapsed = !this.state.collapsed;
        this.props.onToggle(collapsed);
        this.setState({ collapsed });
    }
};

export default TocPanel;
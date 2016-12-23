import React, { Component } from 'react';
import TocMenu from './TocMenu';

import TestData from './TestData';

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
        return this.state.collapsed ? this.renderCollapsed() : this.renderExpanded();
    }

    renderCollapsed() {
        return this.renderExpanded();
        // return (<div className="toc-panel collapsed" onClick={this.toggle}>
        //     B
        //     </div>)
    }

    renderExpanded() {
        const panelClassName = "toc-panel " + (this.state.collapsed ? "collapsed" : "");
        const toggleButtonClassName = "toc-panel-expand-button " + (this.state.collapsed ? "appeared" : "");

        return (<div>
            <div className={toggleButtonClassName} onClick={this.toggle}>T</div>
            <div className={panelClassName}>
                <div onClick={this.toggle}>collapse</div>
                <TocMenu toc={TestData.simpleToc}
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
        this.setState({ collapsed });
    }
};

export default TocPanel;
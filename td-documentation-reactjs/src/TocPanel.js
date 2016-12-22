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

        this.onClick = this.onClick.bind(this);
        this.toggle = this.toggle.bind(this);
    }

    render() {
        return this.state.collapsed ? this.renderCollapsed() : this.renderExpanded();
    }

    renderCollapsed() {
        return (<div>collapsed</div>)
    }

    renderExpanded() {
        return (<div className="toc-panel">
            <div onClick={this.toggle}>Collapse</div>
            <TocMenu toc={TestData.simpleToc}
                selected={this.state.selectedItem}
                onClickHandler={this.onClick} />
        </div>)
    }

    onClick(sectionTitle, fileName) {
        console.log(sectionTitle + " " + fileName);
        this.setState({ selectedItem: { sectionTitle: sectionTitle, fileName: fileName } });
    }
};

export default TocPanel;
import React, { Component } from "react"
import DocElement from "./DocElement"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import elementsLibrary from './DefaultElementsLibrary'

class Page extends Component {
    constructor(props) {
        super(props)
        this.state = { tocCollapsed: true }
        this.onTocToggle = this.onTocToggle.bind(this)
    }

    render() {
        const {title, toc, content, renderContext, docMeta} = this.props

        const tocClassModifier =  (this.state.tocCollapsed ? "without-toc" : "")
        const mainPanelClass = "main-panel " + tocClassModifier
        const pageContentClass = "page-content " + tocClassModifier
        
        return (
            <div className="page">
                <div className="side-panel">
                    <TocPanel toc={toc} collapsed={this.state.tocCollapsed} onToggle={this.onTocToggle} />
                </div>

                <div className={mainPanelClass}>
                    <NavBar renderContext={renderContext} docMeta={docMeta} tocCollapsed={this.state.tocCollapsed} />
                    <div className={pageContentClass}>
                        <div className="page-title">{title}</div>
                        <DocElement content={content} elementsLibrary={elementsLibrary} />
                    </div>
                </div>
            </div>)
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }
}

export default Page

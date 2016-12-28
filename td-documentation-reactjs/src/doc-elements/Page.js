import React, { Component } from "react"
import DocElement from "./DocElement"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import elementsLibrary from './DefaultElementsLibrary'

class Page extends Component {
    constructor(props) {
        super(props)
        this.state = {tocCollapsed: true}
        this.onTocToggle = this.onTocToggle.bind(this)
    }

    render() {
        const {title, toc, content, renderContext, docMeta} = this.props

        const contentClass = "page-content " + (this.state.tocCollapsed ? "without-toc" : "")
        return (
            <div className="page">
                <NavBar renderContext={renderContext} docMeta={docMeta} tocCollapsed={this.state.tocCollapsed}/>

                <TocPanel toc={toc} collapsed={this.state.tocCollapsed} onToggle={this.onTocToggle} />
                <div className={contentClass}>
                    <div className="page-title">{title}</div>
                    <DocElement content={content} elementsLibrary={elementsLibrary} />
                </div>
            </div>)
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }
}

export default Page

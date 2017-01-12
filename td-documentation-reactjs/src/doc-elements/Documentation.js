import React, { Component } from "react"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import elementsLibrary from './DefaultElementsLibrary'

class Documentation extends Component {
    constructor(props) {
        super(props)
        this.state = { tocCollapsed: true }
        this.onTocToggle = this.onTocToggle.bind(this)
    }

    render() {
        const {title, toc, page, docMeta} = this.props

        const tocClassModifier =  (this.state.tocCollapsed ? "without-toc" : "")
        const mainPanelClass = "main-panel " + tocClassModifier

        return (
            <div className="page">
                <div className="side-panel">
                    <TocPanel toc={toc} collapsed={this.state.tocCollapsed} onToggle={this.onTocToggle} />
                </div>

                <div className={mainPanelClass}>
                    <NavBar docMeta={docMeta} />
                    <elementsLibrary.Page title={title} content={page.content}/>
                </div>
            </div>)
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }
}

export default Documentation

import React, { Component } from "react"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import elementsLibrary from './DefaultElementsLibrary'

class Documentation extends Component {
    constructor(props) {
        super(props)

        this.searchPromise = getSearchPromise()

        this.state = { tocCollapsed: true }

        this.onTocToggle = this.onTocToggle.bind(this)
        this.onSearchClick = this.onSearchClick.bind(this)
        this.onSearchClose = this.onSearchClose.bind(this)
    }

    render() {
        const {toc, page, docMeta} = this.props

        const pageTitle = page.tocItem.pageTitle

        const tocClassModifier =  (this.state.tocCollapsed ? "without-toc" : "")
        const mainPanelClass = "main-panel " + tocClassModifier

        const searchPopup = this.state.searchActive ? <SearchPopup searchPromise={this.searchPromise}
                                                                   onClose={this.onSearchClose}/> : null

        return (
            <div className="page">
                <div className="side-panel">
                    <TocPanel toc={toc} collapsed={this.state.tocCollapsed} onToggle={this.onTocToggle} />
                </div>

                <div className="search-button glyphicon glyphicon-search" onClick={this.onSearchClick}/>

                {searchPopup}

                <div className={mainPanelClass}>
                    <NavBar docMeta={docMeta} pageTitle={pageTitle}/>
                    <elementsLibrary.Page title={pageTitle} content={page.content}/>
                </div>
            </div>)
    }

    onSearchClick() {
        this.setState({searchActive: true})
    }

    onSearchClose() {
        this.setState({searchActive: false})
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }
}

export default Documentation

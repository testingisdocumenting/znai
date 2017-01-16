import React, { Component } from "react"
import NavBar from './NavBar'
import TocPanel from './TocPanel'
import SearchPopup from './search/SearchPopup'
import {getSearchPromise} from './search/searchPromise'
import elementsLibrary from './DefaultElementsLibrary'
import {documentationNavigation} from './DocumentationNavigation'
import {getAllPagesPromise} from "./allPages"

import './DocumentationLayout.css'

class Documentation extends Component {
    constructor(props) {
        super(props)

        this.searchPromise = getSearchPromise()

        this.state = { tocCollapsed: false, page: this.props.page }

        this.onTocToggle = this.onTocToggle.bind(this)
        this.onSearchClick = this.onSearchClick.bind(this)
        this.onSearchClose = this.onSearchClose.bind(this)

        documentationNavigation.addUrlChangeListener(this.onUrlChange.bind(this))
    }

    render() {
        const {toc, docMeta} = this.props
        const {page} = this.state

        const pageTitle = page.tocItem.pageTitle

        const searchPopup = this.state.searchActive ? <SearchPopup searchPromise={this.searchPromise}
                                                                   onClose={this.onSearchClose}/> : null

        return (
            <div className="documentation">
                <div className="side-panel">
                    <TocPanel toc={toc} collapsed={this.state.tocCollapsed} onToggle={this.onTocToggle} />
                </div>

                <div className="search-button glyphicon glyphicon-search" onClick={this.onSearchClick}/>

                {searchPopup}

                <div className="main-panel">
                    <NavBar docMeta={docMeta} pageTitle={pageTitle}/>
                    <elementsLibrary.Page title={pageTitle} content={page.content}/>
                </div>
            </div>)
    }

    onSearchClick() {
        history.pushState({}, null, "/test")
        this.setState({searchActive: true})
    }

    onSearchClose() {
        this.setState({searchActive: false})
    }

    onTocToggle(collapsed) {
        this.setState({ tocCollapsed: collapsed })
    }

    onUrlChange(url) {
        console.log("@@", url)

        getAllPagesPromise().then((pages) => {
            const pageCoord = documentationNavigation.extractDirNameAndFileName(url)

            const matchingPages = pages.filter((p) => p.tocItem.dirName === pageCoord.dirName &&
                p.tocItem.fileName === pageCoord.fileName)

            if (! matchingPages) {
                console.error("can't find any page with", pageCoord)
                return
            }

            this.setState({page: matchingPages[0]})
        })
    }
}

export default Documentation

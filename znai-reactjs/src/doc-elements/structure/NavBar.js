import React from 'react'
import {fullResourcePath} from '../../utils/resourcePath'
import {documentationNavigation} from './DocumentationNavigation'

const NavBar = ({docMeta, tocItem}) => {
    const url = fullResourcePath(docMeta.id, "")
    const clickHandler = (e) => {
        e.preventDefault();
        documentationNavigation.navigateToUrl(url)
    }

    const title = (tocItem.dirName === "") ? "" : `// ${tocItem.pageTitle}`

    return (<nav className="top-navigation">
        <div className="menu">
            <a href={url} onClick={clickHandler}><span className="glyphicon glyphicon-home"/></a>
            <span className="menu-page-title">{title}</span>
        </div>
    </nav>)
}

export default NavBar
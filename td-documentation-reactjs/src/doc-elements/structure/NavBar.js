import React from 'react'
import {nestedPath} from '../../utils/renderContext'
import {documentationNavigation} from './DocumentationNavigation'

const NavBar = ({docMeta, tocItem, renderContext}) => {
    const url = nestedPath(renderContext.nestLevel, "")
    const clickHandler = (e) => {
        e.preventDefault();
        documentationNavigation.navigateToUrl(url)
    }

    const title = (tocItem.dirName === "") ? "" : `// ${tocItem.pageTitle}`

    return (<nav className="top-navigation">
        <div className="menu">
            <a href={url} onClick={clickHandler}> <strong>{docMeta.title}</strong> {docMeta.type} </a>
            <span className="menu-page-title">{title}</span>
        </div>
    </nav>)
}

export default NavBar
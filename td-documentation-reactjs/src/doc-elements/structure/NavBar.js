import React from 'react'
import {nestedPath} from '../../utils/renderContext'
import {documentationNavigation} from './DocumentationNavigation'

const NavBar = ({docMeta, pageTitle, renderContext}) => {
    const url = nestedPath(renderContext.nestLevel, "")
    const clickHandler = (e) => {
        e.preventDefault();
        documentationNavigation.navigateToUrl(url)
    }

    return (<nav className="top-navigation">
        <div className="menu">
            <a href={url} onClick={clickHandler}> <strong>{docMeta.title}</strong> {docMeta.type} </a>
            <span className="menu-page-title">// {pageTitle}</span>
        </div>
    </nav>)
}

export default NavBar
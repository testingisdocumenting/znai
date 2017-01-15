import React from 'react'

const NavBar = ({docMeta, pageTitle}) => {
    return (<nav className="top-navigation">
        <div className="menu">
            <strong>{docMeta.title}</strong> {docMeta.type}
            <span className="menu-page-title">// {pageTitle}</span>
        </div>
    </nav>)
}

export default NavBar
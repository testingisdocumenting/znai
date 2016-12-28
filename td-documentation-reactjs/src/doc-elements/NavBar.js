import React from 'react'

const NavBar = ({renderContext, docMeta, tocCollapsed}) => {
    const menuClass = "menu " + (tocCollapsed ? "without-toc": "")
    return (<nav className="top-navigation">
        <div className={menuClass}>
            <img alt="logo" src={nestedPath(docMeta.logo, renderContext.nestLevel)} height="48px" />
            <strong>{docMeta.title}</strong> {docMeta.type}
        </div>
    </nav>)
}

function nestedPath(path, nestLevel) {
    return [new Array(nestLevel)].map((idx) => "..").join("/") +
        (nestLevel === 0 ? "" : "/") + path
}

export default NavBar
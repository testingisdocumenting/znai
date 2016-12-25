import React from 'react'

const NavBar = ({renderContext, docMeta}) => {
    return (<nav className="navbar navbar-default navbar-static-top">
        <div className="container">
            <div className="navbar-header">
                <a className="navbar-left" href="#">
                    <img alt="logo" src={nestedPath(docMeta.logo, renderContext.nestLevel)} height="48px" />
                </a>
                <a className="navbar-brand" href="#">
                    <strong>{docMeta.title}</strong> {docMeta.type}
                </a>
            </div>
        </div>
    </nav>)
}

function nestedPath(path, nestLevel) {
    return [new Array(nestLevel)].map((idx) => "..").join("/") +
        (nestLevel === 0 ? "" : "/") + path
}

export default NavBar
import React from 'react'

const NavBar = ({docMeta}) => {
    return (<nav className="top-navigation">
        <div className="menu">
            <strong>{docMeta.title}</strong> {docMeta.type}
        </div>
    </nav>)
}

export default NavBar
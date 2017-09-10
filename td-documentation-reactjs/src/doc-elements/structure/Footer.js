import React from 'react'

import './Footer.css'

const Footer = (props) => {
    return (
        <div className="footer">
            <props.elementsLibrary.DocElement {...props}/>
        </div>
    )
}

export default Footer

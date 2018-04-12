import React from 'react'

import Icon from '../icons/Icon'

import './SectionTitle.css'

const SectionTitle = ({id, title}) => {
    return id ? (
        <h1 className="section-title" id={id}>{title}
            <a href={"#" + id}><Icon id="link"/></a>
        </h1>
    ) : (
        <h1 className="section-title" id='implicit-section'/>
    )
}

export default SectionTitle

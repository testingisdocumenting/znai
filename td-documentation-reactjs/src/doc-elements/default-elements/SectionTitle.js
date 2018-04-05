import React from 'react'

import Icon from '../icons/Icon'

import './SectionTitle.css'

const SectionTitle = ({id, title}) => {
    return (
            <h1 className="section-title" id={id}>{title}
                <a href={"#" + id}><Icon id="link"/></a>
            </h1>
    )
}

export default SectionTitle

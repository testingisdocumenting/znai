import React from 'react'
import RawHtml from '../default-elements/RawHtml'
import './LatexMath.css'

const LatexMath = ({svg, width}) => {
    const className = "latex-math" + (width > 700 ? " wide" : " content-block")
    return (<div className={className}>
        <RawHtml html={svg}/>
    </div>)
}

export default LatexMath
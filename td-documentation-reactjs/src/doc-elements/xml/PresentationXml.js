import React from 'react'
import Xml from './Xml'

const PresentationXml = ({xmlAsJson, paths, slideIdx, ...props}) => {
    const pathsToDisplay = paths.slice(0, slideIdx)
    return <Xml xmlAsJson={xmlAsJson} paths={pathsToDisplay} {...props}/>
}

export default {component: PresentationXml, numberOfSlides: ({data, paths}) => paths ? (paths.length + 1): 1}
import React from 'react'
import Json from './Json'

const PresentationJson = ({data, paths, slideIdx, ...props}) => {
    const pathsToDisplay = paths.slice(0, slideIdx)
    return <Json data={data} paths={pathsToDisplay} {...props}/>
}

export default {component: PresentationJson, numberOfSlides: ({data, paths}) => paths ? (paths.length + 1): 1}
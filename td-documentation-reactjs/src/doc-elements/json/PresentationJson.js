import React from 'react'
import Json from './Json'

const PresentationJson = ({data, paths, slideIdx}) => {
    const pathsToDisplay = paths.slice(0, slideIdx)
    return <Json data={data} paths={pathsToDisplay}/>
}

export default {component: PresentationJson, numberOfSlides: ({data, paths}) => paths ? (paths.length + 1): 1}
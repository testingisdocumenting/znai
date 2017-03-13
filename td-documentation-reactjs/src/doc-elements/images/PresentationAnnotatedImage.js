import React, {Component} from 'react'

import EmbeddedAnnotatedImage from './EmbeddedAnnotatedImage'
import './PresentationAnnotatedImage.css'

class PresentationAnnotatedImage extends Component {
    render() {
        const {imageSrc, shapes, slideIdx, onLoad} = this.props
        const shapesToDisplay = shapes.slice(0, slideIdx)

        return (<EmbeddedAnnotatedImage imageSrc={imageSrc} shapes={shapesToDisplay} onLoad={onLoad}/>)
    }
}

export default PresentationAnnotatedImage

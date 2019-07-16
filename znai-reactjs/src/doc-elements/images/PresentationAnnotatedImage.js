import React, {Component} from 'react'

import EmbeddedAnnotatedImage from './EmbeddedAnnotatedImage'
import './PresentationAnnotatedImage.css'

class PresentationAnnotatedImage extends Component {
    render() {
        const {shapes, slideIdx} = this.props
        const shapesToDisplay = shapes.slice(0, slideIdx)

        return (<EmbeddedAnnotatedImage {...this.props} shapes={shapesToDisplay}/>)
    }
}

export default {component: PresentationAnnotatedImage, numberOfSlides: ({shapes}) => shapes.length + 1}

import React, {Component} from 'react'

import AnnotatedImage from './AnnotatedImage'
import Annotations from './annotations/Annotations'
import './EmbeddedAnnotatedImage.css'

class EmbeddedAnnotatedImage extends Component {
    render() {
        const {imageSrc, shapes} = this.props
        const annotations = new Annotations(shapes)
        return (<div className="embedded-annotated-image">
            <AnnotatedImage imageSrc={imageSrc} annotations={annotations} {...this.props} isStatic="true"/>
        </div>)
    }
}

export default EmbeddedAnnotatedImage


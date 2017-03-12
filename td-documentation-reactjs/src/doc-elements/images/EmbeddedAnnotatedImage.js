import React from 'react'

import AnnotatedImage from './AnnotatedImage'
import Annotations from './annotations/Annotations'
import './EmbeddedAnnotatedImage.css'

const EmbeddedAnnotatedImage = ({imageSrc, shapes}) => {
    const annotations = new Annotations(shapes)
    return (<div className="embedded-annotated-image"><AnnotatedImage imageSrc={imageSrc} annotations={annotations} isStatic="true"/></div>)
}

export default EmbeddedAnnotatedImage


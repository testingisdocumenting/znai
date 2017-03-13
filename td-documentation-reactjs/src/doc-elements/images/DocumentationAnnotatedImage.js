import React, {Component} from 'react'

import EmbeddedAnnotatedImage from './EmbeddedAnnotatedImage'
import PresentationAnnotatedImage from './PresentationAnnotatedImage'

import {presentationRegistry} from '../presentation/PresentationRegistry'

class DocumentationAnnotatedImage extends Component {
    constructor(props) {
        super(props)

        const {shapes} = this.props
        presentationRegistry.register(PresentationAnnotatedImage, props, shapes.length)
    }

    render() {
        return (<EmbeddedAnnotatedImage {...this.props}/>)
    }
}

export default DocumentationAnnotatedImage


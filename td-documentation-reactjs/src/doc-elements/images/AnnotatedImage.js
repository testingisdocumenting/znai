import React, {Component} from 'react'

import {isPreviewEnabled} from '../docMeta'

class AnnotatedImage extends Component {
    constructor(props) {
        super(props)
    }

    render() {
        const {imageSrc, annotations, width, height, isStatic, selectedId, fit} = this.props

        const scale = fit ? 900.0/width : 1 // TODO theme with sizes. How to share with CSS, e.g. content-block?

        const scaledWidth = width * scale
        const scaledHeight = height * scale

        const imageWidth = scaledWidth + "px"
        const imageHeight = scaledHeight + "px"

        let parentStyle = {position: 'relative', width: imageWidth, height: imageHeight}
        const childrenStyle = {float: "left", position: "absolute", top: 0}

        const srcToUse = imageSrc + (isPreviewEnabled() ? "?time=" + new Date().getTime() : "")

        return (
            <div style={parentStyle} className="annotated-image" >
                <div style={childrenStyle}>
                    <img alt="annotated" src={srcToUse}
                         width={imageWidth}
                         height={imageHeight}
                         ref={node => this.imageNode = node}/>
                </div>
                <div style={childrenStyle}>
                    <svg width={imageWidth} height={imageHeight}>
                        {isStatic ?
                            annotations.staticAnnotationsToRender():
                            annotations.interactiveAnnotationsToRender(selectedId)}

                        <filter id="highlight">
                            <feColorMatrix values="1 0 1 0 0
                                               1 0 1 0 0
                                               1 0 1 0 0
                                               0 0 0 1 0"/>
                        </filter>
                    </svg>
                </div>
            </div>
        )
    }
}

export default AnnotatedImage
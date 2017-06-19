import React, {Component} from 'react'

import {isPreviewEnabled} from '../docMeta'

class AnnotatedImage extends Component {
    constructor(props) {
        super(props)
        this.state = {imageHeight: 100, imageWidth: 100,
            annotations: this.props.annotations}
    }

    render() {
        const {imageSrc, annotations, width, height, isStatic, selectedId} = this.props

        const imageWidth = width + "px"
        const imageHeight = height + "px"

        const parentStyle = {position: 'relative', width: imageWidth, height: imageHeight}
        const childrenStyle = {float: "left", position: "absolute", top: 0}

        const srcToUse = imageSrc + (isPreviewEnabled() ? "?time=" + new Date().getTime() : "")

        return (<div style={parentStyle} className="annotated-image" >
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
        </div>)
    }
}

export default AnnotatedImage
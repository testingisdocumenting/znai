import React, {Component} from 'react'

class AnnotatedImage extends Component {
    constructor(props) {
        super(props)
        this.state = {imageHeight: 100, imageWidth: 100,
            annotations: this.props.annotations}
    }

    render() {
        const {imageSrc, annotations, isStatic, selectedId} = this.props
        const {imageWidth, imageHeight} = this.state

        const svgWidth = imageWidth + "px"
        const svgHeight = imageHeight + "px"

        const parentStyle = {position: 'relative', width: imageWidth, height: imageHeight}
        const childrenStyle = {float: "left", position: "absolute", top: 0}

        return (<div style={parentStyle} className="annotated-image" >
            <div style={childrenStyle}>
                <img alt="annotated" src={imageSrc}
                     ref={node => this.imageNode = node}
                     onLoad={() => this.calcSize()}/>
            </div>
            <div style={childrenStyle}>
                <svg width={svgWidth} height={svgHeight}>
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

    calcSize() {
        const {onLoad} = this.props

        const imageWidth = this.imageNode.offsetWidth;
        const imageHeight = this.imageNode.offsetHeight;

        this.setState({imageWidth : imageWidth,
            imageHeight: imageHeight})

        if (onLoad) {
            onLoad(imageWidth, imageHeight)
        }
    }
}

export default AnnotatedImage
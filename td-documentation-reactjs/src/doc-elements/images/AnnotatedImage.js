import React, {Component} from 'react'
import annotation from './annotations/Annotation'
import CircleAnnotation from './annotations/CircleAnnotation'

const Circle = annotation(CircleAnnotation)

class AnnotatedImage extends Component {
    constructor(props) {
        super(props)
        this.state = {imageHeight: 100, imageWidth: 100}
    }

    render() {
        const {data} = this.props
        const {imageWidth, imageHeight} = this.state

        const svgWidth = imageWidth + "px"
        const svgHeight = imageHeight + "px"

        const imageBlockStyle = {float: "left"}
        const svgBlockStyle = {float: "left", position: "absolute", top: 0}

        return (<div className="annotated-image">
            <div style={imageBlockStyle}>
                <img alt="annotated" src={data.imageSrc} ref={node => this.imageNode = node} onLoad={() => this.calcSize()}/>
            </div>
            <div style={svgBlockStyle}>
                <svg width={svgWidth} height={svgHeight}>
                    <Circle x={100} y={100} r={20}/>
                </svg>
            </div>
        </div>)
    }

    calcSize() {
        this.setState({imageWidth : this.imageNode.offsetWidth,
            imageHeight: this.imageNode.offsetHeight})
    }
}

export default AnnotatedImage
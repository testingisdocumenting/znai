import React, {Component} from 'react'

class AnnotatedImage extends Component {
    constructor(props) {
        super(props)
        this.state = {imageHeight: 100, imageWidth: 100,
            annotations: this.props.annotations}
    }

    render() {
        const {data, annotations, selectedId} = this.props
        const {imageWidth, imageHeight} = this.state

        const svgWidth = imageWidth + "px"
        const svgHeight = imageHeight + "px"

        const imageBlockStyle = {float: "left"}
        const svgBlockStyle = {float: "left", position: "absolute", top: 0}

        return (<div className="annotated-image" >
            <div style={imageBlockStyle}>
                <img alt="annotated" src={data.imageSrc}
                     ref={node => this.imageNode = node}
                     onLoad={() => this.calcSize()}/>
            </div>
            <div style={svgBlockStyle}>
                <svg width={svgWidth} height={svgHeight}>
                    {annotations.interactiveAnnotationsToRender(selectedId)}

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
        this.setState({imageWidth : this.imageNode.offsetWidth,
            imageHeight: this.imageNode.offsetHeight})
    }
}

export default AnnotatedImage
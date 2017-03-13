import React, {Component} from 'react'
import AnnotatedImage from './AnnotatedImage'
import Annotations from './annotations/Annotations'
import ShapeInfo from './ShapeInfo'

import './AnnotatedImageEditor.css'

class AnnotatedImageEditor extends Component {
    constructor(props) {
        super(props)
        this.state = {selectedId: ''}

        this.annotations = new Annotations()

        this.annotations.add({type: 'circle', id: 'c1', x: 100, y: 100, r: 50, text: '1', color: 'red'})
        this.annotations.add({type: 'circle', id: 'c2', x: 150, y: 150, r: 30, text: '2', color: 'blue'})
        this.annotations.add({type: 'arrow', id: 'a1', beginX: 250, beginY: 150, endX: 300, endY: 200, text: 'look', color: 'green'})

        this.onAnnotationChange = this.onAnnotationChange.bind(this)
        this.onEditedShape = this.onEditedShape.bind(this)
        this.onAnnotationSelect = this.onAnnotationSelect.bind(this)
        this.onMouseDown = this.onMouseDown.bind(this)

        this.annotations.registerListener({
            onChange: this.onAnnotationChange,
            onSelection: this.onAnnotationSelect,
        })
    }

    render() {
        const {imageSrc} = this.props
        const {selectedId} = this.state
        const shapes = this.annotations.shapes

        return <div className="editor" onMouseDown={this.onMouseDown}>
            <div className="image-area">
                <AnnotatedImage imageSrc={imageSrc}
                                annotations={this.annotations}
                                selectedId={selectedId}/>
            </div>
            <div className="shapes-info-area">
                {shapes.map(shape => <ShapeInfo key={shape.id} shape={shape}
                                                onSelect={this.onAnnotationSelect}
                                                isSelected={shape.id === selectedId}
                                                onChange={this.onEditedShape}/>)}
            </div>
        </div>
    }

    onMouseDown() {
        if (! this.isSomethingSelectedOnClick) {
            this.setState({selectedId: ''})
        }

        this.isSomethingSelectedOnClick = false
    }

    onAnnotationSelect(shape) {
        this.isSomethingSelectedOnClick = shape
        this.setState({selectedId: shape.id})
    }

    onAnnotationChange(shape) {
        this.forceUpdate()
    }

    onEditedShape(shape) {
        this.annotations.updateShape(shape)
        this.forceUpdate()
    }
}

export default AnnotatedImageEditor

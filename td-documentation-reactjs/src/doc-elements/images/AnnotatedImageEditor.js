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

        this.annotations.add({type: 'circle', id: 'c1', x: 100, y: 100, r: 50})
        this.annotations.add({type: 'circle', id: 'c2', x: 150, y: 150, r: 30})

        this.onAnnotationChange = this.onAnnotationChange.bind(this)
        this.onAnnotationSelect = this.onAnnotationSelect.bind(this)
        this.onMouseDown = this.onMouseDown.bind(this)

        this.annotations.registerListener({
            onChange: this.onAnnotationChange,
            onSelection: this.onAnnotationSelect,
        })
    }

    render() {
        const {data} = this.props
        const {selectedId} = this.state
        const shapes = this.annotations.shapes

        return <div className="editor" onMouseDown={this.onMouseDown}>
            <div className="image-area">
                <AnnotatedImage data={data}
                                annotations={this.annotations}
                                selectedId={selectedId}
                                onImageClick={this.onAnnotationDeselect}/>
            </div>
            <div className="shapes-info-area">
                {shapes.map(shape => <ShapeInfo key={shape.id} shape={shape}
                                                onSelect={this.onAnnotationSelect}
                                                isActive={shape.id === selectedId}/>)}
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
}

export default AnnotatedImageEditor

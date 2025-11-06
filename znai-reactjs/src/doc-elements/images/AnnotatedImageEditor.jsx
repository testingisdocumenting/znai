/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'
import AnnotatedImage from './AnnotatedImage'
import Annotations from './annotations/Annotations'
import ShapeInfo from './ShapeInfo'

import './AnnotatedImageEditor.css'

class AnnotatedImageEditor extends React.Component {
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

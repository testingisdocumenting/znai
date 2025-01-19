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

import React, {Component} from 'react'
import Knob from './Knob'

const interactiveAnnotation = (shapeHandler, handlers) => class InteractiveAnnotation extends Component {
    constructor(props) {
        super(props)

        this.state = {isMouseOver: false}

        this.shapeHandler = shapeHandler;

        this.onPosChange = this.onPosChange.bind(this)
        this.onMouseOver = this.onMouseOver.bind(this)
        this.onMouseOut = this.onMouseOut.bind(this)

        this.onKnobDragStarted = this.onKnobDragStarted.bind(this)
        this.onKnobDragStopped = this.onKnobDragStopped.bind(this)

        this.onDragStarted = this.onDragStarted.bind(this)
        this.onDragStopped = this.onDragStopped.bind(this)
        this.onDrag = this.onDrag.bind(this)
    }

    componentDidMount() {
        document.addEventListener('mouseup', this.onDragStopped)
        document.addEventListener('mousemove', this.onDrag)
    }

    componentWillUnmount() {
        document.removeEventListener('mouseup', this.onDragStopped)
        document.removeEventListener('mousemove', this.onDrag)
    }

    onPosChange(knob) {
        const {shape} = this.props
        const newShape = this.shapeHandler.update(shape, knob.id, knob.dx, knob.dy)
        if (handlers.onChange) {
            handlers.onChange(newShape)
        }
    }

    render() {
        const {shape} = this.props

        const {isMouseOver} = this.state
        const isSelected = this.props.isSelected || isMouseOver || this.dragStarted || this.knobDragStarted

        const createKnob = k => <Knob
            key={k.id}
            onPosChange={this.onPosChange}
            onMouseOver={this.onMouseOver}
            onMouseOut={this.onMouseOut}
            onDragStarted={this.onKnobDragStarted}
            onDragStopped={this.onKnobDragStopped}
            {...k}/>

        const Body = this.shapeHandler.body;
        const bodyStyle = isSelected ? {filter: 'url(#highlight)'} : {}
        const knobsComponents = isSelected ? this.shapeHandler.knobs(shape).map(createKnob) : []

        this.elements = [<Body key="body"
                               onMouseOver={this.onMouseOver}
                               onMouseOut={this.onMouseOut}
                               onMouseDown={this.onDragStarted}
                               style={bodyStyle}
                               {...shape}/>,
            ...knobsComponents]

        return <g>
            {this.elements}
        </g>
    }

    onMouseOver() {
        if (this.moveOutTimer) {
            clearTimeout(this.moveOutTimer)
        }

        this.setState({isMouseOver: true})
    }

    onMouseOut() {
        this.moveOutTimer = setTimeout(() => this.setState({isMouseOver: false}), 100)
    }

    onDragStarted(e) {
        const {shape} = this.props

        this.lastDragX = e.nativeEvent.offsetX
        this.lastDragY = e.nativeEvent.offsetY
        this.dragStarted = true

        if (handlers.onSelection) {
            handlers.onSelection(shape)
        }
    }

    onDragStopped() {
        this.dragStarted = false
    }

    onDrag(e) {
        if (! this.dragStarted) {
            return
        }

        const newX = e.layerX
        const newY = e.layerY

        const dx = newX - this.lastDragX
        const dy = newY - this.lastDragY

        this.lastDragX = newX
        this.lastDragY = newY

        this.onPosChange({id: 'body', dx: dx, dy: dy})
    }

    onKnobDragStarted() {
        this.knobDragStarted = true
        const {shape} = this.props

        if (handlers.onSelection) {
            handlers.onSelection(shape)
        }
    }

    onKnobDragStopped() {
        this.knobDragStarted = false
    }
}

export default interactiveAnnotation
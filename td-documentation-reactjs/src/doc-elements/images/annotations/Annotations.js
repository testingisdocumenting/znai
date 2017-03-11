import React from 'react'

import interactiveAnnotation from './InteractiveAnnotation'
import circle from '../shapes/Circle'

const shapesLib = {circle}

class Annotations {
    constructor() {
        this.shapes = []
        this.listeners = []

        this.onChange = this.onChange.bind(this)
        this.onSelection = this.onSelection.bind(this)
    }

    registerListener(listener) {
        this.listeners.push(listener)
    }

    add(shape) {
        this.updateShape(shape)
    }

    updateShape(shape) {
        const idx = this.shapes.findIndex(s => s.id === shape.id)
        if (idx !== -1) {
            this.shapes[idx] = shape
        } else {
            this.shapes.push(shape)
        }
    }

    annotationsToRender(selectedId) {
        return this.shapes.map(shape => {
            const InteractiveAnnotation = interactiveAnnotationForType(shape.type, handlerForShapeType(shape), {
                onChange: this.onChange,
                onSelection: this.onSelection
            })
            return <InteractiveAnnotation key={shape.id} shape={shape} isSelected={selectedId === shape.id}/>
        })
    }

    onChange(shape) {
        this.updateShape(shape)
        this.listeners.forEach(l => l.onChange(shape))
    }

    onSelection(shape) {
        this.listeners.forEach(l => l.onSelection(shape))
    }
}

function handlerForShapeType(shape) {
    if (shapesLib.hasOwnProperty(shape.type)) {
        return shapesLib[shape.type]
    } else {
        console.error("can't find type for shape: " + shape.type)
    }
}

const interactiveAnnotationsByType = {}
function interactiveAnnotationForType(type, shapeHandler, onChange) {
    if (interactiveAnnotationsByType.hasOwnProperty(type)) {
        return interactiveAnnotationsByType[type]
    } else {
        const InteractiveAnnotation = interactiveAnnotation(shapeHandler, onChange)
        interactiveAnnotationsByType[type] = InteractiveAnnotation

        return InteractiveAnnotation
    }
}

export default Annotations

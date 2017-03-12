import React from 'react'

import interactiveAnnotation from './InteractiveAnnotation'
import staticAnnotation from './StaticAnnotation'

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

    staticAnnotationsToRender() {
        return this.shapes.map(shape => {
            const StaticAnnotation = staticAnnotationForShape(shape)
            return <StaticAnnotation key={shape.id} shape={shape}/>
        })
    }

    interactiveAnnotationsToRender(selectedId) {
        return this.shapes.map(shape => {
            const InteractiveAnnotation = interactiveAnnotationForShape(shape, {
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
function interactiveAnnotationForShape(shape, handlers) {
    return cachedAnnotationForType(interactiveAnnotationsByType, shape,  (shapeHandler) => interactiveAnnotation(shapeHandler, handlers))
}

const staticAnnotationsByType = {}
function staticAnnotationForShape(shape) {
    return cachedAnnotationForType(staticAnnotationsByType, shape,  (shapeHandler) => staticAnnotation(shapeHandler))
}

function cachedAnnotationForType(cache, shape, createFunc) {
    const type = shape.type
    if (cache.hasOwnProperty(type)) {
        return cache[type]
    } else {
        const Annotation = createFunc(handlerForShapeType(shape))
        cache[type] = Annotation

        return Annotation
    }
}

export default Annotations

/*
 * Copyright 2021 znai maintainers
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

import interactiveAnnotation from './InteractiveAnnotation'
import staticAnnotation from './StaticAnnotation'

import {circle, badge} from '../shapes/Circle'
import rectangle from '../shapes/Rectangle'
import arrow from '../shapes/Arrow'
import highlight from '../shapes/Highlight'

const shapesLib = {circle, badge, rectangle, arrow, highlight}

class Annotations {
    constructor(shapes) {
        this.shapes = shapes || []
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

    staticAnnotationsToRender(scale) {
        return this.shapes.map(shape => {
            const StaticAnnotation = staticAnnotationForShape(shape)
            return <StaticAnnotation key={shape.id} shape={shape} scale={scale}/>
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

/*
 * Copyright 2020 znai maintainers
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
import EmbeddedAnnotatedImage from './EmbeddedAnnotatedImage'
import {elementsLibrary} from '../DefaultElementsLibrary';
import {ZoomOverlay} from '../zoom/ZoomOverlay';
import { simulateState } from "react-component-viewer";

const [getBorder, setBorder] = simulateState(true);

export function imageDemo(registry) {
    registry.add('standard image', () => (
        <div style={{width: 300, overflow: 'hidden'}}>
            <elementsLibrary.DocElement content={[standardImage()]}
                                        elementsLibrary={elementsLibrary}/>
        </div>
    ))
    registry.add('with border', () => <EmbeddedAnnotatedImage {...standardImage()} border={true}/>)
    registry.add('with border and badge', () => <EmbeddedAnnotatedImage {...standardImage()} {...badgeAlignment(450, 350)} border={true}/>)
    registry.add('without border but with badge', () => <EmbeddedAnnotatedImage {...standardImage()} {...badgeAlignment(450, 350)}/>)
    registry.add("toggle border", () => (
      <div>
          <button onClick={() => setBorder(!getBorder())}>toggle border</button>
        <EmbeddedAnnotatedImage {...standardImage()} {...badgeAlignment(450, 350)} border={getBorder()}/>)
      </div>
    ));

    registry.add('no fit image with annotation', () => (
      <EmbeddedAnnotatedImage {...noFitImage()} {...noFitImageAnnotations()}/>
    ))
    registry.add('fit image with zoom', () =>
        (
            <>
                <ZoomOverlay/>
                <EmbeddedAnnotatedImage {...fitImage()}/>
            </>
        )
    )
    registry.add('badge alignment annotations fit', () => (
      <EmbeddedAnnotatedImage {...badgeAlignment(450, 350)} {...fitImage()} {...noFitImageAnnotations()}/>
    ))

    registry.add('badge alignment manual scale param', () => (
      <EmbeddedAnnotatedImage {...badgeAlignment(450, 350)} {...scaleImage(0.6)} {...noFitImageAnnotations()}/>
    ))

    registry.add('simple annotations', () => <EmbeddedAnnotatedImage {...simpleAnnotations()}/>)
    registry.add('simple annotations with border', () => <EmbeddedAnnotatedImage {...simpleAnnotations()}
                                                                                 border={true}/>)
    registry.add('badge alignment annotations', () => <EmbeddedAnnotatedImage {...badgeAlignment(450, 350)}/>)
    registry.add('badge alignment annotations inverted', () => <EmbeddedAnnotatedImage {...badgeAlignment(50, 350, true)}/>)
    registry.add('left aligned', () => <EmbeddedAnnotatedImage {...leftAlign()}/>)
    registry.add('right aligned', () => <EmbeddedAnnotatedImage {...rightAlign()}/>)
}

function standardImage() {
    return {
        type: 'AnnotatedImage',
        imageSrc: 'ui.jpg',
        width: 800,
        height: 400
    }
}

function fitImage() {
    return {
        type: 'AnnotatedImage',
        imageSrc: 'books.jpg',
        width: 1698,
        height: 1131,
        fit: true
    }
}

function noFitImage() {
    return {
        ...fitImage(),
        fit: false
    }
}

function scaleImage(scale) {
    return {
        ...noFitImage(),
        scale
    }
}

function noFitImageAnnotations() {
    return {
        shapes: [
            {type: 'badge', id: 'c1', x: 640, y: 900, text: '1', invertedColors: true},
            {type: 'circle', id: 'c2', x: 640, y: 800, r: 20, text: '2'},
            {type: 'rectangle', id: 'r1', x: 640, y: 700, width: 80, height: 40, text: 'here', color: 'b'},
            {type: 'highlight', id: 'r2', x: 640, y: 650, width: 80, height: 40, text: 'here', color: 'b'},
            {type: 'arrow', id: 'a1', beginX: 640, beginY: 600, endX: 740, endY: 580, color: 'd', text: 'This here'}]
    }
}

function simpleAnnotations() {
    return {
        imageSrc: 'ui.jpg',
        width: 800,
        height: 500,
        shapes: [
            {type: 'circle', id: 'c1', x: 100, y: 100, r: 20, text: '1'},
            {type: 'circle', id: 'c2', x: 500, y: 100, r: 20, text: '1', color: 'a'},
            {type: 'circle', id: 'c3', x: 180, y: 100, r: 20, text: '2', color: 'b'},
            {type: 'circle', id: 'c4', x: 150, y: 150, r: 30, text: '3', color: 'c'},
            {type: 'highlight', id: 'c5', x: 150, y: 220, width: 80, height: 40, color: 'c'},
            {type: 'rectangle', id: 'c6', x: 270, y: 170, width: 80, height: 40, text: 'here', color: 'b'},
            {type: 'arrow', id: 'c7', beginX: 200, beginY: 200, endX: 300, endY: 300, color: 'd', text: 'This here'}]
    }
}

function badgeAlignment(x, y, inverted) {
    return {
        imageSrc: 'ui.jpg',
        width: 800,
        height: 500,
        shapes: [
            {type: 'badge', id: 'd1', x, y, text: '1', invertedColors: inverted},
            {type: 'badge', id: 'd2', x, y, text: '2', align: 'Above', invertedColors: inverted},
            {type: 'badge', id: 'd3', x, y, text: '3', align: 'Below', invertedColors: inverted},
            {type: 'badge', id: 'd4', x, y, text: '4', align: 'ToTheLeft', invertedColors: inverted},
            {type: 'badge', id: 'd5', x, y, text: '5', align: 'ToTheRight', invertedColors: inverted}
        ]
    }
}

function leftAlign() {
    return {
        imageSrc: 'ui.jpg',
        align: 'left',
        width: 400,
        height: 250,
    }
}

function rightAlign() {
    return {
        imageSrc: 'ui.jpg',
        align: 'right',
        width: 400,
        height: 250,
    }
}

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

import React, {Component} from 'react'

import {imageAdditionalPreviewUrlParam} from './imagePreviewAdditionalUrlParam'

import './AnnotatedImage.css'
import { cssVarPixelValue } from "../../utils/cssVars";

class AnnotatedImage extends Component {
    render() {
        const {
            imageSrc,
            annotations,
            width,
            height,
            isStatic,
            selectedId,
            caption,
            captionBottom,
            fit,
            border,
            timestamp
        } = this.props

        const fullWidth = cssVarPixelValue("znai-single-column-full-width");
        const scale = fit ? fullWidth / width : 1;

        const scaledWidth = width * scale
        const scaledHeight = height * scale

        const imageWidth = scaledWidth + "px"
        const imageHeight = scaledHeight + "px"

        const parentStyle = {position: 'relative', width: imageWidth, height: imageHeight}
        const imageContainerStyle = {position: "absolute", top: 0}
        const annotationsContainerStyle = {position: "absolute", top: 0}

        const captionElement = caption ? (
            <div style={captionContainerStyle(captionBottom)} className="annotated-image-caption">
                {caption}
            </div>
        ) : null

        const className = "annotated-image" + (border ? " border" : "")

        return (
            <div style={parentStyle} className={className}>
                <div style={imageContainerStyle}>
                    <img alt="annotated"
                         src={imageSrc + imageAdditionalPreviewUrlParam(timestamp)}
                         width={imageWidth}
                         height={imageHeight}
                         ref={node => this.imageNode = node}/>
                </div>
                <div style={annotationsContainerStyle}>
                    <svg width={imageWidth} height={imageHeight}>
                        {isStatic ?
                            annotations.staticAnnotationsToRender(scale):
                            annotations.interactiveAnnotationsToRender(selectedId)}

                        <filter id="highlight">
                            <feColorMatrix values="1 0 1 0 0
                                               1 0 1 0 0
                                               1 0 1 0 0
                                               0 0 0 1 0"/>
                        </filter>
                    </svg>
                </div>
                {captionElement}
            </div>
        )
    }
}

function captionContainerStyle(captionBottom) {
    let captionContainerStyle = {position: "absolute"}

    if (captionBottom) {
        captionContainerStyle.bottom = 0
    } else {
        captionContainerStyle.top = 0
    }

    return captionContainerStyle
}

export default AnnotatedImage
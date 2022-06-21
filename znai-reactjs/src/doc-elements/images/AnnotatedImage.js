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
            scale,
            border,
            timestamp
        } = this.props

        const scaleToUse = calcScale()

        const scaledWidth = width * scaleToUse
        const scaledHeight = height * scaleToUse

        const borderSize = border ? 1 : 0;
        const borderSizeAdjustment = borderSize * 2;

        const parentStyle = {
            position: 'relative',
            width: (scaledWidth + borderSizeAdjustment) + "px",
            height: (scaledHeight + borderSizeAdjustment) + "px"}

        const imageWidth = scaledWidth + "px"
        const imageHeight = scaledHeight + "px"

        const childContainerStyle = {
            position: "absolute",
            width: imageWidth,
            height: imageHeight,
            // top: borderSize,
            // left: borderSize
        }

        const captionElement = caption ? (
            <div style={captionContainerStyle(captionBottom)} className="annotated-image-caption">
                {caption}
            </div>
        ) : null

        const className = "annotated-image" + (border ? " border" : "")

        return (
            <div style={parentStyle} className={className}>
                <div style={childContainerStyle}>
                    <img alt="annotated"
                         src={imageSrc + imageAdditionalPreviewUrlParam(timestamp)}
                         width={imageWidth}
                         height={imageHeight}
                         ref={node => this.imageNode = node}/>
                </div>
                <div style={childContainerStyle}>
                    <svg width={imageWidth} height={imageHeight}>
                        {isStatic ?
                            annotations.staticAnnotationsToRender(scaleToUse):
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

        function calcScale() {
            if (fit) {
                const fullWidth = cssVarPixelValue("znai-single-column-full-width");
                return  fullWidth / width;
            }

            return scale || 1.0;
        }
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
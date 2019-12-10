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
import {svgAttributesToProps} from './svgUtils'

import {isAllAtOnce} from '../meta/meta'

import './EmbeddedSvg.css'

class EmbeddedSvg extends Component {
    render() {
        const {svg} = this.props

        // server side rendering
        if (typeof DOMParser === 'undefined') {
            return null
        }

        const parser = new DOMParser()
        const dom = parser.parseFromString(svg, 'application/xml')

        const svgProps = svgAttributesToProps(dom.documentElement.attributes)

        const children = this.childrenReactElementsFromDomNode(dom.documentElement)

        return (
            <div className="embedded-svg content-block">
                <svg {...svgProps} ref={this.saveSvgNode}>
                    {children}
                </svg>
            </div>
        )
    }

    saveSvgNode = (node) => {
        this.svgNode = node
    }

    componentDidMount() {
        this.saveOriginalSize()
        this.changeSizeWhenPropIsChanged()
    }

    componentDidUpdate(prevProps, prevState, snapshot) {
        this.changeSizeWhenPropIsChanged()
    }

    // saving original size to restore on prop change back (to make preview mode correctly reflect changes)
    saveOriginalSize() {
        this.originalViewBox = this.svgNode.getAttribute('viewBox')
        this.originalHeight = this.svgNode.getAttribute('height')
        this.originalWidth = this.svgNode.getAttribute('width')
    }

    changeSizeWhenPropIsChanged() {
        if (this.props.actualSize) {
            this.forceActualSizeSvg()
        } else {
            this.restoreOriginalSize()
        }
    }

    forceActualSizeSvg() {
        const {scale = 1} = this.props

        const bbox = this.svgNode.getBBox();
        this.svgNode.setAttribute("width", (bbox.width * scale) + "px")
        this.svgNode.removeAttribute("height")
        this.svgNode.setAttribute("viewBox", `${bbox.x} ${bbox.y} ${bbox.width} ${bbox.height}`)
    }

    restoreOriginalSize() {
        const restore = (attrKey, value) => {
            if (value !== null) {
                this.svgNode.setAttribute(attrKey, value)
            } else {
                this.svgNode.removeAttribute(attrKey)
            }
        }

        restore("width", this.originalWidth)
        restore("height", this.originalHeight)
        restore("viewBox", this.originalViewBox)
    }

    childrenReactElementsFromDomNode(domNode) {
        let children = []

        const {idsToReveal, isPresentation, slideIdx, meta} = this.props

        const lastPartIdx = isAllAtOnce(meta) ? idsToReveal.length : slideIdx
        const idsForSlide = isPresentation && idsToReveal ? idsToReveal.slice(0, lastPartIdx + 1) : idsToReveal

        if (!domNode) {
            return children
        }

        const childNodes = domNode.childNodes || []
        for (let i = 0, len = childNodes.length; i < len; i++) {
            const child = childNodes[i]

            const childProps = svgAttributesToProps(child.attributes)
            const key = childProps.id ? childProps.id : i

            if (idsToReveal && childProps.id && idsForSlide.indexOf(childProps.id) === -1) {
                continue
            }

            const reactChildElement = child.nodeName === 'g' ?
                <g key={key} {...childProps} dangerouslySetInnerHTML={{__html: child.innerHTML}}/> :
                <g key={key} dangerouslySetInnerHTML={{__html: child.outerHTML}}/>

            children.push(reactChildElement)
        }

        return children
    }
}

export {EmbeddedSvg}

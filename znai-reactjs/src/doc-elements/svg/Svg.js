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

import './Svg.css'

class Svg extends Component {
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
            <div className="svg content-block">
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
        if (this.props.actualSize) {
            this.actualSizeSvg()
        }
    }

    actualSizeSvg() {
        const {scale = 1} = this.props

        const bbox = this.svgNode.getBBox();
        this.svgNode.setAttribute("width", (bbox.width * scale) + "px");
        this.svgNode.setAttribute("height", (bbox.height * scale) + "px");
        this.svgNode.setAttribute("viewBox", `${bbox.x} ${bbox.y} ${bbox.width} ${bbox.height}`);
    }

    childrenReactElementsFromDomNode(domNode) {
        let children = []

        const {idsToReveal, isPresentation, slideIdx} = this.props

        const idsForSlide = isPresentation && idsToReveal ? idsToReveal.slice(0, slideIdx + 1) : idsToReveal

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

const presentationSvgHandler = {
    component: Svg,
    numberOfSlides: ({idsToReveal}) => idsToReveal ? idsToReveal.length : 1
}

export {Svg, presentationSvgHandler}

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

import {buildUniqueId} from './gvUtils'

import GraphVizReactElementsBuilder from './GraphVizReactElementsBuilder'

import './GraphVizSvg.css'


class GraphVizSvg extends Component {
    render() {
        const {diagram, idsToDisplay, idsToHighlight, urls, wide} = this.props

        if (typeof DOMParser === 'undefined') {
            return null
        }

        const className = "graphviz-diagram " + (wide ? "wide" : "content-block")

        const parser = new DOMParser()
        const dom = parser.parseFromString(diagram.svg, 'application/xml')

        const dropShadowFilterId = buildUniqueId(diagram.id, "glow_filter")
        const el = new GraphVizReactElementsBuilder({ diagram, idsToDisplay, idsToHighlight, urls })
            .reactElementFromDomNode(dom.documentElement)

        return (
            <div className={className}>
                <svg viewBox="0 0 0 0" width="0" height="0">
                    <filter id={dropShadowFilterId}>
                        <feMorphology operator="dilate" radius="3" in="SourceAlpha" result="thicken" />
                        <feGaussianBlur in="thicken" stdDeviation="1" result="blurred" />
                        <feFlood floodColor="var(--mdoc-diagram-node-shadow-color)" result="glowColor" />
                        <feComposite in="glowColor" in2="blurred" operator="in" result="softGlow_colored" />
                        <feMerge>
                            <feMergeNode in="softGlow_colored"/>
                            <feMergeNode in="SourceGraphic"/>
                        </feMerge>
                    </filter>
                </svg>
                {el}
            </div>
        )
    }
}

export default GraphVizSvg
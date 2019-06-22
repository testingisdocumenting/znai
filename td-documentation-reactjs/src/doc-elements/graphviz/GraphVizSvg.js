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
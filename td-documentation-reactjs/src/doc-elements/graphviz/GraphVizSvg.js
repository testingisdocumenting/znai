import React, {Component} from 'react'

import GvPolygon from './GvPolygon'
import GvText from './GvText'
import GvGroup from './GvGroup'
import GvPath from './GvPath'

import {buildUniqueId} from './gvUtils'

import './GraphVizSvg.css'

class ReactElementsBuilder {
    constructor({diagram, colors, idsToDisplay, idsToHighlight, urls}) {
        this.diagram = diagram
        this.colors = colors
        this.idsToDisplay = idsToDisplay
        this.idsToHighlight = idsToHighlight
        this.urls = urls
        this.currentCommentAsId = null
        this.currentParentClass = null
    }

    reactElementFromDomNode(domNode, idx) {
        if (domNode.nodeName === '#comment') {
            this.currentCommentAsId = decodeComment(domNode.textContent.trim())
            return null
        }

        if (domNode.nodeName === '#text') {
            return this.freeTextNodeFromDomNode(domNode)
        }

        const attrProps = attributesToProps(domNode.attributes)
        if (! attrProps.key) {
            attrProps.key = idx
        }

        const props = {diagramId: this.diagram.id}

// TODO requires refactoring
        const createElement = () => {
            if (this.currentParentClass) {
                if (this.currentStyles) {
                    const stylesWithColors = this.currentStyles.filter((sn) => this.colors[sn])

                    // TODO merge styles (font, fill)
                    if (this.currentSelected) {
                        props.colors = this.colors.h
                    } else if (stylesWithColors.length > 0) {
                        props.colors = this.colors[stylesWithColors[0]]
                    } else {
                        props.colors = this.colors.b // TODO default logic and handle arrays
                    }
                } else {
                    props.colors = this.currentSelected ? this.colors.h : this.colors.b // TODO default logic and handle arrays
                }
            }

            props.svg = this.customSvgShape()
            props.isInvertedTextColor = this.isInvertedTextColor()
            props.url = this.urls[this.currentCommentAsId]

            const component = this.reactComponentForDom(domNode)
            const passExtraProps = typeof component !== 'string'
            const propsToUse = passExtraProps ? {...attrProps, ...props} : attrProps

            return React.createElement(component, propsToUse, this.childrenReactElementsFromDomNode(domNode))
        }

        if (domNode.nodeName === 'g') {
            if (this.currentCommentAsId) {
                props.nodeId = this.currentCommentAsId
                props.id = buildUniqueId(this.diagram.id, props.nodeId)
                this.currentStyles = this.diagram.stylesByNodeId[props.nodeId]
            }

            if (this.idsToHighlight && this.idsToHighlight.indexOf(props.nodeId) !== -1) {
                props.selected = true
                this.currentSelected = true
            } else {
                this.currentSelected = false
            }

            if (this.idsToDisplay && this.currentCommentAsId && this.idsToDisplay.indexOf(props.nodeId) === -1) {
                return null
            }

            if (attrProps.className) {
                this.currentParentClass = attrProps.className
                const element = createElement()
                this.currentStyles = {}

                return element
            }
        }

        if (this.currentParentClass) {
            props.parentClassName = this.currentParentClass
        }

        return createElement()
    }

    customSvgShape() {
        if (! this.currentStyles) {
            return null
        }

        const shapes = this.currentStyles.filter((s) => this.diagram.shapeSvgByStyleId[s])
        return this.diagram.shapeSvgByStyleId[shapes[0]]
    }

    isInvertedTextColor() {
        if (! this.currentStyles) {
            return null
        }

        return this.currentStyles.filter((s) => this.diagram.isInvertedTextColorByStyleId[s]).length > 0
    }

    reactComponentForDom(domNode) {
        switch (domNode.nodeName) {
            case 'polygon': return GvPolygon
            case 'text': return GvText
            case 'path': return GvPath
            case 'g': return GvGroup
            case 'title': return () => <g/>

            default: return domNode.tagName
        }
    }

    freeTextNodeFromDomNode(domNode) {
        const trimmed = domNode.textContent.trim()
        return trimmed.length ? trimmed : null
    }

    childrenReactElementsFromDomNode(domNode) {
        let children = []

        if (!domNode) {
            return children
        }

        const childNodes = domNode.childNodes || []
        for (let i = 0, len = childNodes.length; i < len; i++) {
            const child = childNodes[i]

            const reactChildElement = this.reactElementFromDomNode(child, i)
            if (reactChildElement !== null) {
                children.push(reactChildElement)
            }
        }

        return children
    }
}

class GraphVizSvg extends Component {
    render() {
        const {diagram, colors, idsToDisplay, idsToHighlight, urls, wide} = this.props

        // TODO do we need to create svg on a server side?
        if (typeof DOMParser === 'undefined') {
            return null
        }

        const className = "graphviz-diagram " + (wide ? "wide" : "content-block")

        const parser = new DOMParser()
        const dom = parser.parseFromString(diagram.svg, 'application/xml')

        const dropShadowFilterId = buildUniqueId(diagram.id, "glow_filter")
        const el = new ReactElementsBuilder({ diagram, colors, idsToDisplay, idsToHighlight, urls }).reactElementFromDomNode(dom.documentElement)
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

function attributesToProps(attributes) {
    attributes = attributes || []
    let result = {}
    for (let i = 0, len = attributes.length; i < len; i++) {
        const item = attributes.item(i)
        if (item.name.indexOf(':') !== -1) {
            continue;
        }

        const name = convertAttrName(item.name)
        result[name] = item.nodeValue
    }

    return result
}

function capitalize(name) {
    return name.charAt(0).toUpperCase() + name.slice(1)
}

function convertAttrName(name) {
    if (name === 'class') {
        return 'className'
    }

    if (name === 'id') {
        return 'key'
    }

    if (name.indexOf('-') === -1) {
        return name
    }

    const parts = name.split("-")
    return parts.slice(1).reduce((p, c) => p + capitalize(c), parts[0])
}

function decodeComment(comment) {
    return comment.replace('&#45;', '-').replace('&lt;', '<').replace('&gt;', '>')
}

export default GraphVizSvg
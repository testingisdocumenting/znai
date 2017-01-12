import React, { Component } from 'react'

import GvPolygon from './GvPolygon'
import GvText from './GvText'
import GvGroup from './GvGroup'
import GvPath from './GvPath'

class ReactElementsBuilder {
    constructor({diagram, colors, idsToDisplay, idsToHighlight}) {
        this.diagram = diagram
        this.colors = colors
        this.idsToDisplay = idsToDisplay
        this.idsToHighlight = idsToHighlight
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

        const props = {}

        const createElement = () => {
            if (this.currentParentClass) {
                if (this.currentStyles) {
                    const stylesWithColors = this.currentStyles.filter((sn) => this.colors[sn])

                    // TODO merge styles (font, fill)
                    if (stylesWithColors.length > 0) {
                        props.colors = this.colors[stylesWithColors[0]]
                    } else {
                        props.colors = this.colors.b // TODO default logic and handle arrays
                    }
                } else {
                    props.colors = this.colors.b // TODO default logic and handle arrays
                }
            }

            let svgShape = this.customSvgShape();
            if (svgShape) {
                props.svg = svgShape
                // return React.createElement(SvgCustomShape, props)
            }

            const component = this.reactComponentForDom(domNode)
            const passExtraProps = typeof component !== 'string'
            const propsToUse = passExtraProps ? {...attrProps, ...props} : attrProps

            return React.createElement(component, propsToUse, this.childrenReactElementsFromDomNode(domNode))
        }

        if (domNode.nodeName === 'g') {
            if (this.currentCommentAsId) {
                props.id = this.currentCommentAsId
                this.currentStyles = this.diagram.stylesByNodeId[props.id]
            }

            if (this.idsToDisplay && this.currentCommentAsId && this.idsToDisplay.indexOf(this.currentCommentAsId) === -1) {
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
        if (this.currentStyles) {
            const shapes = this.currentStyles.filter((s) => this.diagram.shapeSvgByStyleId[s])
            return this.diagram.shapeSvgByStyleId[shapes[0]]
        }

        return null
    }

    reactComponentForDom(domNode) {
        switch (domNode.nodeName) {
            case 'polygon': return GvPolygon
            case 'text': return GvText
            case 'path': return GvPath
            case 'g': return GvGroup

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
            if (reactChildElement != null) {
                children.push(reactChildElement)
            }
        }

        return children
    }
}

class GraphVizSvg extends Component {
    render() {
        const {diagram, colors, idsToDisplay, idsToHighlight} = this.props

        // TODO do we need to create svg on a server side?
        if (typeof DOMParser === 'undefined') {
            return null
        }

        const parser = new DOMParser()
        const dom = parser.parseFromString(diagram.svg, 'application/xml')

        const el = new ReactElementsBuilder({ diagram, colors, idsToDisplay, idsToHighlight }).reactElementFromDomNode(dom.documentElement)
        return <div>
            <svg>
                <filter id="dropShadowGraphviz">
                    <feOffset result="offOut" in="SourceGraphic" dx="5" dy="5" />
                    <feColorMatrix result="matrixOut" in="offOut" type="matrix" values="0.2 0 0 0 0 0 0.2 0 0 0 0 0 0.2 0 0 0 0 0 1 0" />
                    <feGaussianBlur result="blurOut" in="matrixOut" stdDeviation="30" />
                    <feBlend in="SourceGraphic" in2="blurOut" mode="normal" />
                </filter>
            </svg>
            {el}
        </div>
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
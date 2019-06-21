import {buildUniqueId} from "./gvUtils"
import React from "react"
import GvPolygon from "./GvPolygon"
import GvText from "./GvText"
import GvPath from "./GvPath"
import GvGroup from "./GvGroup"

export default class GraphVizReactElementsBuilder {
    constructor({diagram, idsToDisplay, idsToHighlight, urls}) {
        this.diagram = diagram
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
                const element = this.createReactElement(domNode, props, attrProps)
                this.currentStyles = {}

                return element
            }
        }

        if (this.currentParentClass) {
            props.parentClassName = this.currentParentClass
        }

        return this.createReactElement(domNode, props, attrProps)
    }

    createReactElement(domNode, props, attrProps) {
        if (this.currentParentClass) {
            // styles is a mix list of one-char color groups and/or shape-names
            if (this.currentStyles) {
                const colorGroup = this.currentStyles.find(style => style.length === 1)

                if (this.currentSelected) {
                    props.colors = this.highlightColors()
                } else if (colorGroup) {
                    props.colors = this.colorsByColorGroup(colorGroup)
                } else {
                    props.colors = this.defaultColors()
                }
            } else {
                props.colors = this.currentSelected ? this.highlightColors() : this.defaultColors()
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

    customSvgShape() {
        if (! this.currentStyles) {
            return null
        }

        const shapes = this.currentStyles.filter((s) => this.diagram.shapeSvgByStyleId[s])
        return this.diagram.shapeSvgByStyleId[shapes[0]]
    }

    isInvertedTextColor() {
        if (! this.currentStyles) {
            return false
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

    colorsByColorGroup(colorGroup) {
        colorGroup = colorGroup.toLowerCase()

        return {
            line: `var(--mdoc-diagram-${colorGroup}-line)`,
            fill: `var(--mdoc-diagram-${colorGroup}-fill)`,
            text: `var(--mdoc-diagram-${colorGroup}-text)`,
            textInverse: `var(--mdoc-diagram-${colorGroup}-text-inverse)`,
        }
    }

    defaultColors() {
        return this.colorsByColorGroup('a')
    }

    highlightColors() {
        return this.colorsByColorGroup('h')
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
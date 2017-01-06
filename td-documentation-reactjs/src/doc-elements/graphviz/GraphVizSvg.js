import React, { Component } from 'react'

import GvPolygon from './GvPolygon'
import GvText from './GvText'
import GvGroup from './GvGroup'
import GvPath from './GvPath'

import styleMeta from './gvStyleMeta'

class ReactElementsBuilder {
    constructor({colors, idsToDisplay, idsToHighlight}) {
        this.colors = colors
        this.idsToDisplay = idsToDisplay
        this.idsToHighlight = idsToHighlight
        this.currentCommentAsId = null
        this.currentParentClass = null
    }

    reactElementFromDomNode(domNode) {
        if (domNode.nodeName === '#comment') {
            this.currentCommentAsId = decodeComment(domNode.textContent.trim())
            return null
        }

        if (domNode.nodeName === '#text') {
            return this.freeTextNodeFromDomNode(domNode)
        }

        const props = attributesToProps(domNode.attributes)

        const createElement = () => {    
            if (this.currentParentClass) {
                props.colors = this.currentStyle
            }

            return React.createElement(this.reactCompnentForDom(domNode), props,
                this.childrenReactElementsFromDomNode(domNode))
        }

        if (domNode.nodeName === 'g') {
            if (this.currentCommentAsId) {
                props.id = this.currentCommentAsId
            }

            if (this.currentCommentAsId && this.idsToDisplay.indexOf(this.currentCommentAsId) === -1) {
                return null
            }

            if (props.className) {
                this.currentParentClass = props.className
                this.currentStyle = this.extractCurrentStyleFromTextLabel(domNode)
                const element = createElement()
                this.currentStyle = {}  

                return element
            }
        }

        if (this.currentParentClass) {
            props.parentClassName = this.currentParentClass
        }

        return createElement()
    }

    reactCompnentForDom(domNode) {
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

            const reactChildElement = this.reactElementFromDomNode(child)
            if (reactChildElement != null) {
                children.push(reactChildElement)
            }
        }

        return children
    }


    extractCurrentStyleNamesFromTextLabel(domNode) {
        const childNodes = domNode.childNodes || []
        for (let i = 0, len = childNodes.length; i < len; i++) {
            const childNode = childNodes[i]
            if (childNode.nodeName !== 'text') {
                continue
            }

            if (!childNode.childNodes.length) {
                continue
            }

            const text = childNode.childNodes[0].textContent
            if (!text) {
                continue
            }

            return styleMeta.extractStyleNames(text)
        }

        return []
    }

    extractCurrentStyleFromTextLabel(domNode) {
        const styleNames = this.extractCurrentStyleNamesFromTextLabel(domNode)
        if (!styleNames.length) {
            return this.colors.b
        }

        console.log("Style names", styleNames)
        return this.colors[styleNames[0]] // TODO support multiple styles and convert to styles from colors
    }
}

class GraphVizSvg extends Component {
    render() {
        const {svg, colors, idsToDisplay, idsToHighlight} = this.props
        const parser = new DOMParser()
        const dom = parser.parseFromString(svg, 'application/xml')

        const el = new ReactElementsBuilder({ colors, idsToDisplay, idsToHighlight }).reactElementFromDomNode(dom.documentElement)
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
    let props = {}
    for (let i = 0, len = attributes.length; i < len; i++) {
        const item = attributes.item(i)
        if (item.name.indexOf(':') !== -1) {
            continue;
        }

        const name = convertAttrName(item.name)
        props[name] = item.nodeValue
    }

    return props
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
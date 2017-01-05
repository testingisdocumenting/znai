import React, { Component } from 'react'

import GvPolygon from './GvPolygon'

class GraphVizSvg extends Component {
    render() {
        const {svg} = this.props
        const parser = new DOMParser()
        const dom = parser.parseFromString(svg, 'application/xml')

        const el = reactElementFromDomNode(dom.documentElement)
        return <div>{el}</div>
    }
}

function reactElementFromDomNode(domNode) {
    if (domNode.nodeName === '#comment') {
        return null
    }

    if (domNode.nodeName === '#text') {
        return textNodeFromDomNode(domNode)
    }

    const props = attributesToProps(domNode.attributes)

    if (domNode.nodeName === 'polygon') {
        return <GvPolygon {...props} />
    }

    return React.createElement(tagNameForDom(domNode), props,
        childrenReactElementsFromDomNode(domNode))
}

function textNodeFromDomNode(domNode) {
    const trimmed = domNode.textContent.trim()
    return trimmed.length ? trimmed : null
}

function childrenReactElementsFromDomNode(domNode) {
    let children = []

    if (!domNode) {
        return children
    }

    const childNodes = domNode.childNodes || []
    for (let i = 0, len = childNodes.length; i < len; i++) {
        const child = childNodes[i]

        const reactChildElement = reactElementFromDomNode(child)
        if (reactChildElement != null) {
            children.push(reactElementFromDomNode(child))
        }
    }

    return children
}

function tagNameForDom(dom) {
    return dom.tagName
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

export default GraphVizSvg
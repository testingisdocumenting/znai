import React, {Component} from 'react'
import {svgAttributesToProps} from './svgUtils'

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

        return <div className="content-block">
            <svg {...svgProps}>
                {children}
            </svg>
        </div>
    }

    childrenReactElementsFromDomNode(domNode) {
        let children = []

        const {idsToReveal, isPresentation, slideIdx} = this.props

        const idsForSlide = isPresentation && idsToReveal ? idsToReveal.slice(0, slideIdx + 1): idsToReveal

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

const presentationSvgHandler = {component: Svg,
    numberOfSlides: ({idsToReveal}) => idsToReveal ? idsToReveal.length : 1}

export {Svg, presentationSvgHandler}

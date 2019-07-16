import React from 'react'

import {elementsLibrary, presentationElementHandlers} from '../DefaultElementsLibrary'

import Presentation from './Presentation'
import PresentationRegistry from './PresentationRegistry'

import '../DocumentationLayout.css'

import columnsContent from './demo/columns'
import quoteContent from './demo/quote'
import svgContent from './demo/svg'
import bulletContent from './demo/bullet'
import codeWithInlinedComments from './demo/codeWithInlinedComment'
import codeWithHighlights from './demo/codeWithHighlights'
import barChart from './demo/barChart'
import lineChart from './demo/lineChart'

const section1 = {
    title: "Section One",
    type: "Section",
    content: svgContent
}

const section2 = {
    title: "Section Two",
    type: "Section",
    content: codeWithInlinedComments
}

const page = {
    tocItem: {pageTitle: "Page Title"},
    type: "Page",
    content: columnsContent
}

const docMeta = {id: "mdoc", title: "MDoc", type: "User Guide"}

const registry = new PresentationRegistry(elementsLibrary, presentationElementHandlers, codeWithHighlights)
const PresentationDemo = (props) => <Presentation docMeta={docMeta} presentationRegistry={registry}/>

export default PresentationDemo

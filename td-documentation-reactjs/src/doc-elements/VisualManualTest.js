import React from 'react'

import diagramSlide from './DiagramSlidesTestData'
import testData from './TestData'

import GraphVizFlow from './graphviz/GraphVizFlow'

const VisualManualTest = () => (
    <GraphVizFlow diagram={diagramSlide.diagram} colors={testData.graphvizColors}
        slides={diagramSlide.slides} currentSlide={1}/>
)

export default VisualManualTest

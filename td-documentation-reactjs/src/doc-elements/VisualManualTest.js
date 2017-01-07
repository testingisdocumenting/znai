import React from 'react'

import diagramSlide from './DiagramSlidesTestData'
import testData from './TestData'

import GraphVizFlow from './graphviz/GraphVizFlow'

const VisualManualTest = () => (
    <GraphVizFlow svg={diagramSlide.svg} colors={testData.graphvizColors}
        slides={diagramSlide.slides} currentSlide={1}/>
)

export default VisualManualTest

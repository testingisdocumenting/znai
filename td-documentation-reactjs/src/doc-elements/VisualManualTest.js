import React from 'react'

import diagramSlide from './DiagramSlidesTestData'
import diagram from './DiagramTestData'
import testData from './TestData'

import GraphVizSvg from './graphviz/GraphVizSvg'
// import GraphVizFlow from './graphviz/GraphVizFlow'

const VisualManualTest = () => (
    <GraphVizSvg diagram={diagram} colors={testData.graphvizColors}/>
)

export default VisualManualTest

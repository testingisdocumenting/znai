import React from 'react'

import TestData from './TestData'
import GraphVizFlow from './graphviz/GraphVizFlow'

const VisualManualTest = () => (
    <GraphVizFlow svg={TestData.svg} colors={TestData.graphvizColors}
        slides={TestData.graphPresentation.slides} currentSlide="1"/>
)

export default VisualManualTest

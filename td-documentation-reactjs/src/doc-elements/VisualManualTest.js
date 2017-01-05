import React from 'react'

import TestData from './TestData'
import GraphVizSvg from './graphviz/GraphVizSvg'

const VisualManualTest = () => (
    <GraphVizSvg svg={TestData.svg} />
)

export default VisualManualTest

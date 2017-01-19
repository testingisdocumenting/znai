import React, {Component} from "react"
import GraphVizFlow from './graphviz/GraphVizFlow'
import diagram from './DiagramSlidesTestData'
import testData from './TestData'

class DiagramSlidesDemo extends Component {
    render() {
        return <GraphVizFlow {...diagram} colors={testData.graphvizColors} currentSlideIdx={0}/>
    }
}

export default DiagramSlidesDemo
import React, {Component} from "react"
import GraphVizFlow from './graphviz/GraphVizFlow'
import diagram from './DiagramSlidesTestData'
import testData from './TestData'

class DiagramSlidesDemo extends Component {
    render() {
        return <div>
            <GraphVizFlow {...diagram} colors={testData.graphvizColors} currentSlideIdx={0}/></div>
    }
}

export default DiagramSlidesDemo
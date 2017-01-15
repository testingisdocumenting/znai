import React, {Component} from "react"
import Documentation from './Documentation'
import testData from './TestData'

class DocumentationDemo extends Component {
    render() {
        return <Documentation {...testData.documentation}/>
    }
}

export default DocumentationDemo
import React, {Component} from 'react'
import Steps from './Steps'

import './TestDetails.css'

const AdditionalResourcesSelection = ({tabs, selectedTabName, onTabSelection}) => {
    return (
        <div className="additional-resources-selection">
            <div className="tab-names">
                {tabs.map(t => {
                    const className = "tab-name" + (selectedTabName === t ? " selected" : "")
                    return <div key={t} className={className} onClick={() => onTabSelection(t)}>{t}</div>
                })}
            </div>
        </div>
    )
}

const Screenshot = ({test}) => {
    return (
        <div className="image">
            <img src={"data:image/png;base64," + test.screenshot} width="100%"/>
        </div>
    )
}

const TestSteps = ({test}) => <Steps steps={test.steps}/>

const NoResource = ({test, selectedResourceTabName}) => <div>No resource: {selectedResourceTabName}</div>

class TestDetails extends Component {
    constructor(props) {
        super(props)

        const tabNames = additionalResourcesTabNames(props.test)
        this.state = {selectedResourceTabName: tabNames[0]}
    }

    componentWillReceiveProps(props) {
        const tabNames = additionalResourcesTabNames(props.test)
        this.setState({selectedResourceTabName: tabNames[0]})
    }

    render() {
        const {test} = this.props
        const {selectedResourceTabName} = this.state

        const tabNames = additionalResourcesTabNames(test)

        const Resource = resourceComponentByTab()

        return (
            <div className="test-details">
                <div className="file-name">
                    {test.fileName}
                </div>

                <div className="scenario">
                    {test.scenario}
                </div>

                {test.contextDescription ?
                    <div className="context-description">
                        <pre>
                            {test.contextDescription}
                        </pre>
                    </div> : null}

                {test.assertion ?
                    <div className="assertion">
                        <pre>
                            {test.assertion}
                        </pre>
                    </div> : null}

                <AdditionalResourcesSelection tabs={tabNames}
                                              selectedTabName={selectedResourceTabName}
                                              onTabSelection={this.onResourceTabSelection}/>

                <div className="resource">
                    <Resource test={test} selectedResourceTabName={selectedResourceTabName}/>
                </div>
            </div>
        )

        function resourceComponentByTab() {
            switch (selectedResourceTabName) {
                case "Screenshot":
                    return Screenshot
                case "Steps":
                    return TestSteps
                default:
                    return NoResource
            }
        }
    }

    onResourceTabSelection = (tabName) => this.setState({selectedResourceTabName: tabName})
}

function additionalResourcesTabNames(test) {
    const tabs = []
    if (test.hasOwnProperty("screenshot")) {
        tabs.push("Screenshot")
    }

    if (test.hasOwnProperty("steps")) {
        tabs.push("Steps")
    }

    return tabs
}

export default TestDetails
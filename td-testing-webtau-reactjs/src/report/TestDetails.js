import React, {Component} from 'react'
import Steps from './details/Steps'

import TestSteps from './details/TestSteps'
import AdditionalResourcesSelection from './details/AdditionalResourcesSelection'
import Screenshot from './details/Screenshot'
import FullStackTrace from './details/FullStackTrace'
import ShortStackTrace from './details/ShortStackTrace'
import HttpCalls from './details/http/HttpCalls'
import NoDetailsDefined from './details/NoDetailsDefined'

import './TestDetails.css'

const OptionalPreBlock = ({className, message}) => {
    if (! message) {
        return null
    }

    return (
        <div className={className}>
            <pre>
                {message}
            </pre>
        </div>
    )
}

class TestDetails extends Component {
    constructor(props) {
        super(props)

        const resources = additionalResources(props.test)
        this.state = {selectedResourceTabName: resources[0].tabName}
    }

    componentWillReceiveProps(props) {
        const resources = additionalResources(props.test)
        this.setState({selectedResourceTabName: resources[0].tabName})
    }

    render() {
        const {test} = this.props
        const {selectedResourceTabName} = this.state

        const resources = additionalResources(test)
        const Resource = resourceComponentByTab()

        const tabNames = resources.map(r => r.tabName)

        return (
            <div className="test-details">
                <div className="file-name">
                    {test.fileName}
                </div>

                <div className="scenario">
                    {test.scenario}
                </div>

                <OptionalPreBlock className="context-description" message={test.contextDescription}/>
                <OptionalPreBlock className="assertion" message={test.assertion}/>
                {
                    ! test.assertion ? <OptionalPreBlock className="exception-message" message={test.exceptionMessage}/> :
                        null
                }

                <AdditionalResourcesSelection tabs={tabNames}
                                              selectedTabName={selectedResourceTabName}
                                              onTabSelection={this.onResourceTabSelection}/>

                <div className="resource">
                    <Resource test={test} selectedResourceTabName={selectedResourceTabName}/>
                </div>
            </div>
        )

        function resourceComponentByTab() {
            const entry = resources.filter(r => r.tabName === selectedResourceTabName)
            return entry ? entry[0].component : NoDetailsDefined
        }
    }

    onResourceTabSelection = (tabName) => this.setState({selectedResourceTabName: tabName})
}

function additionalResources(test) {
    const resources = []
    if (test.hasOwnProperty('screenshot')) {
        resources.push({tabName: 'Screenshot', component: Screenshot})
    }

    if (test.hasOwnProperty('steps')) {
        resources.push({tabName: 'Steps', component: TestSteps})
    }

    if (test.hasOwnProperty('shortStackTrace')) {
        resources.push({tabName: 'StackTrace', component: ShortStackTrace})
    }

    if (test.hasOwnProperty('fullStackTrace')) {
        resources.push({tabName: 'Full StackTrace', component: FullStackTrace})
    }

    if (test.hasOwnProperty('httpCalls')) {
        resources.push({tabName: 'HTTP calls', component: HttpCalls})
    }

    return resources
}

export default TestDetails
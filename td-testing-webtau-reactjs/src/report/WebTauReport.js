import React, {Component} from 'react'
import ListOfTests from './ListOfTests'
import Header from './Header'
import TestDetails from './TestDetails'

import './WebTauReport.css'

class WebTauReport extends Component {
    constructor(props) {
        super(props)

        this.state = {selectedId: null, selectedStatusFilter: "Total", filteredTests: props.report.tests}
    }

    render() {
        const {report} = this.props
        const {selectedId, selectedStatusFilter, filteredTests} = this.state

        const selectedTest = selectedId ? report.tests.filter(t => t.id === selectedId)[0] : null

        return (
            <div className="report">
                <Header summary={report.summary}
                        onTitleClick={this.onHeaderTitleClick}
                        selectedStatusFilter={selectedStatusFilter}
                        onTestStatusSelect={this.onTestStatusSelect}/>
                <div className="body">
                    <ListOfTests tests={filteredTests}
                                 selectedId={selectedId}
                                 selectedStatusFilter={selectedStatusFilter}
                                 onSelect={this.onTestSelect}/>
                    {selectedTest ? <TestDetails test={selectedTest}/> : null}
                </div>
            </div>
        )
    }

    onHeaderTitleClick = () => this.setState({selectedId: null})

    onTestSelect = (id) => this.setState({selectedId: id})

    onTestStatusSelect = (status) => {
        const {report} = this.props

        const filteredTests = status === 'Total' ? report.tests :
            report.tests.filter(t => t.status === status)

        const selectedId = filteredTests.length ? filteredTests[0].id : null

        this.setState({selectedStatusFilter: status, selectedId: selectedId, filteredTests: filteredTests})
    }
}

export default WebTauReport

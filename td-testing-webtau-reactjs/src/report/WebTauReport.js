import React, {Component} from 'react'
import ListOfTests from './ListOfTests'
import Header from './Header'
import TestDetails from './TestDetails'

import './WebTauReport.css'
import WebTauReportStateCreator from './WebTauReportStateCreator'

class WebTauReport extends Component {
    constructor(props) {
        super(props)

        this._stateCreator = new WebTauReportStateCreator(props.report)

        this.state = this.stateFromUrl()
    }

    render() {
        const {report} = this.props
        const {testId, detailTabName, statusFilter} = this.state

        const selectedTest = testId ? report.findTestById(testId) : null

        return (
            <div className="report">
                <Header summary={report.summary}
                        onTitleClick={this.onHeaderTitleClick}
                        selectedStatusFilter={statusFilter}
                        onTestStatusSelect={this.onTestStatusSelect}/>
                <div className="body">
                    <ListOfTests tests={this.filteredTests}
                                 selectedId={testId}
                                 onSelect={this.onTestSelect}/>
                    {selectedTest ? <TestDetails test={selectedTest}
                                                 selectedDetailTabName={detailTabName}
                                                 onDetailsTabSelection={this.onDetailsTabSelection}
                                                 detailTabs={selectedTest.details}/> : null}
                </div>
            </div>
        )
    }

    get filteredTests() {
        const {report} = this.props
        const {statusFilter} = this.state

        return report.withStatus(statusFilter)
    }

    onHeaderTitleClick = () => this.pushUrl({selectedId: null})

    onTestSelect = (id) => this.pushUrl({testId: id})

    onTestStatusSelect = (status) => {
        const {report} = this.props

        const filtered = report.withStatus(status)
        const firstTestId = filtered.length > 0 ? filtered[0].id : null

        this.pushUrl({statusFilter: status, testId: firstTestId})
    }

    onDetailsTabSelection = (tabName) => this.pushUrl({detailTabName: tabName})

    componentDidMount() {
        this.subscribeToUrlChanges()
        this.updateStateFromUrl()
    }

    subscribeToUrlChanges() {
        window.addEventListener('popstate', (e) => {
            this.updateStateFromUrl();
        });
    }

    stateFromUrl() {
        return this._stateCreator.stateFromUrl(document.location.search)
    }

    updateStateFromUrl() {
        const newState = this.stateFromUrl()
        this.setState(newState)
    }

    pushUrl(partialNewState) {
        const searchParams = this._stateCreator.buildUrlSearchParams({...this.state, ...partialNewState})
        window.history.pushState({}, '', '?' + searchParams)

        this.updateStateFromUrl()
    }
}

export default WebTauReport

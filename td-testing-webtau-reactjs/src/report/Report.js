import TestSteps from './details/TestSteps'
import HttpCalls from './details/http/HttpCalls'
import ShortStackTrace from './details/ShortStackTrace'
import Screenshot from './details/Screenshot'
import FullStackTrace from './details/FullStackTrace'
import Summary from './details/Summary'

class Report {
    constructor(report) {
        this.report = report
        this.summary = report.summary
        this.tests = enrichWithAdditionalDetails(report.tests)
    }

    findTestById(id) {
        const found = this.tests.filter(t => t.id === id)
        return found.length ? found[0] : null
    }

    withStatus(status) {
        if (!status || status === 'Total') {
            return this.tests
        }

        return this.tests.filter(t => t.status === status)
    }

    hasTestWithId(id) {
        return this.findTestById(id) !== null
    }

    hasDetailWithTabName(testId, tabName) {
        const test = this.findTestById(testId)
        if (!test) {
            return false
        }

        return test.details.filter(d => d.tabName === tabName).length !== 0
    }

    firstDetailTabName(testId) {
        const test = this.findTestById(testId)
        if (!test) {
            return ''
        }

        return test.details[0].tabName
    }
}

function enrichWithAdditionalDetails(tests) {
    return tests.map(test => ({
            ...test,
            details: additionalDetails(test)
        }))
}


function additionalDetails(test) {
    const details = []
    details.push({tabName: 'Summary', component: Summary})

    if (test.hasOwnProperty('screenshot')) {
        details.push({tabName: 'Screenshot', component: Screenshot})
    }

    if (test.hasOwnProperty('steps')) {
        details.push({tabName: 'Steps', component: TestSteps})
    }

    if (test.hasOwnProperty('httpCalls')) {
        details.push({tabName: 'HTTP calls', component: HttpCalls})
    }

    if (test.hasOwnProperty('shortStackTrace')) {
        details.push({tabName: 'StackTrace', component: ShortStackTrace})
    }

    if (test.hasOwnProperty('fullStackTrace')) {
        details.push({tabName: 'Full StackTrace', component: FullStackTrace})
    }

    return details
}

export default Report
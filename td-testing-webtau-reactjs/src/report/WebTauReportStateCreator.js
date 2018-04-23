const queryParamNames = {
    testId: 'testId',
    statusFilter: 'status',
    detailTabName: 'detail'
};

class WebTauReportStateCreator {
    constructor(report) {
        this.report = report
    }

    stateFromUrl(url) {
        const searchParams = new URLSearchParams(url)

        const testIdFromParam = searchParams.get(queryParamNames.testId)
        const testId = this.report.hasTestWithId(testIdFromParam) ? testIdFromParam : null

        const detailTabFromParam = searchParams.get(queryParamNames.detailTabName)
        const detailTabName = this.report.hasDetailWithTabName(testId, detailTabFromParam) ?
            detailTabFromParam:
            this.report.firstDetailTabName(testId)

        const statusFilter = searchParams.get(queryParamNames.statusFilter)

        return {testId, detailTabName, statusFilter}
    }

    buildUrlSearchParams(state) {
        const searchParams = new URLSearchParams();

        Object.keys(state).forEach(k => {
            const v = state[k];

            if (v) {
                searchParams.set(queryParamNames[k], v.toString());
            }
        });

        return searchParams.toString();
    }
}

export default WebTauReportStateCreator
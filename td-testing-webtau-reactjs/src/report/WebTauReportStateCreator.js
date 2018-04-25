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
        const searchParams = WebTauReportStateCreator._searchParamsFromUrl(url)

        const testIdFromParam = searchParams[queryParamNames.testId]
        const testId = this.report.hasTestWithId(testIdFromParam) ? testIdFromParam : null

        const detailTabFromParam = searchParams[queryParamNames.detailTabName]
        const detailTabName = this.report.hasDetailWithTabName(testId, detailTabFromParam) ?
            detailTabFromParam:
            this.report.firstDetailTabName(testId)

        const statusFilter = searchParams[queryParamNames.statusFilter]

        return {...searchParams, testId, detailTabName, statusFilter}
    }

    buildUrlSearchParams(state) {
        const searchParams = new URLSearchParams();

        Object.keys(state).forEach(k => {
            const v = state[k];

            if (v) {
                const key = queryParamNames[k] || k
                searchParams.set(key, v.toString());
            }
        });

        return searchParams.toString();
    }

    static _searchParamsFromUrl(url) {
        const result = {}
        const searchParams = new URLSearchParams(url)
        for (let p of searchParams) {
            result[p[0]] = p[1]
        }

        return result
    }
}

export default WebTauReportStateCreator
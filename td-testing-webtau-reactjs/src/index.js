import React from 'react'
import ReactDOM from 'react-dom'

import WebTauReport from './report/WebTauReport'
import './index.css'

global.WebTauReport = WebTauReport

global.renderReport = () => {
    ReactDOM.render(<WebTauReport report={global.testReport} />, document.getElementById('root'));
}

global.renderReport()

import React from 'react'
import { storiesOf } from '@kadira/storybook'

import StepsDemo from './StepsDemo'
import WebTauReportDemo from './WebTauReportDemo'

import './WebTauReport.css'

storiesOf('Report', module)
    .add('default view', () => <WebTauReportDemo/>)

storiesOf('Steps', module)
    .add('with errors', () => <StepsDemo/>)

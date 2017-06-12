import React from 'react'
import { storiesOf } from '@kadira/storybook'

import StepsDemo from './StepsDemo'
import WebTauReportDemo from './WebTauReportDemo'
import TestDetailsDemo from "./TestDetailsDemo";

import './WebTauReport.css'

storiesOf('Report', module)
    .add('default view', () => <WebTauReportDemo/>)

storiesOf('TestDetails', module)
    .add('with screenshot', () => <TestDetailsDemo/>)

storiesOf('Steps', module)
    .add('with errors', () => <StepsDemo/>)

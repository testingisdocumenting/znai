import React from 'react'
import { storiesOf } from '@storybook/react'

import StepsDemo from './StepsDemo'
import WebTauReportDemo from './WebTauReportDemo'
import {TestDetailsWithScreenshot, TestDetailsWithStackTrace} from "./TestDetailsDemo";

import './WebTauReport.css'

storiesOf('Report', module)
    .add('default view', () => <WebTauReportDemo/>)

storiesOf('TestDetails', module)
    .add('with screenshot', () => <TestDetailsWithScreenshot/>)
    .add('with stack trace', () => <TestDetailsWithStackTrace/>)

storiesOf('Steps', module)
    .add('with errors', () => <StepsDemo/>)

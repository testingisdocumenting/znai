import React from 'react'
import TestDetails from "./TestDetails";

import steps from './testSteps'

import screenshot from './demoScreenshot'

const test = {fileName: "testFile.groovy",
    id: "testFile.groovy-1",
    status: "Failed",
    steps: steps,
    scenario: "User opens an empty order",
    screenshot: screenshot}

const TestDetailsWithScreenshot = () => <TestDetails test={{...test,
    assertion: "expected 4\nactual 3\n",
    contextDescription: "by css #id"}}/>

const TestDetailsWithStackTrace = () => <TestDetails test={{...test,
    exceptionMessage: "division by zero",
    shortStackTrace: "at class1.groovy\nat class2.groovy",
    fullStackTrace: "at class1.groovy\nat class2.groovy\nat class3.groovy\nat class4.groovy" }}/>

export {
    TestDetailsWithScreenshot,
    TestDetailsWithStackTrace
}

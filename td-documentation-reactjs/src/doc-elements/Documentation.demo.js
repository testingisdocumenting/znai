import React from "react"
import Documentation from './Documentation'
import testData from './TestData'

export function documentationDemo(registry) {
    registry.add('default', <Documentation {...testData.documentation}/>)
}
/*
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import React from 'react'

import {simulateState} from 'react-component-viewer'
import {
    DiffTracking,
    disableDiffTracking,
    enableDiffTracking,
    enableDiffTrackingForOneDomChangeTransaction
} from './DiffTracking'

const [getTextValue, setTextValue] = simulateState('hello')
const [getBeforeItems, setBeforeItems] = simulateState([])
const [getAfterItems, setAfterItems] = simulateState([])
const [getScrollCaseBeforeItems, setScrollCaseBeforeItems] = simulateState(['line1', 'line2', 'line3', 'line4', 'line5'])
const [getScrollCaseAfterItems, setScrollCaseAfterItems] = simulateState(['line1', 'line2', 'line3', 'line4', 'line5'])

export function diffTrackingDemo(registry) {
    registry.add('tracking control', () => (
        <div>
            <button onClick={enableDiffTracking}>enable</button>
            <button onClick={disableDiffTracking}>disable</button>
            <button onClick={enableDiffTrackingForOneDomChangeTransaction}>enable for one change transaction</button>
        </div>
    ))

    registry.add('without scroll', () => (
        <DiffTracking>
            <div className="main-panel">
                <div className="page-content">
                    <div className="content-block" style={{padding: 20}}>
                        <div className="simple-text">{getTextValue()}</div>
                    </div>

                    <div className="simple-text">{getTextValue()}</div>

                    {getBeforeItems().map((item, idx) => <div key={idx} className="simple-text">{item}</div>)}
                </div>

                <button onClick={changeText}>change text</button>
                <button onClick={addBeforeItem}>add before item</button>
                <button onClick={addAfterItem}>add after item</button>

                {getAfterItems().map((item, idx) => <div key={idx} className="simple-text">{item}</div>)}
            </div>
        </DiffTracking>
    ))

    registry.add('with scroll', () => (
        <DiffTracking>
            <div className="main-panel" style={{maxHeight: 100, scroll: 'auto'}}>
                <div className="page-content">
                    {getScrollCaseBeforeItems().map((item, idx) => <div key={idx} className="simple-text">{item}</div>)}
                    <div className="simple-text">{getTextValue()}</div>
                    {getScrollCaseAfterItems().map((item, idx) => <div key={idx} className="simple-text">{item}</div>)}
                </div>
            </div>

            <button onClick={changeText}>change text</button>
            <button onClick={addScrollCaseBeforeItem}>add before item</button>
            <button onClick={addScrollCaseAfterItem}>add after item</button>
        </DiffTracking>
    ))
}

function changeText() {
    setTextValue(getTextValue() + '@')
}

let itemIdx = 0

function addBeforeItem() {
    itemIdx++
    setBeforeItems([...getBeforeItems(), 'another item ' + (itemIdx)])
}

function addAfterItem() {
    itemIdx++
    setAfterItems([...getAfterItems(), 'another item ' + (itemIdx)])
}

function addScrollCaseBeforeItem() {
    itemIdx++
    setScrollCaseBeforeItems([...getScrollCaseBeforeItems(), 'another item ' + (itemIdx)])
}

function addScrollCaseAfterItem() {
    itemIdx++
    setScrollCaseAfterItems([...getScrollCaseAfterItems(), 'another item ' + (itemIdx)])
}

/*
 * Copyright 2021 znai maintainers
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

import React from "react";

import { Registry} from "react-component-viewer";
import { simulateState } from "react-component-viewer";
import {
  DiffTracking,
  disableDiffTracking,
  enableDiffTracking,
  enableDiffTrackingForOneDomChangeTransaction,
} from "./DiffTracking";
import { mainPanelClassName } from "../layout/classNamesAndIds";

const [getTextValue, setTextValue] = simulateState("hello");
const [getSvgTextValue, setSvgTextValue] = simulateState("svg hello");
const [getBeforeItems, setBeforeItems] = simulateState<string[]>([]);
const [getAfterItems, setAfterItems] = simulateState<string[]>([]);
const [getScrollCaseBeforeItems, setScrollCaseBeforeItems] = simulateState([
  "line1",
  "line2",
  "line3",
  "line4",
  "line5",
]);
const [getScrollCaseAfterItems, setScrollCaseAfterItems] = simulateState(["line1", "line2", "line3", "line4", "line5"]);

export function diffTrackingDemo(registry: Registry) {
  registry.add("tracking control", () => (
    <div>
      <button onClick={enableDiffTracking}>enable</button>
      <button onClick={disableDiffTracking}>disable</button>
      <button onClick={enableDiffTrackingForOneDomChangeTransaction}>enable for one change transaction</button>
    </div>
  ));

  registry.add("without scroll", () => (
    <DiffTracking>
      <div className={mainPanelClassName}>
        <div className="page-content">
          <div className="content-block" style={{ padding: 20 }}>
            <div className="znai-simple-text">{getTextValue()}</div>
          </div>

          <svg width={200} height={100}>
            <text x={50} y={50}>
              {getSvgTextValue()}
            </text>
          </svg>

          <div className="znai-simple-text">{getTextValue()}</div>

          {getBeforeItems().map((item, idx) => (
            <div key={idx} className="znai-simple-text">
              {item}
            </div>
          ))}
        </div>

        <button onClick={changeText}>change text</button>
        <button onClick={changeSvgText}>change svg text</button>
        <button onClick={addBeforeItem}>add before item</button>
        <button onClick={addAfterItem}>add after item</button>

        {getAfterItems().map((item, idx) => (
          <div key={idx} className="znai-simple-text">
            {item}
          </div>
        ))}
      </div>
    </DiffTracking>
  ));

  registry.add("with scroll", () => (
    <DiffTracking>
      <div className={mainPanelClassName} style={{ maxHeight: 100, overflow: "auto" }}>
        <div className="page-content">
          {getScrollCaseBeforeItems().map((item, idx) => (
            <div key={idx} className="znai-simple-text">
              {item}
            </div>
          ))}
          <div className="znai-simple-text">{getTextValue()}</div>
          {getScrollCaseAfterItems().map((item, idx) => (
            <div key={idx} className="znai-simple-text">
              {item}
            </div>
          ))}
        </div>
      </div>

      <button onClick={changeText}>change text</button>
      <button onClick={addScrollCaseBeforeItem}>add before item</button>
      <button onClick={addScrollCaseAfterItem}>add after item</button>
    </DiffTracking>
  ));
}

function changeText() {
  setTextValue(getTextValue() + "@");
}

function changeSvgText() {
  setSvgTextValue(getSvgTextValue() + "@");
}

let itemIdx = 0;

function addBeforeItem() {
  itemIdx++;
  setBeforeItems([...getBeforeItems(), "another item " + itemIdx]);
}

function addAfterItem() {
  itemIdx++;
  setAfterItems([...getAfterItems(), "another item " + itemIdx]);
}

function addScrollCaseBeforeItem() {
  itemIdx++;
  setScrollCaseBeforeItems([...getScrollCaseBeforeItems(), "another item " + itemIdx]);
}

function addScrollCaseAfterItem() {
  itemIdx++;
  setScrollCaseAfterItems([...getScrollCaseAfterItems(), "another item " + itemIdx]);
}

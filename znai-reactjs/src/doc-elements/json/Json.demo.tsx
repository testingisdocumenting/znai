/*
 * Copyright 2022 znai maintainers
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
import Json from "./Json";
import { TwoSidesLayoutRightPart } from "../page/two-sides/TwoSidesLayout";
import { Registry } from "react-component-viewer";
import { elementsLibrary } from "../DefaultElementsLibrary";
import { TooltipRenderer } from "../../components/Tooltip";

const withNulls = {
  k1: null,
  k2: "v1",
  k3: null,
};

const arraySimpleData = ["word", "red", "another"];
const objectSimpleData = { key1: 'value1 "quote" part', key2: "value2" };
const objectNestedData = {
  key1: "value1",
  key2: {
    key21: "value21",
    key22: "value22",
    key23: {
      key231: "value231",
    },
  },
  key3: {
    key31: 100,
    key32: "value32",
  },
  key4: [1, 2, 3, 4, 5],
};

const objectEmptyData = {
  key1: "value1",
  key2: {
    key21: "value21",
    key22: [],
    key23: {},
  },
  key3: [{}, {}],
  key4: [[], []],
};

const objectNestedDataLongNames = {
  properties: {
    code: {
      type: "integer",
      format: "int32",
    },
    message: {
      type: "string",
    },
  },
};

const arrayOfObjectWithinObjectData = {
  accounts: [
    { name: "ta1", amount: 200 },
    { name: "ta2", amount: 150 },
  ],
};
const arrayOfObject = [
  { name: "ta1", amount: 200 },
  { name: "ta2", amount: 150 },
];

export function jsonDemo(registry: Registry) {
  registry
    .add("with nulls", () => <Json data={withNulls} highlightValues={["root.k3"]} />)
    .add("array of simple", () => <Json data={arraySimpleData} highlightValues={["root[1]"]} />)
    .add("with title", () => <Json data={arraySimpleData} highlightValues={["root[1]"]} title="Response" />)
    .add("highlight keys", () => <Json data={objectNestedData} highlightKeys={["root.key1", "root.key3.key31"]} />)
    .add("callouts by path", () => (
      <>
        <TooltipRenderer />
        <Json
          data={objectNestedData}
          calloutsByPath={{ "root.key1": [{ type: "SimpleText", text: "hello world" }] }}
          elementsLibrary={elementsLibrary}
        />
      </>
    ))
    .add("record", () => <Json data={objectSimpleData} />)
    .add("nested record", () => (
      <Json data={objectNestedData} highlightValues={["root.key2.key22", "root.key3.key31"]} />
    ))
    .add("nested with empty", () => <Json data={objectEmptyData} />)
    .add("nested record with collapsed entry", () => (
      <Json
        data={objectNestedData}
        highlightValues={["root.key2.key22", "root.key3.key31"]}
        collapsedhighlightValues={["root.key2", "root.key4"]}
      />
    ))
    .add("nested record right side background", () => (
      <TwoSidesLayoutRightPart>
        <Json data={objectNestedData} highlightValues={["root.key2.key22", "root.key3.key31"]} />
      </TwoSidesLayoutRightPart>
    ))
    .add("nested record with collapsed entry right side background", () => (
      <TwoSidesLayoutRightPart>
        <Json
          data={objectNestedData}
          highlightValues={["root.key2.key22", "root.key3.key31"]}
          collapsedhighlightValues={["root.key2", "root.key4"]}
        />
      </TwoSidesLayoutRightPart>
    ))
    .add("nested record with long name", () => <Json data={objectNestedDataLongNames} />)
    .add("array of records within object", () => <Json data={arrayOfObjectWithinObjectData} />)
    .add("with read more", () => <Json data={arrayOfObjectWithinObjectData} readMore={true} />)
    .add("with line highlights", () => <Json data={arrayOfObjectWithinObjectData} highlight={[1, 3]} />)
    .add("array of records", () => <Json data={arrayOfObject} />)
    .add("code references", () => <Json data={objectNestedData} references={buildReferences()} />);
}

function buildReferences() {
  return {
    key23: { pageUrl: "#key23" },
  };
}

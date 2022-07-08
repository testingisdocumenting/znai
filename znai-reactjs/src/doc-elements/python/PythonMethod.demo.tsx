/*
 * Copyright 2022 znai maintainers
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

import { Registry } from "react-component-viewer";
import { PythonMethod } from "./PythonMethod";
import { PythonArg } from "./PythonArg";

export function pythonMethodDemo(registry: Registry) {
  registry.add("no args", () => <PythonMethod qualifiedName="fin.money.split" args={[]} />);
  registry.add("no types", () => <PythonMethod qualifiedName="fin.money.split" args={noTypeArgs()} />);
  registry.add("default values", () => <PythonMethod qualifiedName="fin.money.split" args={defaultValuesArgs()} />);
  registry.add("method url and default values", () => (
    <PythonMethod qualifiedName="fin.money.split" args={defaultValuesArgs()} url="#definition" />
  ));
  registry.add("simple types", () => <PythonMethod qualifiedName="fin.money.split" args={simpleTypeArgs()} />);
  registry.add("simple types with default values", () => (
    <PythonMethod qualifiedName="fin.money.split" args={simpleTypeWithDefaultsArgs()} />
  ));
  registry.add("position only", () => <PythonMethod qualifiedName="fin.money.split" args={positionalArgs()} />);
  registry.add("args", () => <PythonMethod qualifiedName="fin.money.split" args={argsArgs()} />);
  registry.add("kwargs", () => <PythonMethod qualifiedName="fin.money.split" args={kwargsArgs()} />);
  registry.add("all combined", () => <PythonMethod qualifiedName="fin.money.split" args={allCombinedArgs()} />);
  registry.add("no name qualifier", () => (
    <PythonMethod qualifiedName="fin.money.split" args={allCombinedArgs()} hideNameQualifier={true} />
  ));
}

function noTypeArgs(): PythonArg[] {
  return [
    {
      name: "price",
      defaultValue: "",
      category: "REGULAR",
      type: [],
    },
    {
      name: "dept",
      defaultValue: "",
      category: "REGULAR",
      type: [],
    },
  ];
}

function defaultValuesArgs(): PythonArg[] {
  return [
    {
      name: "price",
      defaultValue: "Money(100)",
      category: "REGULAR",
      type: [],
    },
    {
      name: "dept",
      defaultValue: "200",
      category: "REGULAR",
      type: [],
    },
  ];
}

function simpleTypeArgs(): PythonArg[] {
  return [
    {
      name: "price",
      defaultValue: "",
      category: "REGULAR",
      type: [{ text: "fin.money.Money", url: "#money" }],
    },
    {
      name: "dept",
      defaultValue: "",
      category: "REGULAR",
      type: [{ text: "fin.money.Dept", url: "" }],
    },
  ];
}

function simpleTypeWithDefaultsArgs(): PythonArg[] {
  return [
    {
      name: "price",
      defaultValue: "Money(100)",
      category: "REGULAR",
      type: [{ text: "fin.money.Money", url: "#money" }],
    },
    {
      name: "dept",
      defaultValue: "",
      category: "REGULAR",
      type: [{ text: "fin.money.Dept", url: "" }],
    },
  ];
}

function positionalArgs(): PythonArg[] {
  return [
    {
      name: "label",
      defaultValue: "",
      category: "POS_ONLY",
      type: [{ text: "str", url: "" }],
    },
    {
      name: "prices",
      defaultValue: "",
      category: "POS_ONLY",
      type: [{ text: "fin.money.Money", url: "#money" }],
    },
    {
      name: "misc",
      defaultValue: "",
      category: "REGULAR",
      type: [],
    },
  ];
}

function argsArgs(): PythonArg[] {
  return [
    {
      name: "label",
      defaultValue: '"hello"',
      category: "REGULAR",
      type: [{ text: "str", url: "" }],
    },
    {
      name: "prices",
      defaultValue: "",
      category: "ARGS",
      type: [{ text: "fin.money.Money", url: "#money" }],
    },
  ];
}

function kwargsArgs(): PythonArg[] {
  return [
    {
      name: "label",
      defaultValue: '"hello"',
      category: "REGULAR",
      type: [{ text: "str", url: "" }],
    },
    {
      name: "opts",
      defaultValue: "",
      category: "KWARGS",
      type: [],
    },
  ];
}

function allCombinedArgs(): PythonArg[] {
  return [
    {
      name: "label_of_label",
      defaultValue: "",
      category: "POS_ONLY",
      type: [{ text: "str", url: "" }],
    },
    {
      name: "price",
      defaultValue: "",
      category: "POS_ONLY",
      type: [{ text: "fin.money.Money", url: "#money" }],
    },
    {
      name: "misc",
      defaultValue: "",
      category: "REGULAR",
      type: [],
    },
    {
      name: "names",
      defaultValue: "",
      category: "ARGS",
      type: [{ text: "str" }],
    },
    {
      name: "size",
      defaultValue: "100",
      category: "KW_ONLY",
      type: [{ text: "int" }],
    },
    {
      name: "long_title_prefix",
      defaultValue: '""',
      category: "KW_ONLY",
      type: [{ text: "str" }],
    },
    {
      name: "other_options",
      defaultValue: "",
      category: "KWARGS",
      type: [],
    },
  ];
}

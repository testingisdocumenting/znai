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

const emptyDecorators: string[] = [];

export function pythonMethodDemo(registry: Registry) {
  registry.add("no args", () => (
    <PythonMethod qualifiedName="fin.money.split" args={[]} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("no types", () => (
    <PythonMethod qualifiedName="fin.money.split" args={noTypeArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("default values", () => (
    <PythonMethod
      qualifiedName="fin.money.split"
      args={defaultValuesArgs()}
      decorators={emptyDecorators}
      returns={[]}
    />
  ));
  registry.add("method url and default values", () => (
    <PythonMethod
      qualifiedName="fin.money.split"
      args={defaultValuesArgs()}
      url="#definition"
      decorators={emptyDecorators}
      returns={[]}
    />
  ));
  registry.add("simple types", () => (
    <PythonMethod qualifiedName="fin.money.split" args={simpleTypeArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("complex types", () => (
    <PythonMethod qualifiedName="fin.money.split" args={complexTypeArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("simple types with default values", () => (
    <PythonMethod
      qualifiedName="fin.money.split"
      args={simpleTypeWithDefaultsArgs()}
      decorators={emptyDecorators}
      returns={[]}
    />
  ));
  registry.add("with return", () => (
    <PythonMethod
      qualifiedName="fin.money.split"
      args={simpleTypeArgs()}
      decorators={emptyDecorators}
      returns={[{ text: "fin.money.Money", url: "#money" }]}
    />
  ));
  registry.add("position only", () => (
    <PythonMethod qualifiedName="fin.money.split" args={positionalArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("args", () => (
    <PythonMethod qualifiedName="fin.money.split" args={argsArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("kwargs", () => (
    <PythonMethod qualifiedName="fin.money.split" args={kwargsArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("all combined", () => (
    <PythonMethod qualifiedName="fin.money.split" args={allCombinedArgs()} decorators={emptyDecorators} returns={[]} />
  ));
  registry.add("no name qualifier", () => (
    <PythonMethod
      qualifiedName="fin.money.split"
      args={allCombinedArgs()}
      decorators={emptyDecorators}
      hideNameQualifier={true}
      returns={[]}
    />
  ));
  registry.add("with decorators", () => (
    <PythonMethod
      qualifiedName="fin.money.split"
      args={allCombinedArgs()}
      decorators={["staticmethod"]}
      hideNameQualifier={true}
      returns={[]}
    />
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

function complexTypeArgs(): PythonArg[] {
  return [
    {
      name: "price",
      defaultValue: "",
      category: "REGULAR",
      type: [{ text: "list[" }, { text: "fin.money.Money", url: "#money" }, { text: "]" }],
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

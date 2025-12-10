/*
 * Copyright 2021 znai maintainers
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
import { DoxygenMember } from "./DoxygenMember";
import { doxygenMethodParameters, doxygenMethodTemplateParameters } from "../demo-utils/contentGenerators";

export function doxygenMemberDemo(registry: Registry) {
  registry.add("default", () => (
    <DoxygenMember
      compoundName="utils::nested"
      name="my_func"
      declType=""
      isFunction={true}
      isVirtual={false}
      isConst={false}
      isStatic={false}
      isNoExcept={false}
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));

  registry.add("with template params", () => (
    <DoxygenMember
      compoundName="utils::nested"
      name="my_func"
      declType=""
      isFunction={true}
      isVirtual={false}
      isConst={false}
      isStatic={false}
      isNoExcept={false}
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
      templateParameters={doxygenMethodTemplateParameters}
    />
  ));

  registry.add("with link and no compound name", () => (
    <DoxygenMember
      isFunction={true}
      isVirtual={false}
      isConst={false}
      isStatic={false}
      isNoExcept={false}
      compoundName=""
      name="my_func"
      declType=""
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));

  registry.add("static const", () => (
    <DoxygenMember
      isFunction={true}
      isVirtual={false}
      isConst={true}
      isStatic={true}
      isNoExcept={false}
      compoundName="utils::MyClass"
      name="my_func"
      declType=""
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));

  registry.add("static var", () => (
    <DoxygenMember
      isFunction={false}
      isVirtual={false}
      isConst={false}
      isStatic={true}
      isNoExcept={false}
      compoundName="utils::MyClass"
      name="var_name"
      declType=""
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={[]}
    />
  ));

  registry.add("virtual", () => (
    <DoxygenMember
      isFunction={true}
      isVirtual={true}
      isConst={false}
      isStatic={false}
      isNoExcept={false}
      compoundName="utils::MyClass"
      name="my_func"
      declType=""
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));

  registry.add("noexcept", () => (
    <DoxygenMember
      isFunction={true}
      isVirtual={false}
      isConst={false}
      isStatic={false}
      isNoExcept={true}
      compoundName="utils::MyClass"
      name="my_func"
      declType=""
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));

  registry.add("noexcept const", () => (
    <DoxygenMember
      isFunction={true}
      isVirtual={false}
      isConst={true}
      isStatic={false}
      isNoExcept={true}
      compoundName="utils::MyClass"
      name="my_func"
      declType=""
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));

  registry.add("decltype", () => (
    <DoxygenMember
      isFunction={true}
      isVirtual={false}
      isConst={false}
      isStatic={false}
      isNoExcept={true}
      compoundName="utils::MyClass"
      name="my_func"
      declType="decltype(p1)"
      url="#test_link"
      returnType={[{ text: "MyClass", url: "#MyClass__8x" }]}
      parameters={doxygenMethodParameters}
    />
  ));
}

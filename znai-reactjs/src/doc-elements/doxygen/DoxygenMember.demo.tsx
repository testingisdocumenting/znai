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
import { updateGlobalAnchors } from "../references/globalAnchors";

export function doxygenMemberDemo(registry: Registry) {
  updateGlobalAnchors({ test_link: "list/blah" });

  registry.add("default", () => (
    <DoxygenMember
      compoundName="utils::nested"
      name="my_func"
      returnType={[{ text: "MyClass", refId: "MyClass__8x" }]}
      parameters={[
        {
          name: "p_one",
          type: [
            { text: "const ", refId: "" },
            { text: "MyClass", refId: "MyClass__8x" },
          ],
        },
        {
          name: "p_two",
          type: [
            { text: "const ", refId: "" },
            { text: "AnotherClass", refId: "AnotherClass__9x" },
          ],
        },
        {
          name: "p_three",
          type: [
            { text: "const ", refId: "" },
            { text: "AnotherClass2", refId: "AnotherClass2__9x" },
          ],
        },
      ]}
    />
  ));
  registry.add("with link", () => (
    <DoxygenMember
      compoundName=""
      name="my_func"
      refId="test_link"
      returnType={[{ text: "MyClass", refId: "MyClass__8x" }]}
      parameters={[
        {
          name: "p_one",
          type: [
            { text: "const ", refId: "" },
            { text: "MyClass", refId: "MyClass__8x" },
          ],
        },
        {
          name: "p_two",
          type: [
            { text: "const ", refId: "" },
            { text: "AnotherClass", refId: "AnotherClass__9x" },
          ],
        },
        {
          name: "p_three",
          type: [
            { text: "const ", refId: "" },
            { text: "AnotherClass2", refId: "AnotherClass2__9x" },
          ],
        },
      ]}
    />
  ));
}

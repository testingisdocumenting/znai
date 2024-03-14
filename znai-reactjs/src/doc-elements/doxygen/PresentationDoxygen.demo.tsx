/*
 * Copyright 2024 znai maintainers
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

import { createPresentationDemo } from "../demo-utils/PresentationDemo";
import { Registry } from "react-component-viewer";
import {
  doxygenMethodParameters,
  doxygenMethodParametersLong,
  doxygenMethodParametersSingle,
} from "../demo-utils/contentGenerators";

export function doxygenPresentationDemo(registry: Registry) {
  registry.add(
    "full signature",
    createPresentationDemo([
      {
        type: "DoxygenMember",
        name: "my_func",
        declType: "",
        isFunction: true,
        isVirtual: true,
        isConst: true,
        isStatic: true,
        isNoExcept: true,
        returnType: [{ text: "MyClass", url: "#MyClass__8x" }],
        parameters: doxygenMethodParametersLong,
      },
    ])
  );
  registry.add(
    "fully qualified name no params",
    createPresentationDemo([
      {
        isStatic: false,
        visibility: "public",
        isFunction: true,
        kind: "function",
        declType: "",
        isNoExcept: false,
        isConst: false,
        templateParameters: [],
        compoundKind: "class",
        name: "bark",
        isVirtual: false,
        parameters: [],
        compoundName: "utils::second::ThirdClass",
        returnType: [
          {
            text: "void",
            url: "",
          },
        ],
        type: "DoxygenMember",
      },
    ])
  );
  registry.add(
    "fully qualified name with single parameter",
    createPresentationDemo([
      {
        isStatic: false,
        visibility: "public",
        isFunction: true,
        kind: "function",
        declType: "",
        isNoExcept: false,
        isConst: true,
        templateParameters: [],
        compoundKind: "class",
        name: "bark",
        isVirtual: false,
        parameters: doxygenMethodParametersSingle,
        compoundName: "utils::second::ThirdClass",
        returnType: [
          {
            text: "void",
            url: "",
          },
        ],
        type: "DoxygenMember",
      },
    ])
  );
  registry.add(
    "fully qualified name with params",
    createPresentationDemo([
      {
        isStatic: false,
        visibility: "public",
        isFunction: true,
        kind: "function",
        declType: "",
        isNoExcept: false,
        isConst: true,
        templateParameters: [],
        compoundKind: "class",
        name: "bark",
        isVirtual: false,
        parameters: doxygenMethodParameters,
        compoundName: "utils::second::ThirdClass",
        returnType: [
          {
            text: "void",
            url: "",
          },
        ],
        type: "DoxygenMember",
      },
    ])
  );
}

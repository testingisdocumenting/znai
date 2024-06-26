/*
 * Copyright 2024 TWO SIGMA OPEN SOURCE, LLC
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

const arrayOfObjectWithinObjectData = {
  accounts: [
    { name: "ta1", amount: 200 },
    { name: "ta2", amount: 150 },
  ],
};

export function jsonPresentationDemo(registry: Registry) {
  registry
    .add(
      "no path",
      createPresentationDemo([
        {
          type: "Json",
          data: arrayOfObjectWithinObjectData,
        },
      ])
    )
    .add(
      "highlight values",
      createPresentationDemo([
        {
          type: "Json",
          data: arrayOfObjectWithinObjectData,
          highlightValues: ["root.accounts[0].name"],
        },
      ])
    );
}

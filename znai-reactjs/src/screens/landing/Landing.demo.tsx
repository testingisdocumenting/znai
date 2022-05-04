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
import { Landing, LandingDocumentation } from "./Landing";
import { Registry } from "react-component-viewer";

const documentations: LandingDocumentation[] = [
  {
    id: "hipchat",
    name: "HipChat",
    category: "Collaboration",
    description: "short description of HipChat. short description of HipChat",
    url: "NA",
  },
  {
    id: "slack",
    name: "Slack",
    category: "Collaboration",
    url: "https://slack.com",
    description: "external link example",
  },
  {
    id: "znai",
    name: "Znai",
    category: "Documentation",
    description: "short description of znai. short description of znai. ",
    url: "NA",
  },
  {
    id: "python",
    name: "Python",
    category: "Languages",
    description: "short description of python. short description of python. ",
    url: "NA",
  },
  {
    id: "java",
    name: "Java",
    category: "Languages",
    description: "short description of java. short description of java. ",
    url: "NA",
  },
  {
    id: "javascript",
    name: "JavaScript",
    category: "Languages",
    description: "short description of javascript. short description of javascript. ",
    url: "NA",
  },
  {
    id: "kotlin",
    name: "Kotlin",
    category: "Languages",
    description:
      "short description of kotlin. short description of kotlin. short description of kotlin. short description of kotlin. ",
    url: "NA",
  },
];

export function landingDemo(registry: Registry) {
  registry
    .add("landing", () => <Landing documentations={documentations} title="Company" type="Guides" />)
    .add("landing dark theme", () => (
      <div className="with-theme theme-znai-dark">
        <Landing documentations={documentations} title="Company" type="Guides" />
      </div>
    ));
}

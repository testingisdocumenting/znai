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

import Mermaid from "./Mermaid";
import { Registry } from "react-component-viewer";

export function mermaidDemo(registry: Registry) {
  registry.add("simple ", () => <Mermaid mermaid={"graph TD; A-->B; B-->C;"} />);
  registry.add("wide", () => (
    <Mermaid
      wide={true}
      mermaid={
        "sequenceDiagram\n" +
        "    par Alice to Bob\n" +
        "        Alice->>Bob: Go help John\n" +
        "    and Alice to John\n" +
        "        Alice->>John: I want this done today\n" +
        "        par John to Charlie\n" +
        "            John->>Charlie: Can we do this today?\n" +
        "        and John to Diana\n" +
        "            John->>Diana: Can you help us today?\n" +
        "    and Alice to Carl\n" +
        "        Alice->>Carl: I also want this done today\n" +
        "    end\n" +
        "    end\n"
      }
    />
  ));
}

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

import type { Registry} from "react-component-viewer";
import { simulateState } from "react-component-viewer";
import { ContainerTitle } from "./ContainerTitle";

import "./ContainerTitle.demo.css";

const [getCollapsed, setCollapsed] = simulateState(true);

export function containerTitleDemo(registry: Registry) {
  registry
    .add("only title", () => <ContainerTitle title="snippet title" />)
    .add("with collapse", () => (
      <ContainerTitle title="snippet title" collapsed={getCollapsed()} onCollapseToggle={toggleCollapse} />
    ))
    .add("with anchor", () => <ContainerTitle title="snippet title" anchorId="my-code-anchor" />)
    .add("with collapse and anchor", () => (
      <ContainerTitle
        title="snippet title"
        collapsed={getCollapsed()}
        onCollapseToggle={toggleCollapse}
        anchorId="my-code-anchor"
      />
    ))
    .add("custom class", () => (
      <ContainerTitle
        title="snippet title"
        additionalTitleClassNames="demo-center"
        collapsed={getCollapsed()}
        onCollapseToggle={toggleCollapse}
        anchorId="my-code-anchor"
      />
    ));
}

function toggleCollapse() {
  setCollapsed(!getCollapsed());
}

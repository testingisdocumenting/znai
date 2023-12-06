/*
 * Copyright 2020 znai maintainers
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
import { Iframe } from "./Iframe";

export function iframeDemo(registry: Registry) {
  registry.add("non video content", () => <Iframe src="/frame-content.html" fit={true} />);
  registry.add("non video content with title", () => (
    <Iframe src="/frame-content.html" title="demo title" fit={true} />
  ));
  registry.add("content css vars override", () => (
    <Iframe
      src="/frame-content.html"
      title="demo title"
      fit={true}
      light={{ "--backgroundColor": "#abc" }}
      dark={{ "--backgroundColor": "#eee" }}
    />
  ));
  registry.add("default aspect", () => <Iframe src="https://www.youtube.com/embed/tgbNymZ7vqY" title="demo title" />);
  registry.add("4:3 aspect", () => (
    <Iframe aspectRatio="4:3" src="https://www.youtube.com/embed/tgbNymZ7vqY" title="demo title" />
  ));
}

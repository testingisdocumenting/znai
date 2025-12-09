/*
 * Copyright 2025 znai maintainers
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
import type { Registry } from "react-component-viewer";
import type { Line } from "./PreviewConsoleOutput";
import { PreviewConsoleOutput } from "./PreviewConsoleOutput";

export function previewConsoleOutputDemo(registry: Registry) {
  const lines: Line[] = [
    {
      type: "out",
      parts: [
        { type: "text", value: "hello" },
        { type: "color", value: "GREEN" },
        { type: "text", value: " world" },
        { type: "text", value: " another " },
        { type: "color", value: "BLUE" },
        { type: "text", value: "different color" },
      ],
    },
    {
      type: "err",
      parts: [{ type: "text", value: "error processing file" }],
    },
  ];
  const props = { lines };

  registry.add("output", () => <PreviewConsoleOutput {...props} />);
}

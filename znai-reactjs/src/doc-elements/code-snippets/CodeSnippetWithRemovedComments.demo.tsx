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

import { Registry } from "react-component-viewer";
import { Snippet } from "./Snippet";
import React from "react";

export function codeSnippetWithRemovedCommentsDemo(registry: Registry) {
  registry.add("java removed comments", () => (
    <Snippet lang="java" snippet={javaWithComments()} commentsType="remove" />
  ));

  registry.add("python removed comments", () => (
    <Snippet lang="python" snippet={pythonWithComments()} commentsType="remove" />
  ));
}

function javaWithComments() {
  return (
    "class InternationalPriceService implements PriceService {\n" +
    "    private static void main(String... args) {\n" +
    "    // comment one\n" +
    "    // comment two\n" +
    "        ... // code goes here\n" +
    "    } // code stops here\n" +
    "}\n"
  );
}

function pythonWithComments() {
  return `def method:
    # hello message
    # on multiple lines
    print("hello") # inlined
`;
}

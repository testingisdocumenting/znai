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

import lunr from "lunr";
import { lunrQuery } from "./Search.js";

function createIndex(data) {
  return lunr(function() {
    this.ref("id");
    this.field("section");
    this.field("pageTitle");
    this.field("pageSection");
    this.field("textStandard");
    this.field("textHigh");

    this.metadataWhitelist = ["position"];

    data.forEach(function(e) {
      this.add({
        id: e[0],
        section: e[1],
        pageTitle: e[2],
        pageSection: e[3],
        textStandard: e[4],
        textHigh: e[5]
      });
    }, this);
  });
}

const data = [
  ["id1","chapter", "My important page title","page section", "what is this", "what is this"],
  ["id2","chapter", "Different page title","page section", "important important important", "important important important"],
]

let index = createIndex(data);

describe('search', () => {
  it("title boost", () => {
    const result = lunrQuery(lunr, index, "important");
    console.log(result)
  })
})
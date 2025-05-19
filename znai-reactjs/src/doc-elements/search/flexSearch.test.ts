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

import { describe } from "vitest";
import { createLocalSearchIndex, searchWithHighlight, truncateQueryByMinLength } from "./flexSearch";

describe("flex search", () => {
  it("multiple words highlight", () => {
    const index = createLocalSearchIndex();
    index.add({
      id: "id1",
      title: "some title",
      content: "running webs is future for you",
    });
    index.add({
      id: "id2",
      title: "some title",
      content: "running fast into the future for you",
    });
    index.add({
      id: "id3",
      title: "running future",
      content: "brown fox",
    });

    const result = searchWithHighlight(index, "ru future");
    expect(result).toEqual([
      { id: "id3", type: "title", termsToHighlight: [] },
      {
        id: "id1",
        type: "content",
        termsToHighlight: ["running", "future"],
      },
      {
        id: "id2",
        type: "content",
        termsToHighlight: ["running", "future"],
      },
    ]);
  });

  it("high content should go first", () => {
    const index = createLocalSearchIndex();
    index.add({
      id: "id1",
      title: "some title",
      content: "running webs is future for you",
    });
    index.add({
      id: "id3",
      title: "running future",
      content: "apiCall",
    });
    index.add({
      id: "id2",
      title: "some title",
      contentHigh: "apiCall",
    });
    const result = searchWithHighlight(index, "apicall");
    expect(result).toEqual([
      { id: "id2", type: "contentHigh", termsToHighlight: ["apiCall"] },
      { id: "id3", type: "content", termsToHighlight: ["apiCall"] },
    ]);
  });

  it("min query term length", () => {
    expect(truncateQueryByMinLength("ty", 3)).toEqual("");
    expect(truncateQueryByMinLength("typing sl", 3)).toEqual("typing");
    expect(truncateQueryByMinLength("typing slo", 3)).toEqual("typing slo");
  });
});

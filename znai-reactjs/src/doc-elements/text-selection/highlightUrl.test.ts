/*
 * Copyright 2026 znai maintainers
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

import { buildHighlightUrl } from "./highlightUrl";

function setUrl(pathAndSearch: string) {
  window.history.replaceState(null, "", pathAndSearch);
}

beforeEach(() => {
  setUrl("/docs/page/");
});

describe("buildHighlightUrl", () => {
  const basicParams = { prefix: "a", selection: "b", suffix: "c", question: "", context: "" };

  it("sets highlight params and appends trailing slash when missing", () => {
    setUrl("/docs/page");
    const url = new URL(buildHighlightUrl(basicParams));
    expect(url.pathname).toEqual("/docs/page/");
    expect(url.searchParams.get("highlightPrefix")).toEqual(encodeURIComponent("a"));
    expect(url.searchParams.get("highlightSelection")).toEqual(encodeURIComponent("b"));
    expect(url.searchParams.get("highlightSuffix")).toEqual(encodeURIComponent("c"));
  });

  it("preserves arbitrary existing query params (tabId, pageTabId, or anything else)", () => {
    setUrl("/docs/page/?tabId=Python,Mac&pageTabId=advanced&office=NYC");
    const url = new URL(buildHighlightUrl(basicParams));
    expect(url.searchParams.get("tabId")).toEqual("Python,Mac");
    expect(url.searchParams.get("pageTabId")).toEqual("advanced");
    expect(url.searchParams.get("office")).toEqual("NYC");
  });

  it("replaces stale highlight params instead of carrying them over", () => {
    setUrl(
      "/docs/page/?highlightPrefix=old&highlightSelection=old&highlightSuffix=old&highlightQuestion=old&highlightContext=old&tabId=Python"
    );
    const url = new URL(buildHighlightUrl(basicParams));
    expect(url.searchParams.get("highlightPrefix")).toEqual(encodeURIComponent("a"));
    expect(url.searchParams.get("highlightSelection")).toEqual(encodeURIComponent("b"));
    expect(url.searchParams.get("highlightSuffix")).toEqual(encodeURIComponent("c"));
    expect(url.searchParams.get("highlightQuestion")).toBeNull();
    expect(url.searchParams.get("highlightContext")).toBeNull();
    expect(url.searchParams.get("tabId")).toEqual("Python");
  });
});


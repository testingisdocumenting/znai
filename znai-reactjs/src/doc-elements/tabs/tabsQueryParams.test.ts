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

import {
  readPageTabIdFromQuery,
  readTabIdFromQuery,
  writePageTabIdToQuery,
  writeTabIdToQuery,
} from "./tabsQueryParams";

function setUrl(search: string) {
  window.history.replaceState(null, "", "/page" + search);
}

beforeEach(() => {
  setUrl("");
});

describe("readTabIdFromQuery", () => {
  it("returns null when no tabId or no value matches this set", () => {
    expect(readTabIdFromQuery(["Python", "Java"])).toBeNull();

    setUrl("?tabId=Mac");
    expect(readTabIdFromQuery(["Python", "Java"])).toBeNull();
  });

  it("returns the first value from the comma list that matches this set", () => {
    setUrl("?tabId=Python");
    expect(readTabIdFromQuery(["Python", "Java"])).toEqual("Python");

    setUrl("?tabId=Mac,Java,Linux");
    expect(readTabIdFromQuery(["Python", "Java"])).toEqual("Java");
  });

  it("tolerates percent-encoded commas and surrounding whitespace", () => {
    setUrl("?tabId=Mac%2CJava");
    expect(readTabIdFromQuery(["Python", "Java"])).toEqual("Java");

    setUrl("?tabId=Mac , Java");
    expect(readTabIdFromQuery(["Python", "Java"])).toEqual("Java");
  });
});

describe("writeTabIdToQuery", () => {
  it("sets tabId when none exists", () => {
    writeTabIdToQuery(["Python", "Java"], "Python");
    expect(window.location.search).toEqual("?tabId=Python");
  });

  it("appends when prior value belongs to a different set", () => {
    setUrl("?tabId=Mac");
    writeTabIdToQuery(["Python", "Java"], "Python");
    expect(window.location.search).toEqual("?tabId=Mac,Python");
  });

  it("replaces only the slot belonging to this set in a multi value list", () => {
    setUrl("?tabId=Mac,Python,Blue");
    writeTabIdToQuery(["Python", "Java"], "Java");
    expect(window.location.search).toEqual("?tabId=Mac,Blue,Java");
  });

  it("removes all prior matches for this set before appending", () => {
    setUrl("?tabId=Python,Mac,Java");
    writeTabIdToQuery(["Python", "Java"], "Java");
    expect(window.location.search).toEqual("?tabId=Mac,Java");
  });

  it("preserves unrelated query parameters", () => {
    setUrl("?office=NYC&tabId=Mac");
    writeTabIdToQuery(["Python", "Java"], "Python");
    expect(window.location.search).toEqual("?office=NYC&tabId=Mac,Python");
  });
});

describe("readPageTabIdFromQuery", () => {
  it("returns the value when it matches the list, null otherwise", () => {
    expect(readPageTabIdFromQuery(["intro", "advanced"])).toBeNull();

    setUrl("?pageTabId=advanced");
    expect(readPageTabIdFromQuery(["intro", "advanced"])).toEqual("advanced");

    setUrl("?pageTabId=unknown");
    expect(readPageTabIdFromQuery(["intro", "advanced"])).toBeNull();
  });
});

describe("writePageTabIdToQuery", () => {
  it("sets or overwrites pageTabId", () => {
    writePageTabIdToQuery("intro");
    expect(window.location.search).toEqual("?pageTabId=intro");

    writePageTabIdToQuery("advanced");
    expect(window.location.search).toEqual("?pageTabId=advanced");
  });

  it("preserves tabId and other query parameters", () => {
    setUrl("?tabId=Python&office=NYC");
    writePageTabIdToQuery("advanced");
    expect(window.location.search).toEqual("?tabId=Python&office=NYC&pageTabId=advanced");
  });
});

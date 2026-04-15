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

import { resolveTemplateText } from "./queryParamTemplate";

describe("resolveTemplateText", () => {
  function setQueryParams(params: string) {
    Object.defineProperty(window, "location", {
      value: { search: params },
      writable: true,
    });
  }

  beforeEach(() => {
    setQueryParams("");
  });

  it("returns text as is when no templates", () => {
    expect(resolveTemplateText("")).toEqual("");
    expect(resolveTemplateText("hello world")).toEqual("hello world");
  });

  it("uses default value when no query param", () => {
    expect(resolveTemplateText("printer-${office:NYC}-${floor:5}")).toEqual("printer-NYC-5");
  });

  it("uses query param value over default", () => {
    setQueryParams("?office=SF&floor=3");
    expect(resolveTemplateText("printer-${office:NYC}-${floor:5}")).toEqual("printer-SF-3");
  });

  it("wraps param name with angle brackets when no default and no query param", () => {
    expect(resolveTemplateText("connect to ${office}")).toEqual("connect to <office URL query param is missing>");
  });

  it("resolves mix of query params, defaults, and missing values", () => {
    setQueryParams("?office=SF");
    expect(resolveTemplateText("${office} and ${cluster}")).toEqual("SF and <cluster URL query param is missing>");
  });

  it("replaces multiple instances of the same template", () => {
    expect(resolveTemplateText("${office:NYC}-${office:NYC}")).toEqual("NYC-NYC");
    setQueryParams("?office=SF");
    expect(resolveTemplateText("${office:NYC}-${office:NYC}")).toEqual("SF-SF");
  });

  it("trims whitespace in param names and defaults", () => {
    expect(resolveTemplateText("${ office : NYC }")).toEqual("NYC");
  });
});

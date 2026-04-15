/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import { isTocItemIndex, tableOfContents } from "./TableOfContents";

describe("Table of Contents", () => {
  beforeEach(() => {
    tableOfContents.toc = [
      {
        chapterTitle: "",
        dirName: "",
        items: [
          {
            chapterTitle: "",
            pageTitle: "Index",
            pageMeta: {},
            fileName: "index",
            dirName: "",
          },
        ],
      },
      {
        chapterTitle: "REST",
        dirName: "REST",
        items: [
          {
            chapterTitle: "REST",
            pageTitle: "Getting Started",
            pageMeta: {},
            fileName: "getting-started",
            dirName: "REST",
          },
          {
            chapterTitle: "REST",
            pageTitle: "CRUD",
            pageMeta: {},
            fileName: "CRUD",
            dirName: "REST",
          },
        ],
      },
    ];
  });

  it("should detect if page is index", () => {
    expect(isTocItemIndex({ dirName: "", fileName: "index" })).toBeTruthy();
    expect(isTocItemIndex({ dirName: "", fileName: "" })).toBeFalsy();
    expect(isTocItemIndex({ dirName: "abc", fileName: "index" })).toBeFalsy();
  });

  it("should find next toc item", () => {
    expect(tableOfContents.nextTocItem({ dirName: "REST", fileName: "getting-started" })!.pageTitle).toEqual("CRUD");
    expect(tableOfContents.nextTocItem({ dirName: "REST", fileName: "CRUD" })).toEqual(null);
  });

  it("should find prev toc item", () => {
    expect(tableOfContents.prevTocItem({ dirName: "REST", fileName: "CRUD" })!.pageTitle).toEqual("Getting Started");
    expect(tableOfContents.prevTocItem({ dirName: "REST", fileName: "getting-started" })).toEqual({
      dirName: "",
      fileName: "index",
      pageMeta: {},
      pageTitle: "Index",
      chapterTitle: "",
    });
  });

  describe("skip toc behavior", () => {
    beforeEach(() => {
      tableOfContents.toc = [
        {
          chapterTitle: "Guide",
          dirName: "guide",
          items: [
            {
              chapterTitle: "Guide",
              pageTitle: "Page A",
              pageMeta: {},
              fileName: "page-a",
              dirName: "guide",
            },
            {
              chapterTitle: "Guide",
              pageTitle: "Page B",
              pageMeta: {},
              fileName: "page-b",
              dirName: "guide",
              toc: "skip",
            },
            {
              chapterTitle: "Guide",
              pageTitle: "Page C",
              pageMeta: {},
              fileName: "page-c",
              dirName: "guide",
            },
          ],
        },
      ];
    });

    it("should skip pages with toc skip in nextTocItem", () => {
      expect(tableOfContents.nextTocItem({ dirName: "guide", fileName: "page-a" })!.pageTitle).toEqual("Page C");
    });

    it("should skip pages with toc skip in prevTocItem", () => {
      expect(tableOfContents.prevTocItem({ dirName: "guide", fileName: "page-c" })!.pageTitle).toEqual("Page A");
    });

    it("should return null when all remaining pages are skip", () => {
      tableOfContents.toc = [
        {
          chapterTitle: "Guide",
          dirName: "guide",
          items: [
            {
              chapterTitle: "Guide",
              pageTitle: "Page A",
              pageMeta: {},
              fileName: "page-a",
              dirName: "guide",
            },
            {
              chapterTitle: "Guide",
              pageTitle: "Page B",
              pageMeta: {},
              fileName: "page-b",
              dirName: "guide",
              toc: "skip",
            },
          ],
        },
      ];

      expect(tableOfContents.nextTocItem({ dirName: "guide", fileName: "page-a" })).toBeNull();
    });

  });
});

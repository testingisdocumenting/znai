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

import { DocumentationTrackingListener } from "./DocumentationTracking";
import { DocumentationTracking } from "./DocumentationTracking";

interface CallRecord {
  method: string;
  args: unknown[];
}

function createMockListener(): DocumentationTrackingListener & { calls: CallRecord[] } {
  const calls: CallRecord[] = [];

  return {
    calls,
    onPageOpen: (...args) => calls.push({ method: "onPageOpen", args }),
    onNextPage: (...args) => calls.push({ method: "onNextPage", args }),
    onPrevPage: (...args) => calls.push({ method: "onPrevPage", args }),
    onScrollToSection: (...args) => calls.push({ method: "onScrollToSection", args }),
    onTocItemSelect: (...args) => calls.push({ method: "onTocItemSelect", args }),
    onLinkClick: (...args) => calls.push({ method: "onLinkClick", args }),
    onSearchResultSelect: (...args) => calls.push({ method: "onSearchResultSelect", args }),
    onPresentationOpen: (...args) => calls.push({ method: "onPresentationOpen", args }),
    onInteraction: (...args) => calls.push({ method: "onInteraction", args }),
  };
}

describe("DocumentationTracking", () => {
  let tracking: DocumentationTracking;

  beforeEach(() => {
    tracking = new DocumentationTracking();
  });

  describe("addListener", () => {
    it("should add a listener", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("test-page");

      expect(mockListener.calls).toHaveLength(1);
      expect(mockListener.calls[0]).toEqual({ method: "onPageOpen", args: ["test-page"] });
    });

    it("should support multiple listeners", () => {
      const listener1 = createMockListener();
      const listener2 = createMockListener();

      tracking.addListener(listener1);
      tracking.addListener(listener2);
      tracking.onPageOpen("test-page");

      expect(listener1.calls).toHaveLength(1);
      expect(listener1.calls[0]).toEqual({ method: "onPageOpen", args: ["test-page"] });
      expect(listener2.calls).toHaveLength(1);
      expect(listener2.calls[0]).toEqual({ method: "onPageOpen", args: ["test-page"] });
    });
  });

  describe("onPageOpen", () => {
    it("should notify listeners with page id", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");

      expect(mockListener.calls).toHaveLength(1);
      expect(mockListener.calls[0]).toEqual({ method: "onPageOpen", args: ["page-1"] });
    });

    it("should track current page id", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onNextPage();

      expect(mockListener.calls).toHaveLength(2);
      expect(mockListener.calls[1]).toEqual({ method: "onNextPage", args: ["page-1"] });
    });
  });

  describe("onNextPage", () => {
    it("should notify listeners with current page id", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onNextPage();

      expect(mockListener.calls[1]).toEqual({ method: "onNextPage", args: ["page-1"] });
    });
  });

  describe("onPrevPage", () => {
    it("should notify listeners with current page id", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onPrevPage();

      expect(mockListener.calls[1]).toEqual({ method: "onPrevPage", args: ["page-1"] });
    });
  });

  describe("onScrollToSection", () => {
    it("should notify listeners with page id and section", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      const sectionIdTitle = { id: "section-1", title: "Section 1" };
      tracking.onScrollToSection(sectionIdTitle);

      expect(mockListener.calls[1]).toEqual({
        method: "onScrollToSection",
        args: ["page-1", sectionIdTitle]
      });
    });
  });

  describe("onTocItemSelect", () => {
    it("should notify listeners with page id and toc item", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      const tocItem = { dirName: "section", fileName: "page", pageTitle: "Page Title" };
      tracking.onTocItemSelect(tocItem);

      expect(mockListener.calls[1]).toEqual({
        method: "onTocItemSelect",
        args: ["page-1", tocItem]
      });
    });
  });

  describe("onLinkClick", () => {
    it("should notify listeners with page id and url", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onLinkClick("https://example.com");

      expect(mockListener.calls[1]).toEqual({
        method: "onLinkClick",
        args: ["page-1", "https://example.com"]
      });
    });
  });

  describe("onSearchResultSelect", () => {
    it("should notify listeners with page id, query, and selected page", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onSearchResultSelect("search query", "page-2");

      expect(mockListener.calls[1]).toEqual({
        method: "onSearchResultSelect",
        args: ["page-1", "search query", "page-2"]
      });
    });
  });

  describe("onPresentationOpen", () => {
    it("should notify listeners with page id", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onPresentationOpen();

      expect(mockListener.calls[1]).toEqual({
        method: "onPresentationOpen",
        args: ["page-1"]
      });
    });
  });

  describe("onInteraction", () => {
    it("should notify listeners with page id, type, and id", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);
      tracking.onPageOpen("page-1");
      tracking.onInteraction("click", "button-1");

      expect(mockListener.calls[1]).toEqual({
        method: "onInteraction",
        args: ["page-1", "click", "button-1"]
      });
    });
  });

  describe("partial listener support", () => {
    it("should not error when listener does not implement all methods", () => {
      const calls: string[] = [];
      const partialListener: DocumentationTrackingListener = {
        onPageOpen: () => calls.push("onPageOpen"),
      };

      tracking.addListener(partialListener);

      expect(() => {
        tracking.onPageOpen("page-1");
        tracking.onNextPage();
        tracking.onPrevPage();
      }).not.toThrow();

      expect(calls).toEqual(["onPageOpen"]);
    });

    it("should handle listeners with no matching methods", () => {
      const emptyListener: DocumentationTrackingListener = {};

      tracking.addListener(emptyListener);

      expect(() => {
        tracking.onPageOpen("page-1");
        tracking.onNextPage();
      }).not.toThrow();
    });
  });

  describe("multiple event sequence", () => {
    it("should track page changes correctly", () => {
      const mockListener = createMockListener();
      tracking.addListener(mockListener);

      tracking.onPageOpen("page-1");
      tracking.onNextPage();

      tracking.onPageOpen("page-2");
      tracking.onPrevPage();

      expect(mockListener.calls).toHaveLength(4);
      expect(mockListener.calls[0]).toEqual({ method: "onPageOpen", args: ["page-1"] });
      expect(mockListener.calls[1]).toEqual({ method: "onNextPage", args: ["page-1"] });
      expect(mockListener.calls[2]).toEqual({ method: "onPageOpen", args: ["page-2"] });
      expect(mockListener.calls[3]).toEqual({ method: "onPrevPage", args: ["page-2"] });
    });
  });
});

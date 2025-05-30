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

import { TocItem } from "../TocItem";

/**

 Represents a table of contents for the documentation.

 unlike allPages and searchIndex we load toc as part of page load and not as a separate fetch call.
 It is not as big and is essential for page rendering.

 */
class TableOfContents {
  tocItems: TocItem[] = [];
  _toc: Partial<TocItem>[] = [];

  constructor(toc: Partial<TocItem>[]) {
    this.toc = toc;
  }

  set toc(toc: Partial<TocItem>[]) {
    this.tocItems = [];
    toc.forEach((s) => s.items!.forEach((ti) => this.tocItems.push(ti)));
    this._toc = toc;
  }

  get toc() {
    return [...this._toc];
  }

  get first() {
    return this.tocItems[0];
  }

  hasTocItem(tocItem: TocItem) {
    return this.tocItems.filter((ti) => ti.dirName === tocItem.dirName && ti.fileName === tocItem.fileName).length > 0;
  }

  nextTocItem(tocItem: TocItem) {
    for (let i = 0, len = this.tocItems.length; i < len; i++) {
      const ti = this.tocItems[i];
      if (ti.fileName === tocItem.fileName && ti.dirName === tocItem.dirName) {
        return i + 1 < len ? this.tocItems[i + 1] : null;
      }
    }

    return null;
  }

  prevTocItem(tocItem: TocItem) {
    for (let i = this.tocItems.length - 1; i >= 0; i--) {
      const ti = this.tocItems[i];
      if (ti.fileName === tocItem.fileName && ti.dirName === tocItem.dirName) {
        return i - 1 >= 0 ? this.tocItems[i - 1] : null;
      }
    }

    return null;
  }
}

export function isTocItemIndex(tocItem: TocItem) {
  return tocItem.dirName.length === 0 && tocItem.fileName === "index";
}

// @ts-ignore
const tableOfContents = new TableOfContents(window.toc || []);

export { tableOfContents };

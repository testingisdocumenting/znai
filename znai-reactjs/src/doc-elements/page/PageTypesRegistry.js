/*
 * Copyright 2025 znai maintainers
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

import { DefaultPageContent } from "./default/DefaultPageContent";
import PageDefaultBottomPadding from "./default/PageDefaultBottomPadding";
import { PageDefaultNextPrevNavigation } from "./default/PageDefaultNextPrevNavigation";
import ApiPageContent from "./api/ApiPageContent";
import TwoSidesPageContent from "./two-sides/TwoSidesPageContent";
import TwoSidesNextPrevNavigation from "./two-sides/TwoSidesNextPrevNavigation";
import TwoSidesPageBottomPadding from "./two-sides/TwoSidesPageBottomPadding";

class PageTypesRegistry {
  _contentComponentByType = {};

  expandToc(tocItem) {
    return this._registered(tocItem).expandToc;
  }

  pageContentComponent(tocItem) {
    return this._registered(tocItem).pageContentComponent;
  }

  pageBottomPaddingComponent(tocItem) {
    return this._registered(tocItem).pageBottomPaddingComponent;
  }

  nextPrevNavigationComponent(tocItem) {
    return this._registered(tocItem).nextPrevNavigationComponent;
  }

  registerContentComponent(type, settings) {
    this._contentComponentByType[type] = settings;
  }

  _registered(tocItem) {
    return this._contentComponentByType[pageType(tocItem)];
  }
}

const defaultType = "default";
function pageType(tocItem) {
  if (!tocItem.pageMeta || !tocItem.pageMeta.type) {
    return defaultType;
  }

  return tocItem.pageMeta.type[0];
}

const pageTypesRegistry = new PageTypesRegistry();

pageTypesRegistry.registerContentComponent(defaultType, {
  pageContentComponent: DefaultPageContent,
  nextPrevNavigationComponent: PageDefaultNextPrevNavigation,
  pageBottomPaddingComponent: PageDefaultBottomPadding,
  expandToc: true,
});

pageTypesRegistry.registerContentComponent("api", {
  pageContentComponent: ApiPageContent,
  nextPrevNavigationComponent: PageDefaultNextPrevNavigation,
  pageBottomPaddingComponent: PageDefaultBottomPadding,
  expandToc: false,
});

pageTypesRegistry.registerContentComponent("two-sides", {
  pageContentComponent: TwoSidesPageContent,
  nextPrevNavigationComponent: TwoSidesNextPrevNavigation,
  pageBottomPaddingComponent: TwoSidesPageBottomPadding,
  expandToc: true,
});

export { pageTypesRegistry };

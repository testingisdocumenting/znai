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

import React from "react";

import "./PageTabsSelection.css";

interface Props {
  tabIds: string[];
  activeTabId: string;
  onTabSelect: (tabId: string) => void;
}

function PageTabsSelection({ tabIds, activeTabId, onTabSelect }: Props) {
  return (
    <div className="znai-page-tabs-selection">
      <div className="znai-page-tabs-names content-block">
        {tabIds.map((tabId) => (
          <span
            key={tabId}
            className={"znai-page-tab-name" + (tabId === activeTabId ? " active" : "")}
            onClick={() => onTabSelect(tabId)}
          >
            {tabId}
          </span>
        ))}
      </div>
    </div>
  );
}

export default PageTabsSelection;

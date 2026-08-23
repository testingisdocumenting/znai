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

import { DocElementContent, DocElementPayload } from "../../default-elements/DocElement";
import { walkContentNodes } from "../../default-elements/contentTreeWalker";

const TAB_CONTENT_TYPE = "TabContent";

/**
 * extracts unique tab IDs from all TabContent elements in page content, preserving order.
 * searches recursively through nested content (e.g. TabContent inside AttentionBlock)
 */
export function extractTabIds(pageContent: DocElementContent | undefined): string[] {
  const allTabIds: string[] = [];
  walkContentNodes(pageContent, (el) => {
    const tabId = (el as { tabId?: string }).tabId;
    if (el.type === TAB_CONTENT_TYPE && tabId) {
      allTabIds.push(tabId);
    }
  });

  return [...new Set(allTabIds)];
}

/**
 * builds page content for a selected tab:
 * - keeps all non-TabContent elements as-is
 * - keeps TabContent elements when tabId matches (they render via TabContent component with a marker class)
 * - removes TabContent elements that don't match the selected tab
 * - recursively filters nested content (e.g. TabContent inside AttentionBlock)
 *
 * returns sections with their content filtered
 */
export function buildContentForTab(pageContent: DocElementContent, selectedTabId: string): DocElementContent {
  return pageContent.map((section) => {
    if (!section.content) {
      return section;
    }

    return { ...section, content: filterContentForTab(section.content, selectedTabId) };
  });
}

function filterContentForTab(content: DocElementContent, selectedTabId: string): DocElementContent {
  return content
    .filter((el: any) => el.type !== TAB_CONTENT_TYPE || el.tabId === selectedTabId)
    .map((el: any) => {
      if (Array.isArray(el.content)) {
        return { ...el, content: filterContentForTab(el.content, selectedTabId) };
      }
      return el;
    });
}

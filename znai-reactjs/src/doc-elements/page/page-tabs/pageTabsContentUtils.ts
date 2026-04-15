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

const TAB_CONTENT_TYPE = "TabContent";

interface SectionPayload extends DocElementPayload {
  id: string;
  title: string;
}

/**
 * extracts unique tab IDs from all TabContent elements in page content, preserving order.
 * searches recursively through nested content (e.g. TabContent inside AttentionBlock)
 */
export function extractTabIds(pageContent: DocElementContent | undefined): string[] {
  if (!pageContent) {
    return [];
  }

  const allTabIds: string[] = [];
  for (const section of pageContent) {
    collectTabIds(section.content, allTabIds);
  }

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
export function buildContentForTab(
  pageContent: DocElementContent,
  selectedTabId: string
): DocElementContent {
  return pageContent
    .map((section) => buildSectionForTab(section as SectionPayload, selectedTabId))
    .filter((section): section is SectionPayload => section !== null);
}

function buildSectionForTab(section: SectionPayload, selectedTabId: string): SectionPayload | null {
  if (!section.content) {
    return section;
  }

  const { filtered, hadTabContent } = filterContentForTab(section.content, selectedTabId);

  if (filtered.length === 0 && hadTabContent) {
    return null;
  }

  return { ...section, content: filtered };
}

interface FilterResult {
  filtered: DocElementContent;
  hadTabContent: boolean;
}

function filterContentForTab(content: DocElementContent, selectedTabId: string): FilterResult {
  let hadTabContent = false;

  const filtered = content
    .filter((el: any) => {
      if (el.type === TAB_CONTENT_TYPE) {
        hadTabContent = true;
        return el.tabId === selectedTabId;
      }
      return true;
    })
    .map((el: any) => {
      if (Array.isArray(el.content)) {
        const nested = filterContentForTab(el.content, selectedTabId);
        if (nested.hadTabContent) {
          hadTabContent = true;
        }
        return { ...el, content: nested.filtered };
      }
      return el;
    });

  return { filtered, hadTabContent };
}

function collectTabIds(content: DocElementContent | undefined, result: string[]): void {
  if (!content) {
    return;
  }

  for (const el of content) {
    if (el.type === TAB_CONTENT_TYPE && el.tabId) {
      result.push(el.tabId);
    }
    if (Array.isArray(el.content)) {
      collectTabIds(el.content, result);
    }
  }
}

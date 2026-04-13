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

interface SectionPayload extends DocElementPayload {
  id: string;
  title: string;
}

/**
 * checks if page content has any TabContent elements (at section content level)
 */
export function hasTabContent(pageContent: DocElementContent | undefined): boolean {
  if (!pageContent) {
    return false;
  }

  return pageContent.some((section) => sectionHasTabContent(section));
}

/**
 * extracts unique tab IDs from all TabContent elements in page content, preserving order
 */
export function extractTabIds(pageContent: DocElementContent | undefined): string[] {
  const result: string[] = [];
  if (!pageContent) {
    return result;
  }

  pageContent.forEach((section) => {
    if (section.content) {
      section.content.forEach((el: any) => {
        if (el.type === "TabContent" && el.tabId && result.indexOf(el.tabId) === -1) {
          result.push(el.tabId);
        }
      });
    }
  });

  return result;
}

/**
 * builds page content for a selected tab:
 * - keeps all non-TabContent elements as-is
 * - keeps TabContent elements when tabId matches (they render via TabContent component with a marker class)
 * - removes TabContent elements that don't match the selected tab
 *
 * returns sections with their content filtered
 */
export function buildContentForTab(
  pageContent: DocElementContent | null | undefined,
  selectedTabId: string
): DocElementContent | null | undefined {
  if (!pageContent) {
    return pageContent;
  }

  return pageContent
    .map((section) => buildSectionForTab(section as SectionPayload, selectedTabId))
    .filter((section): section is SectionPayload => section !== null);
}

function buildSectionForTab(section: SectionPayload, selectedTabId: string): SectionPayload | null {
  if (!section.content) {
    return section;
  }

  const filteredContent: DocElementPayload[] = [];

  section.content.forEach((el: any) => {
    if (el.type === "TabContent") {
      if (el.tabId === selectedTabId) {
        filteredContent.push(el);
      }
      // skip non-matching tab content
    } else {
      filteredContent.push(el);
    }
  });

  if (filteredContent.length === 0 && sectionHasTabContent(section)) {
    return null;
  }

  return { ...section, content: filteredContent };
}

function sectionHasTabContent(section: DocElementPayload): boolean {
  if (!section.content) {
    return false;
  }

  return section.content.some((el: any) => el.type === "TabContent");
}

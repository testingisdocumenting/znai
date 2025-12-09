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

export interface DocumentationTrackingListener {
  onPageOpen?(pageId: string): void;
  onNextPage?(pageId: string): void;
  onPrevPage?(pageId: string): void;
  onScrollToSection?(pageId: string, sectionIdTitle: unknown): void;
  onTocItemSelect?(pageId: string, tocItem: unknown): void;
  onLinkClick?(pageId: string, url: string): void;
  onSearchResultSelect?(pageId: string, query: string, selectedPageId: string): void;
  onPresentationOpen?(pageId: string): void;
  onInteraction?(pageId: string, type: string, id: string): void;
}

export class DocumentationTracking {
  private currentPageId: string;
  private listeners: DocumentationTrackingListener[];

  constructor() {
    this.currentPageId = "";
    this.listeners = [];
  }

  addListener(listener: DocumentationTrackingListener): void {
    this.listeners.push(listener);
  }

  removeListener(listener: DocumentationTrackingListener): void {
    this.listeners = this.listeners.filter((l) => l !== listener);
  }

  onPageOpen(pageId: string): void {
    this.currentPageId = pageId;
    this.listeners.forEach((listener) => listener.onPageOpen?.(pageId));
  }

  onNextPage(): void {
    this.listeners.forEach((listener) => listener.onNextPage?.(this.currentPageId));
  }

  onPrevPage(): void {
    this.listeners.forEach((listener) => listener.onPrevPage?.(this.currentPageId));
  }

  onScrollToSection(sectionIdTitle: unknown): void {
    this.listeners.forEach((listener) => listener.onScrollToSection?.(this.currentPageId, sectionIdTitle));
  }

  onTocItemSelect(tocItem: unknown): void {
    this.listeners.forEach((listener) => listener.onTocItemSelect?.(this.currentPageId, tocItem));
  }

  onLinkClick(url: string): void {
    this.listeners.forEach((listener) => listener.onLinkClick?.(this.currentPageId, url));
  }

  onSearchResultSelect(query: string, selectedPageId: string): void {
    this.listeners.forEach((listener) => listener.onSearchResultSelect?.(this.currentPageId, query, selectedPageId));
  }

  onPresentationOpen(): void {
    this.listeners.forEach((listener) => listener.onPresentationOpen?.(this.currentPageId));
  }

  onInteraction(type: string, id: string): void {
    this.listeners.forEach((listener) => listener.onInteraction?.(this.currentPageId, type, id));
  }
}

const documentationTracking = new DocumentationTracking();

export { documentationTracking };

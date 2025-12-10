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

import {DocumentationTrackingListener} from "./DocumentationTracking";
import { getDocId, getDocMeta } from "../../structure/docMeta";

export interface TrackingEvent {
  docId: string;
  eventType: string;
  pageId: string;
  data?: unknown;
}

export class HttpDocumentationTracking implements DocumentationTrackingListener {
  private readonly trackingUrl: string;
  private readonly onError?: (error: Error) => void;

  constructor(trackingUrl: string, onError?: (error: Error) => void) {
    this.trackingUrl = trackingUrl;
    this.onError = onError;
  }

  onPageOpen(pageId: string) {
    void this.sendEvent("pageOpen", pageId);
  }

  onNextPage(pageId: string) {
    void this.sendEvent("nextPage", pageId);
  }

  onPrevPage(pageId: string) {
    void this.sendEvent("prevPage", pageId);
  }

  onScrollToSection(pageId: string, sectionIdTitle: unknown) {
    void this.sendEvent("scrollToSection", pageId, { sectionIdTitle });
  }

  onTocItemSelect(pageId: string, tocItem: unknown) {
    void this.sendEvent("tocItemSelect", pageId, { tocItem });
  }

  onLinkClick(pageId: string, url: string) {
    void this.sendEvent("linkClick", pageId, { url });
  }

  onSearchResultSelect(pageId: string, query: string, selectedPageId: string) {
    void this.sendEvent("searchResultSelect", pageId, { query, selectedPageId });
  }

  onPresentationOpen(pageId: string) {
    void this.sendEvent("presentationOpen", pageId);
  }

  onInteraction(pageId: string, type: string, id: string) {
    void this.sendEvent("interaction", pageId, { type, id });
  }

  private async sendEvent(eventType: string, pageId: string, data?: unknown) {
    const event: TrackingEvent = {
      docId: getDocId(),
      eventType,
      pageId,
      data,
    };

    try {
      const headers = getDocMeta().trackActivityIncludeContentType
        ? {
            "Content-Type": "application/json",
          }
        : undefined;

      const response = await fetch(this.trackingUrl, {
        method: "POST",
        headers: headers,
        body: JSON.stringify(event),
        credentials: "include",
      });

      if (!response.ok) {
        console.error("tracking server error when sending event", event);
        if (this.onError) {
          this.onError(new Error(`HTTP ${response.status}: ${response.statusText}`));
        }
      }
    } catch (error) {
      console.error("error sending tracking event", event, error);
      if (this.onError) {
        this.onError(error instanceof Error ? error : new Error(String(error)));
      }
    }
  }
}

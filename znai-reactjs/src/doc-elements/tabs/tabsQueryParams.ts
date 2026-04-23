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

const TAB_ID_PARAM = "tabId";
const PAGE_TAB_ID_PARAM = "pageTabId";

// tabNames are the identifiers a tab set uses to mark its tabs; they are what gets
// serialized under the tabId / pageTabId URL query params. regular Tabs components
// call these `name` internally (Tab.name field); page tabs call them `tabId` internally
// but pass them as tabNames at this boundary.
//
// the tabId query param is a comma separated list because multiple independent
// Tabs components may live on the same page, each contributing one value.

// returns the first value from the URL's tabId comma list that belongs to this
// tab set, or null if the URL has no value for this set. callers fall back to
// their normal selection logic (history, defaults) when null is returned.
// iteration order follows the URL, not tabNames, so a shared link keeps the
// author's intended ordering.
export function readTabIdFromQuery(tabNames: string[]): string | null {
  const parts = splitValues(currentParams().get(TAB_ID_PARAM));
  for (const p of parts) {
    if (tabNames.indexOf(p) >= 0) {
      return p;
    }
  }
  return null;
}

export function writeTabIdToQuery(tabNames: string[], selected: string): void {
  const params = currentParams();
  const existing = splitValues(params.get(TAB_ID_PARAM));
  const kept = existing.filter((v) => tabNames.indexOf(v) < 0);
  kept.push(selected);
  params.set(TAB_ID_PARAM, kept.join(","));
  replaceUrl(params);
}

export function readPageTabIdFromQuery(tabNames: string[]): string | null {
  const value = currentParams().get(PAGE_TAB_ID_PARAM);
  if (!value) {
    return null;
  }
  return tabNames.indexOf(value) >= 0 ? value : null;
}

export function writePageTabIdToQuery(tabId: string): void {
  const params = currentParams();
  params.set(PAGE_TAB_ID_PARAM, tabId);
  replaceUrl(params);
}

function currentParams(): URLSearchParams {
  return new URLSearchParams(window.location.search);
}

function replaceUrl(params: URLSearchParams): void {
  // URLSearchParams percent-encodes commas; keep them readable for shareable URLs
  const search = params.toString().replace(/%2C/g, ",");
  const url = window.location.pathname + (search ? "?" + search : "") + window.location.hash;
  window.history.replaceState(null, "", url);
}

function splitValues(raw: string | null): string[] {
  if (!raw) {
    return [];
  }
  return raw
    .split(",")
    .map((s) => s.trim())
    .filter((s) => s.length > 0);
}

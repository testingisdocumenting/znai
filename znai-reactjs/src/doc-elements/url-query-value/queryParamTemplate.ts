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

import { useEffect, useReducer } from "react";
import { documentationNavigation } from "../../structure/DocumentationNavigation";

const TEMPLATE_PATTERN = /\$\{([^}]+)}/g;

// subscribes the calling component to URL changes so it re-renders when the
// query string changes — query-param-driven content stays in sync even when
// an ancestor's shouldComponentUpdate would otherwise skip the re-render.
// TODO: switch to useSyncExternalStore once React is upgraded to 18+.
export function useUrlQuerySubscription(): void {
  const [, forceUpdate] = useReducer((x: number) => x + 1, 0);

  useEffect(() => {
    documentationNavigation.addUrlChangeListener(forceUpdate);
    return () => documentationNavigation.removeUrlChangeListener(forceUpdate);
  }, []);
}

export function resolveQueryParamValue(
  paramName: string,
  defaultValue?: string
): { value: string; isMissing: boolean } {
  const urlParams = new URLSearchParams(window.location.search);
  const fromUrl = urlParams.get(paramName);

  if (fromUrl) {
    return { value: fromUrl, isMissing: false };
  }

  if (defaultValue !== undefined) {
    return { value: defaultValue, isMissing: false };
  }

  return { value: paramName, isMissing: true };
}

export function resolveTemplateText(text: string): string {
  return text.replace(TEMPLATE_PATTERN, (_match, inner: string) => {
    const colonIdx = inner.indexOf(":");
    if (colonIdx === -1) {
      const resolved = resolveQueryParamValue(inner.trim());
      return resolved.isMissing ? "<" + inner.trim() + " URL query param is missing>" : resolved.value;
    }

    const paramName = inner.substring(0, colonIdx).trim();
    const defaultValue = inner.substring(colonIdx + 1).trim();
    const resolved = resolveQueryParamValue(paramName, defaultValue);
    return resolved.value;
  });
}

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

const HIGHLIGHT_PREFIX_PARAM = "highlightPrefix";
const HIGHLIGHT_SELECTION_PARAM = "highlightSelection";
const HIGHLIGHT_SUFFIX_PARAM = "highlightSuffix";

export interface HighlightParams {
  prefix: string;
  selection: string;
  suffix: string;
}

export function extractHighlightParams(): HighlightParams | null {
  const params = new URLSearchParams(window.location.search);
  const prefix = params.get(HIGHLIGHT_PREFIX_PARAM);
  const selection = params.get(HIGHLIGHT_SELECTION_PARAM);
  const suffix = params.get(HIGHLIGHT_SUFFIX_PARAM);

  if (prefix !== null && selection && suffix !== null) {
    return {
      prefix: decodeURIComponent(prefix),
      selection: decodeURIComponent(selection),
      suffix: decodeURIComponent(suffix),
    };
  }

  return null;
}

export function buildHighlightUrl(baseUrl: string, params: HighlightParams): string {
  const url = new URL(baseUrl);
  url.searchParams.set(HIGHLIGHT_PREFIX_PARAM, encodeURIComponent(params.prefix));
  url.searchParams.set(HIGHLIGHT_SELECTION_PARAM, encodeURIComponent(params.selection));
  url.searchParams.set(HIGHLIGHT_SUFFIX_PARAM, encodeURIComponent(params.suffix));
  return url.toString();
}

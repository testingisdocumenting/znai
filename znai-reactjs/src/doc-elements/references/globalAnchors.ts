/*
 * Copyright 2021 znai maintainers
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

export type GlobalAnchors = Record<string, string>;

/**
 * global anchors are defined on the documentation level and is available to every component that needs to resolve a global anchor
 * anchors are loaded as a javascript file with a globally defined variable (similar to how Table Of Contents is loaded)
 */
export function getGlobalAnchors(): GlobalAnchors {
  // @ts-ignore
  return window.globalAnchors || {};
}

export function updateGlobalAnchors(anchors: GlobalAnchors) {
  // @ts-ignore
  window.globalAnchors = anchors;
}

export function globalAnchorUrl(anchorId: string) {
  // @ts-ignore
  return getGlobalAnchors()[anchorId];
}

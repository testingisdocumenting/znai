/*
 * Copyright 2020 znai maintainers
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

import { jsonPromise } from "../utils/json";

export interface DocMeta {
  id: string;
  title: string;
  type: string;
  previewEnabled: boolean;
  support?: DocMetaSupport;
  supportMeta?: SupportMeta;
}

export interface SupportMeta {
  link: string;
}

export interface DocMetaSupport {
  link?: string;

  urlToFetchSupportLink?(meta: DocMeta): string;
}

let docMeta: DocMeta = { id: "", title: "", type: "", previewEnabled: false };

function setDocMeta(newDocMeta: DocMeta) {
  docMeta = { ...newDocMeta };
}

function getDocMeta() {
  return { ...docMeta };
}

function mergeDocMeta(newDocMeta: Partial<DocMeta>) {
  setDocMeta({ ...docMeta, ...newDocMeta });
  return getDocMeta();
}

function isPreviewEnabled() {
  return docMeta.previewEnabled;
}

function getDocId() {
  return docMeta.id;
}

let supportLinkPromise: Promise<string> | undefined = undefined;

function getSupportLinkPromise(): Promise<string> {
  if (supportLinkPromise) {
    return supportLinkPromise;
  }
  const support = docMeta.support;
  if (support && support.link) {
    supportLinkPromise = new Promise((resolve) => resolve(support.link!));
  } else if (support && support.urlToFetchSupportLink) {
    // @ts-ignore
    supportLinkPromise = jsonPromise<string, { link: string }>(
      support.urlToFetchSupportLink(getDocMeta())
    )
      // @ts-ignore
      .then((supportMeta) => supportMeta.link);
  } else {
    supportLinkPromise = new Promise((resolve) => resolve(""));
  }

  return supportLinkPromise!;
}

export {
  setDocMeta,
  mergeDocMeta,
  getDocMeta,
  isPreviewEnabled,
  getDocId,
  getSupportLinkPromise,
};

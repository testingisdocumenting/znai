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
  slackChannel?: string;
  sendToSlackUrl?: string;
  slackActiveQuestionsUrl?: string;
  sendToSlackIncludeContentType?: boolean;
  resolveSlackQuestionUrl?: string;
  trackActivityUrl?: string;
  trackActivityIncludeContentType?: boolean;
  docStatsUrl?: string;
  useTopHeader?: boolean;
  hidePresentationTrigger?: boolean;
  support?: DocMetaSupport;
  supportMeta?: SupportMeta;
}

export interface SupportMeta {
  link: string;
  title: string;
}

export interface DocMetaSupport {
  link?: string;
  title?: string;

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
  return docMeta.id || "";
}

function isPresentationButtonVisible() {
  return !docMeta.hidePresentationTrigger;
}

let supportLinkPromise: Promise<SupportMeta> | undefined = undefined;

const defaultSupportTitle = "Support";

function getSupportLinkAndTitlePromise(): Promise<SupportMeta> {
  if (supportLinkPromise) {
    return supportLinkPromise;
  }
  const support = docMeta.support;
  if (support && support.link) {
    supportLinkPromise = new Promise((resolve) =>
      resolve({
        link: support.link!,
        title: support.title || defaultSupportTitle,
      })
    );
  } else if (support && support.urlToFetchSupportLink) {
    // @ts-ignore
    supportLinkPromise = jsonPromise<string, SupportMeta>(support.urlToFetchSupportLink(getDocMeta()))
      // @ts-ignore
      .then((supportMeta) => ({
        link: supportMeta.link,
        title: defaultSupportTitle,
      }));
  } else {
    supportLinkPromise = new Promise((resolve) => resolve({ link: "", title: "" }));
  }

  return supportLinkPromise!;
}

export {
  setDocMeta,
  mergeDocMeta,
  getDocMeta,
  isPreviewEnabled,
  getDocId,
  getSupportLinkAndTitlePromise,
  isPresentationButtonVisible,
};

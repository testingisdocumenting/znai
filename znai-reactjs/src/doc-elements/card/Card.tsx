/*
 * Copyright 2022 znai maintainers
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

import React from "react";

import type { DocElementContent, WithElementsLibrary } from "../default-elements/DocElement";

import { extractLinkTextAndUrl, isContentElementSelfContainedLink } from "./cardContentAnalyzer";
import { LinkWrapper } from "../default-elements/LinkWrapper";
import { isLocalUrl } from "../../structure/links";

import "./Card.css";

interface Props extends WithElementsLibrary {
  title?: string;
  imageSrc?: string;
  imageHeight?: number;
  imageBackground?: string;
  bodyContent: DocElementContent;
}

export function Card({ title, elementsLibrary, imageSrc, imageHeight, imageBackground, bodyContent }: Props) {
  const lastNonLinkIdx = findIndexOfLastNonLinkElement();

  const links = extractLinks();

  const cardClassName = "znai-card " + (isLastNonLinkElementContentContainer() ? " no-bottom-padding" : "");

  return (
    <div className="znai-card-wrapper content-block">
      <div className={cardClassName}>
        {imageSrc && (
          <div className="znai-card-image" style={{ background: imageBackground }}>
            <img src={imageSrc} alt="card visual" style={{ height: imageHeight }} />
          </div>
        )}
        <div className="znai-card-content">
          {title && <div className="znai-card-title">{title}</div>}
          <div className="znai-card-body">
            <elementsLibrary.DocElement content={contentWithoutLinksAtTheEnd()} elementsLibrary={elementsLibrary} />
          </div>
        </div>
        {links.length > 0 && <div className="znai-card-links">{renderLinks(links)}</div>}
      </div>
    </div>
  );

  function renderLinks(links: Array<{ text: string; url: string }>) {
    return links.map((link, idx) => (
      <div className="znai-card-link" key={idx}>
        <LinkWrapper url={link.url} treatAsLocal={isLocalUrl(link.url)}>
          {link.text}
        </LinkWrapper>
      </div>
    ));
  }

  function contentWithoutLinksAtTheEnd() {
    return bodyContent.slice(0, lastNonLinkIdx + 1);
  }

  function extractLinks() {
    const result = [];
    for (let idx = lastNonLinkIdx + 1; idx < bodyContent.length; idx++) {
      result.push(extractLinkTextAndUrl(bodyContent[idx]));
    }

    return result;
  }

  function findIndexOfLastNonLinkElement() {
    let lastNonLinkIdx = bodyContent.length - 1;
    while (lastNonLinkIdx > 0) {
      if (isContentElementSelfContainedLink(bodyContent[lastNonLinkIdx])) {
        lastNonLinkIdx--;
      } else {
        break;
      }
    }

    return lastNonLinkIdx;
  }

  function isLastNonLinkElementContentContainer() {
    if (bodyContent.length === 0) {
      return false;
    }

    const type = bodyContent[lastNonLinkIdx].type;
    return type === "Snippet" || type === "ApiParameters" || type === "Table";
  }
}

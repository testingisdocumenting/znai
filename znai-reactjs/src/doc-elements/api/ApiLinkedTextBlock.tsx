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

import React from "react";

import { ApiLinkedText } from "./ApiLinkedText";
import { globalAnchorUrl } from "../references/globalAnchors";

import { LinkWrapper } from "../default-elements/LinkWrapper";
import { isLocalUrl } from "../../structure/links";

import "./ApiLinkedTextBlock.css";

interface Props {
  linkedText: ApiLinkedText;
}

export function ApiLinkedTextBlock({ linkedText }: Props) {
  if (linkedText.length === 0) {
    return null;
  }

  return (
    <div className="znai-api-text-with-links">
      {linkedText.map((part, idx) => {
        const url = part.refId ? globalAnchorUrl(part.refId) : undefined;
        if (url) {
          const isLocal = isLocalUrl(url);

          return (
            <LinkWrapper key={idx} url={url} treatAsLocal={isLocal}>
              {part.text}
            </LinkWrapper>
          );
        }

        return (
          <span key={idx} className="znai-api-text-with-links-text">
            {part.text}
          </span>
        );
      })}
    </div>
  );
}

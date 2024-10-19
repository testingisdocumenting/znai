/*
 * Copyright 2024 znai maintainers
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
import { DocElementContent, ElementsLibraryMap } from "../default-elements/DocElement";
import { Tooltip } from "../../components/Tooltip";
import "./FootnoteReference.css";

interface Props {
  label: string;
  content: DocElementContent;
  elementsLibrary: ElementsLibraryMap;
}

export function FootnoteReference({ label, content, elementsLibrary }: Props) {
  const tooltipContent = (
    <div className="znai-footnote-preview">
      <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={content} />
    </div>
  );

  const referenceClassName =
    "znai-footnote-reference " + (label.length === 1 ? "single-char-label" : "multi-char-label");
  return (
    <Tooltip
      content={tooltipContent}
      placement="parent-content-block"
      contentClassName="znai-footnote-preview-container"
    >
      <sup className={referenceClassName}>{label}</sup>
    </Tooltip>
  );
}

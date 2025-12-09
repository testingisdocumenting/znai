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

import type { DocElementContent, DocElementProps } from "../default-elements/DocElement";
import { Icon } from "../icons/Icon";

import "./AttentionBlock.css";

interface Props extends DocElementProps {
  attentionType: string;
  label?: string;
  iconTooltip?: string;
  content: DocElementContent;
}

const iconByType: Record<string, string> = {
  note: "info",
  warning: "alert-triangle",
  question: "help-circle",
  avoid: "x-octagon",
  recommendation: "check-circle",
};

export function AttentionBlock({ attentionType, label, iconTooltip, content, elementsLibrary }: Props) {
  const iconId = iconByType[attentionType] || "square";
  return (
    <div className={`znai-attention-block ${attentionType} content-block`}>
      <span className="znai-attention-block-icon" title={tooltipToUse()}>
        <Icon id={iconId} />
        {label && <span className="znai-attention-block-label">{label}:</span>}
      </span>
      <span className="znai-attention-block-content">
        <elementsLibrary.DocElement content={content} elementsLibrary={elementsLibrary} />
      </span>
    </div>
  );

  function tooltipToUse() {
    if (iconTooltip) {
      return iconTooltip;
    }

    return label ? undefined : attentionType;
  }
}

export const presentationAttentionBlock = {
  component: AttentionBlock,
  numberOfSlides: () => 1,
  ignoreNestedContent: () => true,
};

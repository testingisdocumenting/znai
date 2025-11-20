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

import React from 'react'

import { extractHighlightParams } from "./highlightUrl";
import { HighlightedText } from "./HighlightedText";

export function HighlightUrlText({ containerNode }: { containerNode: HTMLElement }) {
  const params = extractHighlightParams();

  return params ? (
    <HighlightedText
      containerNode={containerNode}
      selectedText={params.selection}
      selectedPrefix={params.prefix}
      selectedSuffix={params.suffix}
      question={params.question}
      context={params.context}
      displayBubbleAndScrollIntoView={true}
      additionalView={null}
    />
  ) : null;
}

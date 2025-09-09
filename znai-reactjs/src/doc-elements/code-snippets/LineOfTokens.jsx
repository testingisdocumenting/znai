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

import React from "react";

import { enhanceMatchedTokensWithMeta, lineWithTokensTrimmedOnRight } from "./codeUtils";

import CodeToken from "./CodeToken";

import "./LineOfTokens.css";

const LineOfTokens = ({
  tokens,
  references,
  isHighlighted,
  isPrevHighlighted,
  isNextHighlighted,
  isPresentation,
  wrap,
  endOfLineRender,
  isHidden,
}) => {
  const className =
    "znai-code-line" +
    (isHighlighted ? " highlight" : "") +
    (isHighlighted && !isPrevHighlighted ? " no-highlight-top-neighbour" : "") +
    (isHighlighted && !isNextHighlighted ? " no-highlight-bottom-neighbour" : "") +
    (wrap ? " wrap" : "") +
    (isHidden ? " hidden" : "");

  const trimmedOnRight = lineWithTokensTrimmedOnRight(tokens);
  const enhancedTokens = enhanceTokens(trimmedOnRight);

  return (
    <span className={className}>
      {enhancedTokens.map((t, idx) => (
        <CodeToken key={idx} token={t} isPresentation={isPresentation} />
      ))}
      {endOfLineRender && endOfLineRender()}
      <span>{"\n"}</span>
    </span>
  );

  function enhanceTokens(tokens) {
    if (!references) {
      return tokens;
    }

    return enhanceMatchedTokensWithMeta(
      tokens,
      Object.keys(references),
      () => "link",
      (referenceText) => {
        const reference = references[referenceText];
        return reference.pageUrl;
      }
    );
  }
};

export default LineOfTokens;

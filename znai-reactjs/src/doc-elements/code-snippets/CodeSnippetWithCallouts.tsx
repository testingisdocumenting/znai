/*
 * Copyright 2023 znai maintainers
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

import LineOfTokens from "./LineOfTokens";
import { SnippetCircleBadge } from "./explanations/SnippetCircleBadge";

import { isAllAtOnce } from "../meta/meta";
import { isCommentToken, lineWithTokensTrimmedOnRight } from "./codeUtils";

import { mergeWithGlobalDocReferences } from "../references/globalDocReferences";

import type { SnippetDocReferences } from "../references/SnippetDocReferences";
import type { DocElementContent, ElementsLibraryMap } from "../default-elements/DocElement";

import "./CodeSnippetWithCallouts.css";

interface Props {
  linesOfCode: any[];
  references?: SnippetDocReferences;
  callouts: Record<number, DocElementContent>;
  isPresentation: boolean;
  slideIdx: number;
  wrap: boolean;
  meta: any;
  elementsLibrary: ElementsLibraryMap;
}

const CodeSnippetWithCallouts = ({
  linesOfCode,
  references,
  callouts = {},
  isPresentation,
  meta,
  slideIdx,
  wrap,
  elementsLibrary,
}: Props) => {
  // slideIdx === 0 means no highlights, 1 - first comment, etc
  const highlightIsVisible = slideIdx > 0;

  const className = "code-with-inlined-comments" + (highlightIsVisible ? " with-highlighted-line" : "");
  const mergedReferences = mergeWithGlobalDocReferences(references);

  const lineIdxByBulletNumber: Record<number, number> = {};
  let bulletNumberForCalc = 1;
  Object.keys(callouts).forEach((lineIdx) => {
    lineIdxByBulletNumber[bulletNumberForCalc] = Number(lineIdx);
    bulletNumberForCalc++;
  });

  const isHighlightedByIdx = highlightIsVisible ? linesOfCode.map((_, lineIdx) => isHighlighted(lineIdx)) : [];

  let bulletNumber = 1;
  return (
    <div className={className}>
      <pre>
        {linesOfCode.map((line, lineIdx) => {
          const lineToRender = removeCommentAtTheEnd(line);

          return (
            <LineOfTokens
              key={lineIdx}
              tokens={lineToRender}
              isPrevHighlighted={isHighlightedByIdx[lineIdx - 1]}
              isHighlighted={isHighlightedByIdx[lineIdx]}
              isNextHighlighted={isHighlightedByIdx[lineIdx + 1]}
              references={mergedReferences}
              isPresentation={isPresentation}
              wrap={wrap}
              isHidden={false}
              endOfLineRender={() => {
                const calloutContent = callouts[lineIdx];

                return calloutContent ? (
                  <SnippetCircleBadge
                    className="left-margin"
                    idx={bulletNumber++}
                    tooltip={<elementsLibrary.DocElement content={calloutContent} elementsLibrary={elementsLibrary} />}
                  />
                ) : null;
              }}
            />
          );
        })}
      </pre>
    </div>
  );

  function removeCommentAtTheEnd(line: string) {
    const trimmed = lineWithTokensTrimmedOnRight(line);
    const lastToken = trimmed[trimmed.length - 1];
    if (lastToken && isCommentToken(lastToken)) {
      trimmed.splice(trimmed.length - 1, 1);
    }

    return trimmed;
  }

  function isHighlighted(idx: number) {
    if (isAllAtOnce(meta) && highlightIsVisible) {
      return callouts.hasOwnProperty(idx);
    }

    const lineIdxToHighlight = highlightIsVisible ? lineIdxByBulletNumber[slideIdx] : -1;
    return lineIdxToHighlight === idx;
  }
};

export default CodeSnippetWithCallouts;

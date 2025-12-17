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

import * as React from "react";

import {
  collapseCommentsAboveToMakeCommentOnTheCodeLine,
  isCommentToken,
  removeCommentsFromEachLine,
  splitTokensIntoLines,
  trimComment,
} from "./codeUtils";

import SnippetContainer from "./SnippetContainer";
import CodeSnippetWithCallouts from "./CodeSnippetWithCallouts";
import SimpleCodeSnippet from "./SimpleCodeSnippet";

import { parseCode } from "./codeParser";

import { SnippetBulletExplanations } from "./explanations/SnippetBulletExplanations";

import "./Snippet.css";

const defaultNumberOfVisibleLines = 25;

const BULLETS_COMMENT_TYPE = "inline";
const REMOVE_COMMENT_TYPE = "remove";

const Snippet = (props) => {
  const tokensToUse = parseCodeWithCompatibility({ lang: props.lang, snippet: props.snippet, tokens: props.tokens });

  const renderBulletComments =
    props.commentsType === BULLETS_COMMENT_TYPE || (props.callouts && Object.keys(props.callouts).length > 0);

  const snippetComponent = renderBulletComments ? CodeSnippetWithCallouts : SimpleCodeSnippet;

  const lines = splitTokensIntoLines(tokensToUse);

  const modifiedLines = mergeOrRemoveComments();
  const comments = renderBulletComments ? buildCalloutsFromComments(modifiedLines) : {};
  const mergedCallouts = { ...comments, ...props.callouts };

  return (
    <>
      <SnippetContainer
        {...props}
        tokens={tokensToUse}
        linesOfCode={modifiedLines}
        scrollToLineIdx={scrollToLineIdx(props)}
        callouts={mergedCallouts}
        snippetComponent={snippetComponent}
      />
      <Explanations callouts={mergedCallouts} {...props} />
    </>
  );

  function mergeOrRemoveComments() {
    if (renderBulletComments) {
      return collapseCommentsAboveToMakeCommentOnTheCodeLine(lines);
    }

    if (props.commentsType === REMOVE_COMMENT_TYPE) {
      return removeCommentsFromEachLine(lines);
    }

    return lines;
  }
};

Snippet.defaultProps = {
  numberOfVisibleLines: defaultNumberOfVisibleLines,
};

function Explanations({ spoiler, isPresentation, callouts = {}, elementsLibrary }) {
  if (isPresentation || Object.keys(callouts).length === 0) {
    return null;
  }

  return <SnippetBulletExplanations spoiler={spoiler} callouts={callouts} elementsLibrary={elementsLibrary} />;
}

function scrollToLineIdx({ isPresentation, slideIdx, numberOfVisibleLines }) {
  if (!isPresentation || !numberOfVisibleLines) {
    return undefined;
  }

  return numberOfVisibleLines * slideIdx;
}

// TODO for backward compatibility with already built and deployed docs
// remove once TSI rebuilds all the docs
function parseCodeWithCompatibility({ lang, tokens, snippet }) {
  if (tokens) {
    return tokens;
  }

  return parseCode(lang, snippet);
}

function buildCalloutsFromComments(lines) {
  const result = {};
  lines.forEach((line, lineIdx) => {
    line.forEach((token) => {
      if (isCommentToken(token)) {
        result[lineIdx] = [{ type: "SimpleText", text: trimComment(token.content) }];
      }
    });
  });

  return result;
}

export { Snippet };
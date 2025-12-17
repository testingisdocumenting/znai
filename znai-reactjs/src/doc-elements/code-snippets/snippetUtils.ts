
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

import { isCommentToken } from "./codeUtils";
import { isAllAtOnce } from "../meta/meta";
import { convertToList } from "../propsUtils";
import { parseCode } from "./codeParser";
import { countNumberOfLines } from "../../utils/strings";

const defaultNumberOfVisibleLines = 25;
const BULLETS_COMMENT_TYPE = "inline";

// TODO for backward compatibility with already built and deployed docs
// remove once TSI rebuilds all the docs
function parseCodeWithCompatibility({ lang, tokens, snippet }: { lang: string; tokens?: any; snippet: string }) {
  if (tokens) {
    return tokens;
  }

  return parseCode(lang, snippet);
}

function inlinedCommentsNumberOfSlides({ meta, tokens }: { meta: any; tokens: any[] }) {
  const comments = tokens.filter((t) => isCommentToken(t));

  if (isAllAtOnce(meta) && comments.length > 0) {
    return 2; // two slides: 1st - no highlights; 2nd - all highlighted at once
  }

  return comments.length + 1;
}

function highlightNumberOfSlides({ meta, highlightAsList }: { meta: any; highlightAsList: any[] }) {
  if (isAllAtOnce(meta) && highlightAsList.length > 0) {
    return 1;
  }

  return highlightAsList.length;
}

export const presentationSnippetHandler = {
  component: null as any, // Will be set when imported
  numberOfSlides: ({
    meta,
    commentsType,
    lang,
    snippet,
    tokens,
    highlight,
    revealLineStop,
    numberOfVisibleLines = defaultNumberOfVisibleLines,
  }: {
    meta: any;
    commentsType?: string;
    lang: string;
    snippet: string;
    tokens?: any;
    highlight?: any;
    revealLineStop?: any[];
    numberOfVisibleLines?: number;
  }) => {
    const tokensToUse = parseCodeWithCompatibility({ lang, snippet, tokens });
    const highlightAsList = convertToList(highlight);

    if (commentsType === BULLETS_COMMENT_TYPE) {
      return inlinedCommentsNumberOfSlides({ meta, tokens: tokensToUse });
    }

    const numberOfStopLines = (revealLineStop || []).length;
    const numberOfScrolls = countNumberOfScrolls();

    const hasFirstNoActionSlide =
      highlightAsList.length > 0 ||
      numberOfStopLines > 0 ||
      (highlightAsList.length === 0 && numberOfStopLines === 0 && numberOfScrolls === 0);

    return (
      (hasFirstNoActionSlide ? 1 : 0) +
      highlightNumberOfSlides({ meta, highlightAsList }) +
      numberOfStopLines +
      numberOfScrolls
    );

    function countNumberOfScrolls() {
      const numberOfLines = countNumberOfLines(snippet);

      if (numberOfLines <= numberOfVisibleLines) {
        return 0;
      }

      return Math.ceil(numberOfLines / numberOfVisibleLines);
    }
  },
  slideInfoProvider: ({ meta, commentsType, lang, snippet, tokens, slideIdx }: {
    meta: any;
    commentsType?: string;
    lang: string;
    snippet: string;
    tokens?: any;
    slideIdx: number;
  }) => {
    const tokensToUse = parseCodeWithCompatibility({ lang, snippet, tokens });

    if (isAllAtOnce(meta)) {
      return {};
    }

    if (commentsType !== BULLETS_COMMENT_TYPE) {
      return {};
    }

    const comments = tokensToUse.filter((t: any) => isCommentToken(t));

    return {
      slideVisibleNote: !comments.length ? null : slideIdx === 0 ? "" : comments[slideIdx - 1].content,
    };
  },
};
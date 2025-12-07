/*
 * Copyright 2022 znai maintainers
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

import React, { useState, useEffect, useMemo } from "react";

import CliCommandToken from "./CliCommandToken";
import { splitParts } from "../../utils/strings";
import { DocElementPayload } from "../default-elements/DocElement";
import "./CliCommand.css";

interface Token {
  type: "command" | "param";
  value: string;
}

interface CliCommandProps {
  command: string;
  next?: DocElementPayload;
  prev?: DocElementPayload;
  paramsToHighlight?: string[];
  isPresentation?: boolean;
  isPresentationDisplayed?: boolean;
  threshold?: number;
  presentationThreshold?: number;
  splitAfter?: string[];
}

const CliCommand: React.FC<CliCommandProps> = ({
  command,
  next,
  prev,
  paramsToHighlight,
  isPresentation = false,
  isPresentationDisplayed = false,
  threshold = 100,
  presentationThreshold = 40,
  splitAfter = [],
}) => {
  const tokens = useMemo(() => tokenize(command), [command]);

  const [lastTokenIdx, setLastTokenIdx] = useState(
    isPresentation && !isPresentationDisplayed ? 1 : tokens.length
  );

  useEffect(() => {
    setLastTokenIdx(isPresentation && !isPresentationDisplayed ? 1 : tokens.length);
  }, [command, isPresentation, isPresentationDisplayed, tokens.length]);

  const revealNextToken = () => {
    setTimeout(() => {
      setLastTokenIdx((prev) => prev + 1);
    }, 80 + Math.random() * 50);
  };

  const isHighlighted = (token: Token): boolean => {
    return !!(
      paramsToHighlight &&
      paramsToHighlight.filter((p) => token.value.indexOf(p) !== -1).length
    );
  };

  const renderTokens = () => {
    const lines = splitParts({
      parts: tokens,
      valueFunc: (token: Token) => token.value,
      lengthFunc: (token: Token) => token.value.length,
      thresholdCharCount: isPresentation ? presentationThreshold : threshold,
      splitAfterList: splitAfter,
    });

    let tokenIdx = 0;

    return lines.map((line: Token[], lineIdx: number) => {
      const isLineVisible = lastTokenIdx > tokenIdx + line.length;
      const isLastLine = lineIdx === lines.length - 1;

      const renderedLine = line.map((token: Token) => {
        const isHidden = lastTokenIdx <= tokenIdx;
        const isLast = tokenIdx === tokens.length - 1;
        const isLastVisible = tokenIdx === lastTokenIdx - 1;
        const key = tokenIdx + (isHidden ? "-hidden" : "-visible");

        tokenIdx++;

        return (
          <CliCommandToken
            key={key}
            {...token}
            isHighlighted={isHighlighted(token)}
            isCursorVisible={
              isPresentation && ((lastTokenIdx > tokens.length && isLast) || isLastVisible)
            }
            isPresentation={isPresentation}
            isPresentationDisplayed={isPresentationDisplayed}
            isHidden={isHidden}
            onFullReveal={revealNextToken}
          />
        );
      });

      return (
        <div key={lineIdx} className="tokens-line">
          {[
            ...renderedLine,
            !isLastLine && isLineVisible ? (
              <span key="separator" className="line-separator">
                \
              </span>
            ) : null,
          ]}
        </div>
      );
    });
  };

  const isNextCliCommand = next && next.type === "CliCommand";
  const isPrevCliCommand = prev && prev.type === "CliCommand";

  const className =
    "cli-command content-block" +
    (isNextCliCommand ? " next-present" : "") +
    (isPrevCliCommand ? " prev-present" : "");

  return (
    <div key={command} className={className}>
      <pre>
        <span className="prompt">$ </span>
        <span key={command}>{renderTokens()}</span>
      </pre>
    </div>
  );
};

function tokenize(fullCommand: string): Token[] {
  const [command, ...params] = fullCommand.split(" ");
  return [
    { type: "command", value: command + " " },
    ...params.map((p) => ({
      type: "param" as const,
      value: p + " ",
    })),
  ];
}

const presentationCliCommandHandler = {
  component: CliCommand,
  numberOfSlides: () => 1,
};

export { CliCommand, presentationCliCommandHandler };

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

import React from "react";
import { useEffect, useRef } from "react";
import "./PreviewChangeScreen.css";

export interface OutputPart {
  type: "color" | "text";
  value: string;
}

export interface Line {
  type: "out" | "err";
  parts: OutputPart[];
}

interface Props {
  lines: Line[];
}

interface StyledParts {
  style: string;
  part: string;
}

function combineStylesAndText(parts: OutputPart[]) {
  const result: StyledParts[] = [];
  let currentStyle = "";
  for (const part of parts) {
    switch (part.type) {
      case "color":
        currentStyle = part.value.toLowerCase();
        break;
      case "text":
        result.push({ style: currentStyle, part: part.value });
        break;
    }
  }

  return result;
}

function LineContent({ styledParts }: { styledParts: StyledParts[] }) {
  return (
    <>
      {styledParts.map(({ style, part }) => {
        const className = "znai-preview-console-output-line-part" + (style.length > 0 ? " " + style : "");
        return <span className={className}>{part}</span>;
      })}
    </>
  );
}

function LineView({ type, parts }: Line) {
  const lineContent = <LineContent styledParts={combineStylesAndText(parts)} />;
  switch (type) {
    case "err":
      return <div className="znai-preview-console-output-line err">{lineContent}</div>;
    case "out":
      return <div className="znai-preview-console-output-line out">{lineContent}</div>;
  }
}

export function PreviewConsoleOutput({ lines }: Props) {
  const lastElementRef = useRef<HTMLDivElement>(null);
  useEffect(() => {
    lastElementRef?.current?.scrollIntoView({ behavior: "smooth" });
  }, [lines]);
  return (
    <pre className="znai-preview-console-output">
      {lines.map(LineView)}
      <div ref={lastElementRef} />
    </pre>
  );
}

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

import React, { CSSProperties } from "react";

import { useIsMobile } from "../../theme/ViewPortContext";

import { DocElementContent, ElementsLibraryMap } from "../default-elements/DocElement";
import "./Columns.css";
import { Property } from "csstype";

interface ColumnConfig {
  border?: boolean;
  align?: Property.TextAlign;
  portion?: number;
}

interface ColumnContent {
  content: DocElementContent;
}

interface Props {
  columns: ColumnContent[];
  config: {
    left?: ColumnConfig;
    right?: ColumnConfig;
    border?: boolean;
  };
  isPresentation: boolean;
  slideIdx: number;
  elementsLibrary: ElementsLibraryMap;
}

function Columns({ columns, config, isPresentation, slideIdx, ...props }: Props) {
  const isMobile = useIsMobile();

  const leftStyle = buildStyle(config.left);
  const rightStyle = buildStyle(config.right);

  const showRight = !isPresentation || slideIdx >= 1;
  const leftClassName = "column" + (config.border && showRight ? " border" : "");

  const leftContent = columns[0].content;
  const rightContent = columns[1].content;

  const left = (
    <div className={leftClassName} style={leftStyle}>
      <props.elementsLibrary.DocElement {...props} content={leftContent} />
    </div>
  );

  const right = showRight ? (
    <div className="column" style={rightStyle}>
      <props.elementsLibrary.DocElement {...props} content={rightContent} />
    </div>
  ) : null;

  const hasCodeSnippetAsLastElement = isLastElementCodeSnippet(leftContent) || isLastElementCodeSnippet(rightContent);

  const className =
    "columns content-block" + (isMobile ? " mobile" : "") + (hasCodeSnippetAsLastElement ? " snippet-at-bottom" : "");

  return (
    <div className={className}>
      {left}
      {right}
    </div>
  );
}

function isLastElementCodeSnippet(content: DocElementContent) {
  return content.length > 0 && content[content.length - 1].type === "Snippet";
}

const defaultColumnFlex = 10;
function buildStyle(columnConfig?: ColumnConfig) {
  const style: CSSProperties = {};

  if (!columnConfig) {
    return { flex: defaultColumnFlex };
  }

  style.flex = columnConfig.portion || defaultColumnFlex;

  if (columnConfig.align) {
    style.textAlign = columnConfig.align;
  }

  return style;
}

const presentationColumnsHandler = {
  component: Columns,
  numberOfSlides: ({ columns }: { columns: DocElementContent[] }) => columns.length,
};

export { Columns, presentationColumnsHandler };

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

import React, {type CSSProperties } from "react";

import { useIsMobile } from "../../theme/ViewPortContext";

import type {DocElementContent, ElementsLibraryMap} from "../default-elements/DocElement";
// @ts-ignore
import "./Columns.css";
import type {Property} from "csstype";

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
    middle?: ColumnConfig;
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
  const middleStyle = buildStyle(config.middle);
  const rightStyle = buildStyle(config.right);

  const styles = columns.length > 2 ? [leftStyle, middleStyle, rightStyle] : [leftStyle, rightStyle];
  const columnsToDisplay = isPresentation ? columns.slice(0, slideIdx + 1) : columns;

  const hasCodeSnippetAsLastElement = columnsToDisplay.some((column) => isLastElementCodeSnippet(column.content));

  const columnsClassName =
    "columns content-block" + (isMobile ? " mobile" : "") + (hasCodeSnippetAsLastElement ? " snippet-at-bottom" : "");

  return (
    <div className={columnsClassName}>
      {columnsToDisplay.map((column, idx) => {
        const isLastColumn = idx === columnsToDisplay.length - 1;
        const className = "column" + (config.border && !isLastColumn ? " border" : "");

        return (
          <div className={className} style={styles[idx]}>
            <props.elementsLibrary.DocElement {...props} content={column.content} />
          </div>
        );
      })}
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

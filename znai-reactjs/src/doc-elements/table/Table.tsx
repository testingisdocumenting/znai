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

import { CSSProperties } from "react";
import React from "react";

import { DocElementProps } from "../default-elements/DocElement";
import { calcTableWidth } from "./tableSize";

import { cssVarPixelValue } from "../../utils/cssVars";
import { ContainerTitleCommonProps, useIsUserDrivenCollapsed } from "../container/ContainerTitle";

import { ContainerCommonProps, Container } from "../container/Container";

import "./Table.css";

interface Column {
  title: string;
  align?: string;
  width?: string;
}

interface Table {
  styles: string[];
  columns: Column[];
  minColumnWidth?: number;
  wide?: boolean;
  data: any[][];
}

interface Props extends DocElementProps, ContainerTitleCommonProps, ContainerCommonProps {
  table: Table;
  highlightRowIndexes: number[];
}

interface RowProps {
  table: Table;
  row: any[];
  highlight: boolean;
  docElementProps: Omit<Props, "table" | "highlightRowIndexes">;
}

// defined outside of Table so React keeps the same component identity across Table re-renders,
// otherwise every re-render (e.g. preview mode re-renders on scroll) remounts each row's DOM
function Row({ table, row, highlight, docElementProps }: RowProps) {
  const className = "znai-table-row" + (highlight ? " znai-table-row-highlight" : "");
  return (
    <tr className={className}>
      {row.map((v, idx) => {
        const c = table.columns[idx];
        const style = buildColumnStyle(table, c);
        const value = Array.isArray(v) ? (
          <docElementProps.elementsLibrary.DocElement {...docElementProps} content={v} />
        ) : (
          v
        );

        return (
          <td key={idx} style={style}>
            {value}
          </td>
        );
      })}
    </tr>
  );
}

export function Table({ table, highlightRowIndexes, ...props }: Props) {
  const { userDrivenCollapsed, collapseToggle } = useIsUserDrivenCollapsed(props.collapsed);

  const tableStyles = table.styles || [];

  const showHeader = tableStyles.indexOf("no-header") === -1;

  // header related style will not trigger custom css
  const isCustomClassName = (tableStyles.length > 0 && showHeader) || (!showHeader && tableStyles.length > 1);

  const hasTitle = !!props.title;

  const wrapperClassName = "znai-table-wrapper";

  const tableClassName = (isCustomClassName ? tableStyles.join(" ") : "znai-table") + (hasTitle ? " with-title" : "");

  return (
    <Container
      className={wrapperClassName}
      wide={table.wide}
      title={props.title}
      anchorId={props.anchorId}
      collapsed={userDrivenCollapsed}
      onCollapseToggle={collapseToggle}
      additionalTitleContainerClassNames="znai-table-title-container"
      additionalTitleClassNames="znai-table-title"
      next={props.next}
      prev={props.prev}
      noGap={props.noGap}
    >
      {renderBody()}
    </Container>
  );

  function renderBody() {
    if (userDrivenCollapsed) {
      return null;
    }

    return (
      <div className="znai-table-inner-scroll-wrapper">
        <table className={tableClassName}>
          <thead>
            <tr>
              {showHeader
                ? table.columns.map((c, idx) => {
                    const style = buildColumnStyle(table, c);
                    return (
                      <th key={idx} style={style}>
                        {c.title}
                      </th>
                    );
                  })
                : null}
            </tr>
          </thead>
          <tbody>
            {table.data.map((r, idx) => (
              <Row key={idx} table={table} row={r} highlight={hasIdx(highlightRowIndexes, idx)} docElementProps={props} />
            ))}
          </tbody>
        </table>
      </div>
    );
  }
}

function hasIdx(indexes: number[] | undefined, idx: number): boolean {
  if (!indexes) {
    return false;
  }

  return indexes.indexOf(idx) !== -1;
}

function buildColumnStyle(table: Table, c: Column): CSSProperties {
  const textAlign = c.align ? c.align : "left";

  const fullWidth = cssVarPixelValue("znai-single-column-full-width");
  const calculatedWidth = c.width ? calcTableWidth(fullWidth, c.width) : undefined;

  const calculatedColumnsMinWidth = table.minColumnWidth ? calcTableWidth(fullWidth, table.minColumnWidth) : 0;
  const widthToUse =
    calculatedWidth || calculatedColumnsMinWidth
      ? Math.max(calculatedColumnsMinWidth, calculatedWidth || 0)
      : undefined;

  // @ts-ignore
  return { textAlign, width: widthToUse, minWidth: widthToUse };
}

export default Table;

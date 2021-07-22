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

import { DocElementProps } from "../default-elements/DocElement";
import { calcTableWidth } from "./tableSize";

import "./Table.css";
import { cssVarPixelValue } from "../../utils/cssVars";

interface Column {
  title: string;
  align?: string;
  width?: string;
}

interface Table {
  styles: string[];
  columns: Column[];
  minColumnWidth?: number;
  data: any[][];
}

interface Props extends DocElementProps {
  title?: string;
  table: Table;
}

export function Table({ table, ...props }: Props) {
  const tableStyles = table.styles || [];

  const Row = ({ row }: { row: any[] }) => {
    return (
      <tr>
        {row.map((v, idx) => {
          const c = table.columns[idx];
          const style = buildColumnStyle(table, c);
          const value = Array.isArray(v) ? (
            <props.elementsLibrary.DocElement {...props} content={v} />
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
  };

  const showHeader = tableStyles.indexOf("no-header") === -1;

  // header related style will not trigger custom css
  const isCustomClassName =
    (tableStyles.length > 0 && showHeader) ||
    (!showHeader && tableStyles.length > 1);

  const hasTitle = !!props.title;

  const tableClassName =
    (isCustomClassName ? tableStyles.join(" ") : "znai-table") +
    (hasTitle ? " with-title" : "");

  return (
    <div className="znai-table-wrapper content-block">
      <TableTitle title={props.title} />
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
            <Row key={idx} row={r} />
          ))}
        </tbody>
      </table>
    </div>
  );
}

function buildColumnStyle(table: Table, c: Column): CSSProperties {
  const textAlign = c.align ? c.align : "left";

  const fullWidth = cssVarPixelValue("znai-single-column-full-width");
  const calculatedWidth = c.width
    ? calcTableWidth(fullWidth, c.width)
    : undefined;

  const calculatedColumnsMinWidth = table.minColumnWidth
    ? calcTableWidth(fullWidth, table.minColumnWidth)
    : 0;
  const widthToUse =
    calculatedWidth || calculatedColumnsMinWidth
      ? Math.max(calculatedColumnsMinWidth, calculatedWidth || 0)
      : undefined;

  // @ts-ignore
  return { textAlign, width: widthToUse, minWidth: widthToUse };
}

function TableTitle({ title }: { title?: string }) {
  if (!title) {
    return null;
  }

  return <div className="znai-table-title">{title}</div>;
}

export default Table;

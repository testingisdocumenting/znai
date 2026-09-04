import { describe, it, expect, vi, beforeEach, afterEach } from "vitest";
import { render } from "@testing-library/react";
import React from "react";

import Table from "./Table";
import { DocElement } from "../default-elements/DocElement";
import { SimpleText } from "../default-elements/SimpleText";

const elementsLibrary: any = { DocElement, SimpleText };

function tableData() {
  return {
    styles: [],
    columns: [{ title: "Setting" }, { title: "Value" }],
    data: [
      [[{ text: "Office", type: "SimpleText" }], [{ text: "NYC", type: "SimpleText" }]],
      [[{ text: "Floor", type: "SimpleText" }], [{ text: "5", type: "SimpleText" }]],
    ],
  };
}

describe("Table", () => {
  beforeEach(() => {
    // buildColumnStyle reads --znai-single-column-full-width which is not defined in jsdom
    vi.spyOn(window, "getComputedStyle").mockReturnValue({
      getPropertyValue: () => "1000px",
    } as any);
  });

  afterEach(() => {
    vi.restoreAllMocks();
  });

  it("renders rows and cells content", () => {
    const { container } = render(<Table table={tableData()} elementsLibrary={elementsLibrary} />);

    const rows = [...container.querySelectorAll("tbody tr")];
    expect(rows.length).toBe(2);
    expect(rows[0].textContent).toBe("OfficeNYC");
    expect(rows[1].textContent).toBe("Floor5");
  });

  // guards against defining row component inside Table render: that gives the row a new
  // component identity every render and React remounts every <tr> DOM node, which makes
  // tables flicker when an ancestor re-renders on scroll (e.g. preview mode)
  it("keeps row DOM nodes in place when re-rendered", () => {
    const data = tableData();
    const { container, rerender } = render(<Table table={data} elementsLibrary={elementsLibrary} />);

    const rowsBefore = [...container.querySelectorAll("tbody tr")];
    expect(rowsBefore.length).toBe(2);

    rerender(<Table table={data} elementsLibrary={elementsLibrary} />);

    const rowsAfter = [...container.querySelectorAll("tbody tr")];
    expect(rowsAfter.length).toBe(2);
    rowsAfter.forEach((row, idx) => expect(row).toBe(rowsBefore[idx]));
  });
});

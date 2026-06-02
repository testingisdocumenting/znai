/*
 * Copyright 2026 znai maintainers
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

import { describe, it, expect, vi, beforeEach } from "vitest";
import { render, fireEvent } from "@testing-library/react";
import React from "react";
import { MermaidZoomed } from "./MermaidZoomed";
import { zoom } from "../zoom/Zoom";

vi.mock("mermaid", () => ({
  default: {
    initialize: vi.fn(),
    render: vi.fn(() => Promise.resolve({ svg: '<div data-testid="mocked-svg">Mocked SVG</div>' })),
    registerIconPacks: vi.fn(),
  },
}));

describe("MermaidZoomed", () => {
  beforeEach(() => {
    vi.clearAllMocks();
    Object.defineProperty(window, "znaiTheme", {
      value: {
        name: "znai-light",
        addChangeHandler: vi.fn(),
        removeChangeHandler: vi.fn(),
      },
      writable: true,
      configurable: true,
    });
  });

  it("renders the zoom & pan toolbar", () => {
    const { container } = render(<MermaidZoomed mermaid="graph TD; A-->B" />);

    expect(container.querySelector(".znai-mermaid-zoomed-toolbar")).toBeTruthy();
    expect(container.querySelectorAll(".znai-mermaid-zoomed-button")).toHaveLength(4);
    // the percent readout is rendered once the diagram is measured; its value depends on layout
    expect(container.querySelector(".znai-mermaid-zoomed-percent")).toBeTruthy();
  });

  it("closes the overlay when the close button is clicked", () => {
    const clearZoomSpy = vi.spyOn(zoom, "clearZoom");

    const { container } = render(<MermaidZoomed mermaid="graph TD; A-->B" />);
    fireEvent.click(container.querySelector(".znai-mermaid-zoomed-close") as HTMLElement);

    expect(clearZoomSpy).toHaveBeenCalled();
    clearZoomSpy.mockRestore();
  });
});

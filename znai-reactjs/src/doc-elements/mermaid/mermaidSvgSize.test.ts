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

import { describe, it, expect } from "vitest";
import { mermaidSvgNaturalSize, isMermaidSvgShrunk } from "./mermaidSvgSize";

// jsdom does not lay out svg, so the dimensions are supplied via a lightweight stand-in
function svgStub(opts: { viewBox?: { width: number; height: number }; maxWidth?: string; rect?: { width: number; height: number } }) {
  return {
    viewBox: { baseVal: { x: 0, y: 0, width: opts.viewBox?.width ?? 0, height: opts.viewBox?.height ?? 0 } },
    style: { maxWidth: opts.maxWidth ?? "" },
    getBoundingClientRect: () => ({ width: opts.rect?.width ?? 0, height: opts.rect?.height ?? 0 }),
  } as unknown as SVGSVGElement;
}

function containerWith(svg: SVGSVGElement | null) {
  return { querySelector: () => svg } as unknown as HTMLElement;
}

describe("mermaidSvgNaturalSize", () => {
  it("uses the viewBox when present", () => {
    expect(mermaidSvgNaturalSize(svgStub({ viewBox: { width: 1500, height: 800 } }))).toEqual({ width: 1500, height: 800 });
  });

  it("falls back to the max-width style and measured height when there is no viewBox", () => {
    expect(mermaidSvgNaturalSize(svgStub({ maxWidth: "640px", rect: { width: 400, height: 480 } }))).toEqual({
      width: 640,
      height: 480,
    });
  });
});

describe("isMermaidSvgShrunk", () => {
  it("is true when the diagram is rendered well below its natural width", () => {
    const svg = svgStub({ viewBox: { width: 1500, height: 800 }, rect: { width: 900, height: 480 } });
    expect(isMermaidSvgShrunk(containerWith(svg))).toBe(true);
  });

  it("is false when the diagram fits at its natural width", () => {
    const svg = svgStub({ viewBox: { width: 800, height: 400 }, rect: { width: 800, height: 400 } });
    expect(isMermaidSvgShrunk(containerWith(svg))).toBe(false);
  });

  it("is false when there is no container or svg yet", () => {
    expect(isMermaidSvgShrunk(null)).toBe(false);
    expect(isMermaidSvgShrunk(containerWith(null))).toBe(false);
  });
});

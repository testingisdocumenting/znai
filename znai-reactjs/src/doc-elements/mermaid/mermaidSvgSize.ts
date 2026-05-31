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

// once a diagram is rendered smaller than this fraction of its natural size it is worth zooming
const SHRUNK_THRESHOLD = 0.95;

export interface Size {
  width: number;
  height: number;
}

/**
 * natural (unscaled) size of a mermaid svg. Mermaid encodes it in the viewBox, falling back to
 * the max-width style / measured size for diagram types that don't set one.
 */
export function mermaidSvgNaturalSize(svg: SVGSVGElement): Size {
  const viewBox = svg.viewBox?.baseVal;
  if (viewBox && viewBox.width > 0 && viewBox.height > 0) {
    return { width: viewBox.width, height: viewBox.height };
  }

  const rect = svg.getBoundingClientRect();
  const maxWidth = parseFloat(svg.style.maxWidth);
  return {
    width: isNaN(maxWidth) ? rect.width : maxWidth,
    height: rect.height,
  };
}

/**
 * true when the diagram is being shrunk to fit the available width and is therefore hard to read
 * at its current size (the case where zoom & pan helps).
 */
export function isMermaidSvgShrunk(container: HTMLElement | null): boolean {
  if (!container) {
    return false;
  }

  const svg = container.querySelector("svg");
  if (!svg) {
    return false;
  }

  const natural = mermaidSvgNaturalSize(svg);
  const displayedWidth = svg.getBoundingClientRect().width;
  if (natural.width === 0 || displayedWidth === 0) {
    return false;
  }

  return displayedWidth / natural.width < SHRUNK_THRESHOLD;
}

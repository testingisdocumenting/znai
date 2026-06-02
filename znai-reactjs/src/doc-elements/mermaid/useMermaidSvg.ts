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

import { useEffect, useState } from "react";

import mermaid from "mermaid";
import { isZnaiDarkTheme, useZnaiThemeChange } from "../../theme/znaiTheme";

export interface MermaidIconPack {
  name: string;
  url: string;
}

let mermaidIdIdx = 0;
function generateNewMermaidId() {
  return "znai-mermaid-id-" + mermaidIdIdx++;
}

let isMermaidInitialized = false;
function initMermaidIfRequired() {
  if (isMermaidInitialized) {
    return;
  }

  window.znaiTheme.addChangeHandler(initializeMermaid);

  initializeMermaid();
  isMermaidInitialized = true;
}

function initializeMermaid() {
  mermaid.initialize({
    startOnLoad: false,
    securityLevel: "strict",
    theme: mermaidThemeName(),
  });
}

function mermaidThemeName() {
  return isZnaiDarkTheme() ? "dark" : "default";
}

/**
 * Renders mermaid source into an svg string, re-rendering on theme change. Each render uses a
 * fresh diagram id so the same source can be rendered multiple times on a page (e.g. inline and
 * inside the zoom overlay) without sharing internal marker/gradient ids.
 */
export function useMermaidSvg(mermaidSource: string, iconpacks?: MermaidIconPack[]): string {
  const [html, setHtml] = useState("");
  const [znaiThemeName, setZnaiThemeName] = useState(() => window.znaiTheme.name);

  useZnaiThemeChange((name) => {
    setZnaiThemeName(name);
  });

  useEffect(() => {
    initMermaidIfRequired();
  }, []);

  useEffect(() => {
    const id = generateNewMermaidId();
    initializeMermaid();

    // Register icon packs if provided, otherwise use default logos pack
    if (iconpacks && iconpacks.length > 0) {
      mermaid.registerIconPacks(
        iconpacks.map((pack) => ({
          name: pack.name,
          loader: () => fetch(pack.url).then((res) => res.json()),
        }))
      );
    }

    let cancelled = false;
    mermaid
      .render(id, mermaidSource)
      .then(({ svg }) => {
        if (!cancelled) {
          setHtml(svg);
        }
      })
      .catch((error) => {
        console.error("Error rendering mermaid diagram:", error);
      });

    return () => {
      cancelled = true;
    };
  }, [mermaidSource, iconpacks, znaiThemeName]);

  return html;
}

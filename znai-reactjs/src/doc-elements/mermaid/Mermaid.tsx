/*
 * Copyright 2021 znai maintainers
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

import React, { useRef, useState } from "react";

import mermaid from "mermaid";
import { isLocalUrl } from "../../structure/links";
import { documentationNavigation } from "../../structure/DocumentationNavigation";

import "./Mermaid.css";

interface Props {
  mermaid: string;
  wide?: boolean;
  iconpacks?: Array<{ name: string; url: string }>;
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

  // @ts-ignore
  window.znaiTheme.addChangeHandler(onThemeChange);

  initializeMermaid();
  isMermaidInitialized = true;

  function onThemeChange() {
    initializeMermaid();
  }

  function initializeMermaid() {
    mermaid.initialize({
      startOnLoad: false,
      securityLevel: "strict",
      // @ts-ignore
      theme: mermaidThemeName(),
    });
  }
}

export default function Mermaid(props: Props) {
  const [html, setHTML] = React.useState("");
  const [znaiThemeName, setZnaiThemeName] = useState(detectZnaiThemeName());

  React.useEffect(() => {
    // TODO theme integration via context
    // @ts-ignore
    window.znaiTheme.addChangeHandler(onThemeChange);

    initMermaidIfRequired();

    // @ts-ignore
    return () => window.znaiTheme.removeChangeHandler(onThemeChange);

    function onThemeChange() {
      setZnaiThemeName(detectZnaiThemeName());
    }
  }, []);

  React.useEffect(() => {
    const id = generateNewMermaidId();
    mermaid.initialize({
      startOnLoad: false,
      securityLevel: "strict",
      // @ts-ignore
      theme: mermaidThemeName(),
    });
    // Register icon packs if provided, otherwise use default logos pack
    if (props.iconpacks && props.iconpacks.length > 0) {
      mermaid.registerIconPacks(props.iconpacks.map(pack => ({
        name: pack.name,
        loader: () => fetch(pack.url).then(res => res.json())
      })));
    }

    mermaid.render(id, props.mermaid)
        .then(({ svg }) => {
          setHTML(svg);
        })
        .catch((error) => {
          console.error('Error rendering mermaid diagram:', error);
        });
  }, [props.mermaid, props.iconpacks, znaiThemeName]);

  const containerRef = useRef<HTMLDivElement>(null);

  React.useEffect(() => {
    const container = containerRef.current;
    if (!container) {
      return;
    }

    function handleClick(e: MouseEvent) {
      const target = e.target as HTMLElement;
      const anchor = target.closest("a");
      if (!anchor) {
        return;
      }

      const url = anchor.getAttribute("href") || anchor.getAttributeNS("http://www.w3.org/1999/xlink", "href");
      if (url && isLocalUrl(url)) {
        e.preventDefault();
        documentationNavigation.navigateToUrl(url);
      }
    }

    container.addEventListener("click", handleClick);
    return () => container.removeEventListener("click", handleClick);
  }, [html]);

  const className = "znai-mermaid " + (props.wide ? "wide" : "content-block");
  return <div ref={containerRef} className={className} dangerouslySetInnerHTML={{ __html: html }}></div>;
}

function mermaidThemeName() {
  // @ts-ignore
  return detectZnaiThemeName() === "znai-dark" ? "dark" : "default";
}

function detectZnaiThemeName(): string {
  // @ts-ignore
  return window.znaiTheme.name;
}
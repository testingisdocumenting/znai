/*
 * Copyright 2020 znai maintainers
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

import React, { useEffect, useRef, useState } from "react";
import { Container } from "../container/Container";

import "./Iframe.css";

interface Props {
  src: string;
  title?: string;
  aspectRatio?: string;
  light?: any;
  dark?: any;
  fit?: boolean;
  wide?: boolean;
  height?: number;
  maxHeight?: number;
  // changes on every page regen to force iframe reload
  previewMarker?: string;
}

export function Iframe(props: Props) {
  if (props.fit) {
    return <IframeFit {...props} />;
  } else {
    return <IframeVideo {...props} />;
  }
}

const initialIframeHeight = 14;

let activeElement: any = null;
export function IframeFit({ src, title, wide, height, maxHeight, light, dark, previewMarker }: Props) {
  const containerRef = useRef<HTMLDivElement>(null);
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const mutationObserverRef = useRef<MutationObserver | null>(null);
  const [visible, setVisible] = useState(false);
  const [calculatedIframeHeight, setCalculatedIframeHeight] = useState(initialIframeHeight);

  // iframe reload on mount
  useEffect(() => {
    if (!iframeRef) {
      return;
    }

    iframeRef!.current!.src += "";
  }, [previewMarker]);

  // handle site theme switching
  useEffect(() => {
    // TODO theme integration via context
    // @ts-ignore
    window.znaiTheme.addChangeHandler(onThemeChange);

    // @ts-ignore
    return () => window.znaiTheme.removeChangeHandler(onThemeChange);

    function onThemeChange() {
      injectCssProperties(iframeRef, dark, light);
      updateScrollBarToMatch(containerRef, iframeRef);
    }
  }, [dark, light]);

  useEffect(() => {
    return () => {
      if (mutationObserverRef.current) {
        mutationObserverRef.current.disconnect();
      }
    };
  }, []);

  // remembering what is a current active element
  // so when iframe mounts we can restore focus back
  if (document.activeElement?.tagName !== "IFRAME") {
    activeElement = document.activeElement;
  }

  return (
    <Container wide={wide} title={title}>
      <div ref={containerRef}></div>
      <iframe
        title={title}
        src={src}
        style={{ height: calculatedIframeHeight, minHeight: height, maxHeight }}
        width="100%"
        className={"znai-iframe fit" + (visible ? " visible" : "")}
        ref={iframeRef}
        onLoad={onLoad}
      />
    </Container>
  );

  function onLoad() {
    handleSize();
    updateScrollBarToMatch(containerRef, iframeRef);
    observeContentChanges();
  }

  function observeContentChanges() {
    if (mutationObserverRef.current) {
      mutationObserverRef.current.disconnect();
    }

    try {
      const iframeDocument = iframeRef!.current!.contentWindow!.document;
      const body = iframeDocument.body;
      if (!body) {
        return;
      }

      mutationObserverRef.current = new MutationObserver(() => {
        handleSize();
      });

      mutationObserverRef.current.observe(body, { childList: true, subtree: true });
    } catch (e) {
      // cross-origin iframes will throw, silently ignore
    }
  }

  function measureContentHeight() {
    const iframe = iframeRef!.current!;
    const htmlEl = iframe.contentWindow!.document.documentElement;

    // collapse iframe and set html to auto height to measure
    // natural content size without stretching to fill the container
    const prevIframeHeight = iframe.style.height;
    const prevHtmlHeight = htmlEl.style.height;
    iframe.style.height = "0px";
    htmlEl.style.height = "auto";
    const contentHeight = htmlEl.scrollHeight;
    htmlEl.style.height = prevHtmlHeight;
    iframe.style.height = prevIframeHeight;

    return contentHeight;
  }

  function handleSize() {
    setTimeout(() => {
      const newHeight = measureContentHeight();

      injectCssProperties(iframeRef, dark, light);
      setCalculatedIframeHeight(newHeight);
      setVisible(true);

      if (activeElement != null) {
        if (activeElement.tagName === "INPUT") {
          activeElement.focus();
        } else {
          window.focus();
        }
      }
    }, 0);
  }
}

function updateScrollBarToMatch(containerRef: any, iframeRef: any) {
  const div = containerRef!.current;

  function styleValue(varName: string) {
    return getComputedStyle(div!).getPropertyValue("--" + varName);
  }

  const widthKey = "znai-scrollbar-width";
  const heightKey = "znai-scrollbar-height";
  const bgKey = "znai-scrollbar-background-color";
  const fgKey = "znai-scrollbar-thumb-color";

  const scrollbarWidth = styleValue(widthKey);
  const scrollbarHeight = styleValue(heightKey);
  const scrollbarBg = styleValue(bgKey);
  const scrollbarFg = styleValue(fgKey);

  const iframeDocument = iframeRef!.current!.contentWindow!.document;
  const iframeEl = iframeDocument.documentElement;

  function setStyle(varName: string, value: string) {
    console.log("el", iframeEl, varName, value);
    iframeEl.style.setProperty("--" + varName, value);
  }

  setStyle(widthKey, scrollbarWidth);
  setStyle(heightKey, scrollbarHeight);
  setStyle(bgKey, scrollbarBg);
  setStyle(fgKey, scrollbarFg);

  const style = document.createElement("style");
  style.innerHTML = `
::-webkit-scrollbar {
    width: var(--znai-scrollbar-width);
    height: var(--znai-scrollbar-height);
}

::-webkit-scrollbar-thumb {
    background: var(--znai-scrollbar-thumb-color);
}

::-webkit-scrollbar-track {
    background: var(--znai-scrollbar-background-color);
}
    `;
  iframeDocument.head.appendChild(style);
}

function queryIsDarkTheme() {
  // @ts-ignore
  return window.znaiTheme.name === "znai-dark";
}

function injectCssProperties(iframeRef: any, dark: any, light: any) {
  const vars = queryIsDarkTheme() ? dark : light;

  if (!vars) {
    return;
  }

  const document = iframeRef!.current!.contentWindow!.document;
  const documentEl = document.documentElement;

  Object.keys(vars).forEach((k) => {
    documentEl.style.setProperty(k, vars[k]);
  });
}

export function IframeVideo({ src, title, aspectRatio = "16:9" }: Props) {
  return (
    <div className="content-block">
      <div style={{ position: "relative", paddingTop: calcAspectRatioPaddingTop(aspectRatio), height: 0 }}>
        <iframe
          title={title}
          src={src}
          allowFullScreen={true}
          style={{ position: "absolute", top: 0, left: 0, width: "100%", height: "100%" }}
        />
      </div>
    </div>
  );
}

export function calcAspectRatioPaddingTop(aspectRatio: string): string {
  const [width, height] = aspectRatio.split(":");
  if (!width || !height) {
    return "56.25%";
  }

  return ((Number(height) / Number(width)) * 100.0).toFixed(2) + "%";
}

export const presentationIframe = {
  component: Iframe,
  numberOfSlides: () => 1,
};

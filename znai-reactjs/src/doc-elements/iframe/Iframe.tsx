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
import { Container, ContainerCommonProps } from "../container/Container";
import { useIsUserDrivenCollapsed } from "../container/ContainerTitle";
import { Icon } from "../icons/Icon";
import { zoom } from "../zoom/Zoom";
import { isZnaiDarkTheme, useZnaiThemeChange } from "../../theme/znaiTheme";

import "./Iframe.css";

interface Props extends ContainerCommonProps {
  src: string;
  title?: string;
  collapsed?: boolean;
  aspectRatio?: string;
  light?: any;
  dark?: any;
  fit?: boolean;
  wide?: boolean;
  height?: number;
  maxHeight?: number;
  zoomEnabled?: boolean;
  newTabEnabled?: boolean;
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
// used when content height can't be measured (e.g. cross-origin iframe) and no explicit height is provided
const fallbackIframeHeight = 400;

function accessibleIframeDocument(iframeRef: React.RefObject<HTMLIFrameElement | null>): Document | null {
  try {
    return iframeRef.current?.contentWindow?.document ?? null;
  } catch (e) {
    // cross-origin iframes deny access to their document
    return null;
  }
}

let activeElement: any = null;
export function IframeFit({
  src,
  title,
  wide,
  height,
  maxHeight,
  light,
  dark,
  zoomEnabled,
  newTabEnabled,
  collapsed,
  noGap,
  noGapBorder,
  next,
  prev,
  previewMarker,
}: Props) {
  const containerRef = useRef<HTMLDivElement>(null);
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const mutationObserverRef = useRef<MutationObserver | null>(null);
  const [visible, setVisible] = useState(false);
  const [calculatedIframeHeight, setCalculatedIframeHeight] = useState(initialIframeHeight);
  const { userDrivenCollapsed, collapseToggle } = useIsUserDrivenCollapsed(collapsed);

  // iframe reload on mount
  useEffect(() => {
    if (!iframeRef.current) {
      return;
    }

    iframeRef.current.src += "";
  }, [previewMarker]);

  const { syncTheme } = useIframeThemeSync(iframeRef, dark, light, () => {
    updateScrollBarToMatch(containerRef, iframeRef);
  });

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

  const titleActions = iframeTitleActions({ src, title, light, dark, zoomEnabled, newTabEnabled });

  return (
    <Container
      wide={wide}
      title={title}
      additionalTitleClassNames="znai-iframe-title"
      titleActions={titleActions}
      collapsed={userDrivenCollapsed}
      onCollapseToggle={collapseToggle}
      noGap={noGap}
      noGapBorder={noGapBorder}
      next={next}
      prev={prev}
    >
      {!userDrivenCollapsed && (
        <>
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
        </>
      )}
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

    const iframeDocument = accessibleIframeDocument(iframeRef);
    const body = iframeDocument?.body;
    if (!body) {
      return;
    }

    mutationObserverRef.current = new MutationObserver(() => {
      handleSize();
    });

    mutationObserverRef.current.observe(body, { childList: true, subtree: true });
  }

  function measureContentHeight(): number | null {
    const iframeDocument = accessibleIframeDocument(iframeRef);
    if (!iframeDocument) {
      return null;
    }

    const iframe = iframeRef!.current!;
    const htmlEl = iframeDocument.documentElement;

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
      const measuredHeight = measureContentHeight();
      const newHeight = measuredHeight !== null ? measuredHeight : height || fallbackIframeHeight;

      syncTheme();
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

function useIframeThemeSync(iframeRef: React.RefObject<HTMLIFrameElement | null>, dark: any, light: any, onThemeChangeExtra?: () => void) {
  useZnaiThemeChange(() => {
    syncTheme();
    onThemeChangeExtra?.();
  });

  function syncTheme() {
    injectCssProperties(iframeRef, dark, light);
  }

  return { syncTheme };
}

function updateScrollBarToMatch(containerRef: any, iframeRef: any) {
  const iframeDocument = accessibleIframeDocument(iframeRef);
  if (!iframeDocument) {
    return;
  }

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

  const iframeEl = iframeDocument.documentElement;

  function setStyle(varName: string, value: string) {
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

function injectCssProperties(iframeRef: any, dark: any, light: any) {
  const vars = isZnaiDarkTheme() ? dark : light;

  if (!vars) {
    return;
  }

  const iframeDocument = accessibleIframeDocument(iframeRef);
  if (!iframeDocument) {
    return;
  }

  const documentEl = iframeDocument.documentElement;

  Object.keys(vars).forEach((k) => {
    documentEl.style.setProperty(k, vars[k]);
  });
}

function iframeTitleActions({
  src,
  title,
  light,
  dark,
  zoomEnabled,
  newTabEnabled,
}: Pick<Props, "src" | "title" | "light" | "dark" | "zoomEnabled" | "newTabEnabled">) {
  if (!zoomEnabled && !newTabEnabled) {
    return undefined;
  }

  return (
    <>
      {zoomEnabled && (
        <Icon
          id="maximize-2"
          onClick={() => zoom.zoom(<IframeZoomed src={src} title={title} light={light} dark={dark} />)}
        />
      )}
      {newTabEnabled && <Icon id="external-link" onClick={() => window.open(src, "_blank")} />}
    </>
  );
}

export function IframeVideo({
  src,
  title,
  aspectRatio = "16:9",
  wide,
  light,
  dark,
  zoomEnabled,
  newTabEnabled,
  collapsed,
  noGap,
  noGapBorder,
  next,
  prev,
}: Props) {
  const { userDrivenCollapsed, collapseToggle } = useIsUserDrivenCollapsed(collapsed);
  const titleActions = iframeTitleActions({ src, title, light, dark, zoomEnabled, newTabEnabled });

  return (
    <Container
      wide={wide}
      title={title}
      additionalTitleClassNames="znai-iframe-title"
      titleActions={titleActions}
      collapsed={userDrivenCollapsed}
      onCollapseToggle={collapseToggle}
      noGap={noGap}
      noGapBorder={noGapBorder}
      next={next}
      prev={prev}
    >
      {!userDrivenCollapsed && (
        <div style={{ position: "relative", paddingTop: calcAspectRatioPaddingTop(aspectRatio), height: 0 }}>
          <iframe
            title={title}
            src={src}
            allowFullScreen={true}
            style={{ position: "absolute", top: 0, left: 0, width: "100%", height: "100%" }}
          />
        </div>
      )}
    </Container>
  );
}

export function calcAspectRatioPaddingTop(aspectRatio: string): string {
  const [width, height] = aspectRatio.split(":");
  if (!width || !height) {
    return "56.25%";
  }

  return ((Number(height) / Number(width)) * 100.0).toFixed(2) + "%";
}

function IframeZoomed({ src, title, light, dark }: Pick<Props, "src" | "title" | "light" | "dark">) {
  const iframeRef = useRef<HTMLIFrameElement>(null);
  const { syncTheme } = useIframeThemeSync(iframeRef, dark, light);

  return (
    <div className="znai-iframe-zoomed" onClick={(e) => e.stopPropagation()}>
      <div className="znai-iframe-zoomed-header">
        <span className="znai-iframe-zoomed-title">{title}</span>
        <Icon id="x" className="znai-iframe-zoomed-close" onClick={() => zoom.clearZoom()} />
      </div>
      <iframe
        title={title}
        src={src}
        className="znai-iframe-zoomed-content"
        onLoad={syncTheme}
        ref={iframeRef}
      />
    </div>
  );
}

export const presentationIframe = {
  component: Iframe,
  numberOfSlides: () => 1,
};

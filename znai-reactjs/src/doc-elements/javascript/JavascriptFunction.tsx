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

import React, { CSSProperties, useEffect, useMemo, useRef } from "react";

import { Container } from "../container/Container";
import { isZnaiDarkTheme, useZnaiThemeChange, ZNAI_DARK_THEME_NAME } from "../../theme/znaiTheme";

import "./JavascriptFunction.css";

interface Props {
  functionName: string;
  args?: Record<string, unknown>;
}

type ThemeName = "light" | "dark";
type ThemeListener = (themeName: ThemeName) => void;

export interface ThemeObservable {
  readonly current: ThemeName;
  subscribe(listener: ThemeListener): void;
}

type JavascriptPluginFunction = (
  node: HTMLDivElement,
  args: Record<string, unknown>,
  theme: ThemeObservable
) => void | (() => void);

export function JavascriptFunction({ functionName, args }: Props) {
  const nodeRef = useRef<HTMLDivElement>(null);
  const listenersRef = useRef<ThemeListener[]>([]);

  const title = asString(args?.title);
  const anchorId = asString(args?.anchorId);
  const userClassName = asString(args?.className);
  const wide = args?.wide === true;
  const height = asHeight(args?.height);

  const userArgs = useMemo(() => {
    if (!args) return {};
    const { title: _t, wide: _w, className: _c, anchorId: _a, height: _h, ...rest } = args;
    return rest;
  }, [args]);

  const nodeStyle: CSSProperties | undefined =
    height !== undefined ? { height, overflow: "auto" } : undefined;

  useZnaiThemeChange((znaiThemeName) => {
    const publicName: ThemeName = znaiThemeName === ZNAI_DARK_THEME_NAME ? "dark" : "light";
    listenersRef.current.slice().forEach((l) => l(publicName));
  });

  useEffect(() => {
    const node = nodeRef.current;
    if (!node) return;

    const fn = lookupWindowFunction(functionName);
    if (!fn) {
      renderError(node, `javascript function "${functionName}" was not found on window`);
      return;
    }

    const listeners = listenersRef.current;
    const themeObservable: ThemeObservable = {
      get current() {
        return isZnaiDarkTheme() ? "dark" : "light";
      },
      subscribe(listener) {
        listeners.push(listener);
      },
    };

    let userCleanup: void | (() => void);
    try {
      userCleanup = fn(node, userArgs, themeObservable);
    } catch (e) {
      console.error(`error while calling javascript function "${functionName}"`, e);
      renderError(node, `error while calling javascript function "${functionName}": ${String(e)}`);
    }

    return () => {
      listeners.length = 0;
      if (typeof userCleanup === "function") {
        try {
          userCleanup();
        } catch (e) {
          console.error(`error while cleaning up javascript function "${functionName}"`, e);
        }
      }
      node.innerHTML = "";
    };
  }, [functionName, userArgs]);

  return (
    <Container
      wide={wide}
      title={title}
      anchorId={anchorId}
      className={userClassName ? `znai-javascript-function ${userClassName}` : "znai-javascript-function"}
      additionalTitleClassNames="znai-javascript-function-title"
    >
      <div ref={nodeRef} style={nodeStyle} onClick={(e) => e.stopPropagation()} />
    </Container>
  );
}

function asString(value: unknown): string | undefined {
  return typeof value === "string" ? value : undefined;
}

function asHeight(value: unknown): string | number | undefined {
  if (typeof value === "number") return value;
  if (typeof value === "string" && value.length > 0) return value;
  return undefined;
}

function lookupWindowFunction(functionName: string): JavascriptPluginFunction | undefined {
  // @ts-ignore
  const candidate = window[functionName];
  return typeof candidate === "function" ? (candidate as JavascriptPluginFunction) : undefined;
}

function renderError(node: HTMLDivElement, message: string) {
  node.innerHTML = "";
  const errorEl = document.createElement("div");
  errorEl.className = "znai-javascript-function-error";
  errorEl.textContent = message;
  node.appendChild(errorEl);
}

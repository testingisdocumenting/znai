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

import React, { useEffect, useMemo, useRef } from "react";

import { Container } from "../container/Container";

import "./JavascriptFunction.css";

interface Props {
  functionName: string;
  args?: Record<string, unknown>;
}

const CONTAINER_ARG_NAMES = new Set(["title", "wide", "className", "anchorId"]);

type ThemeName = "light" | "dark";
type ThemeListener = (themeName: ThemeName) => void;

export interface ThemeObservable {
  readonly current: ThemeName;
  subscribe(listener: ThemeListener): () => void;
}

function toPublicThemeName(znaiThemeName: string): ThemeName {
  return znaiThemeName === "znai-dark" ? "dark" : "light";
}

type JavascriptPluginFunction = (
  node: HTMLDivElement,
  args: Record<string, unknown>,
  theme: ThemeObservable
) => void | (() => void);

export function JavascriptFunction({ functionName, args }: Props) {
  const nodeRef = useRef<HTMLDivElement>(null);

  const title = typeof args?.title === "string" ? args.title : undefined;
  const wide = args?.wide === true;
  const anchorId = typeof args?.anchorId === "string" ? args.anchorId : undefined;
  const userClassName = typeof args?.className === "string" ? args.className : "";

  const userArgs = useMemo(() => extractUserArgs(args), [args]);

  useEffect(() => {
    const node = nodeRef.current;
    if (!node) {
      return;
    }

    const fn = lookupFunction(functionName);
    if (!fn) {
      renderError(node, `javascript function "${functionName}" was not found on window`);
      return;
    }

    const listeners: ThemeListener[] = [];
    const themeObservable: ThemeObservable = {
      get current() {
        // @ts-ignore
        return toPublicThemeName(window.znaiTheme.name);
      },
      subscribe(listener: ThemeListener) {
        listeners.push(listener);
        return () => {
          const idx = listeners.indexOf(listener);
          if (idx !== -1) {
            listeners.splice(idx, 1);
          }
        };
      },
    };

    function broadcastThemeChange(znaiThemeName: string) {
      const publicName = toPublicThemeName(znaiThemeName);
      listeners.slice().forEach((l) => l(publicName));
    }

    // @ts-ignore
    window.znaiTheme.addChangeHandler(broadcastThemeChange);

    let userCleanup: void | (() => void);
    try {
      userCleanup = fn(node, userArgs, themeObservable);
    } catch (e) {
      console.error(`error while calling javascript function "${functionName}"`, e);
      renderError(node, `error while calling javascript function "${functionName}": ${String(e)}`);
    }

    return () => {
      // @ts-ignore
      window.znaiTheme.removeChangeHandler(broadcastThemeChange);
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

  const containerClassName = `znai-javascript-function${userClassName ? ` ${userClassName}` : ""}`;

  return (
    <Container
      wide={wide}
      title={title}
      anchorId={anchorId}
      className={containerClassName}
      additionalTitleClassNames="znai-javascript-function-title"
    >
      <div ref={nodeRef} />
    </Container>
  );
}

function extractUserArgs(args: Record<string, unknown> | undefined): Record<string, unknown> {
  if (!args) {
    return {};
  }

  const result: Record<string, unknown> = {};
  for (const key of Object.keys(args)) {
    if (!CONTAINER_ARG_NAMES.has(key)) {
      result[key] = args[key];
    }
  }
  return result;
}

function lookupFunction(functionName: string): JavascriptPluginFunction | undefined {
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

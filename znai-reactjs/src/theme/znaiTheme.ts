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

import { useEffect, useRef } from "react";

export type ZnaiThemeChangeHandler = (themeName: string) => void;

export interface ZnaiTheme {
  readonly name: string;
  addChangeHandler(handler: ZnaiThemeChangeHandler): void;
  removeChangeHandler(handler: ZnaiThemeChangeHandler): void;
  set(name: string): void;
  setExplicitly(name: string): void;
  setExplicitlyIfNotSetAlready(name: string): void;
  toggle(): void;
}

declare global {
  interface Window {
    znaiTheme: ZnaiTheme;
  }
}

export const ZNAI_DARK_THEME_NAME = "znai-dark";

export function isZnaiDarkTheme(): boolean {
  return window.znaiTheme.name === ZNAI_DARK_THEME_NAME;
}

export function useZnaiThemeChange(handler: ZnaiThemeChangeHandler): void {
  const handlerRef = useRef(handler);
  handlerRef.current = handler;

  useEffect(() => {
    function onThemeChange(name: string) {
      handlerRef.current(name);
    }
    window.znaiTheme.addChangeHandler(onThemeChange);
    return () => window.znaiTheme.removeChangeHandler(onThemeChange);
  }, []);
}

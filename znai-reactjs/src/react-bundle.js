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
import React, {
    useState,
    useEffect,
    useContext,
    useReducer,
    useCallback,
    useMemo,
    useRef,
    useImperativeHandle,
    useLayoutEffect,
    useDebugValue,
    Fragment,
    StrictMode,
    createElement,
    createContext,
    forwardRef,
    memo,
    lazy,
    Suspense
} from 'react';

import ReactDOM from 'react-dom';

// Also attach to window for global access
if (typeof window !== 'undefined') {
    window.React = React;
    window.ReactDOM = ReactDOM;
}

// Export default
export default { React, ReactDOM };

// Explicitly re-export React named exports
export {
    React,
    ReactDOM,
    useState,
    useEffect,
    useContext,
    useReducer,
    useCallback,
    useMemo,
    useRef,
    useImperativeHandle,
    useLayoutEffect,
    useDebugValue,
    Fragment,
    StrictMode,
    createElement,
    createContext,
    forwardRef,
    memo,
    lazy,
    Suspense
};
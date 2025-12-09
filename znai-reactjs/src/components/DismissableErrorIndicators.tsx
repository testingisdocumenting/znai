/*
 * Copyright 2025 znai maintainers
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

import React, { useEffect, useState } from "react";

import "./DismissableErrorIndicators.css";

export interface ErrorIndicator {
  id: string;
  message: string;
}

type ErrorListener = (errors: Map<string, ErrorIndicator>) => void;

class ErrorNotifications {
  private listeners: ErrorListener[] = [];
  private errors: Map<string, ErrorIndicator> = new Map();
  private dismissedErrors: Set<string> = new Set();

  addListener(listener: ErrorListener) {
    this.listeners.push(listener);
  }

  removeListener(listener: ErrorListener) {
    this.listeners = this.listeners.filter((l) => l !== listener);
  }

  notifyError(error: ErrorIndicator) {
    if (!this.dismissedErrors.has(error.id)) {
      this.errors.set(error.id, error);
      this.notifyListeners();
    }
  }

  dismissError(id: string) {
    this.dismissedErrors.add(id);
    this.errors.delete(id);
    this.notifyListeners();
  }

  private notifyListeners() {
    this.listeners.forEach((listener) => listener(new Map(this.errors)));
  }
}

export const errorNotifications = new ErrorNotifications();

export function DismissableErrorIndicators() {
  const [errors, setErrors] = useState<Map<string, ErrorIndicator>>(new Map());

  useEffect(() => {
    const listener = (updatedErrors: Map<string, ErrorIndicator>) => {
      setErrors(updatedErrors);
    };

    errorNotifications.addListener(listener);
    return () => errorNotifications.removeListener(listener);
  }, []);

  const errorArray = Array.from(errors.values());

  if (errorArray.length === 0) {
    return null;
  }

  return (
    <div className="znai-dismissable-error-indicators">
      {errorArray.map((error) => (
        <div key={error.id} className="znai-error-indicator">
          <div className="znai-error-indicator-content">
            <span className="znai-error-indicator-message">{error.message}</span>
            <button
              className="znai-error-indicator-close"
              onClick={() => errorNotifications.dismissError(error.id)}
              aria-label="Dismiss error indicator"
            >
              ✕
            </button>
          </div>
        </div>
      ))}
    </div>
  );
}

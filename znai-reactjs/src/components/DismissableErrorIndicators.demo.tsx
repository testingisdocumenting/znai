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

import React from "react";

import { Registry } from "react-component-viewer";
import { DismissableErrorIndicators, errorNotifications } from "./DismissableErrorIndicators";

export function dismissableErrorIndicatorsDemo(registry: Registry) {
  registry.add("single error", () => {
    return (
      <div>
        <button
          onClick={() =>
            errorNotifications.notifyError({
              id: "demo-error-1",
              message: "Connection to server failed",
            })
          }
        >
          Notify Error
        </button>
        <DismissableErrorIndicators />
      </div>
    );
  });

  registry.add("multiple errors", () => {
    return (
      <div>
        <button
          onClick={() =>
            errorNotifications.notifyError({
              id: "demo-error-slack",
              message: "Slack conversations are offline",
            })
          }
        >
          Notify Slack Error
        </button>
        <button
          onClick={() =>
            errorNotifications.notifyError({
              id: "demo-error-tracking",
              message: "Activity tracking is offline",
            })
          }
        >
          Notify Tracking Error
        </button>
        <button
          onClick={() =>
            errorNotifications.notifyError({
              id: "demo-error-database",
              message: "Database connection lost",
            })
          }
        >
          Notify Database Error
        </button>
        <DismissableErrorIndicators />
      </div>
    );
  });

  registry.add("persistent dismissal", () => {
    let attempt = 1;

    return (
      <div>
        <p>Dismiss the error, then click the button again. The error will not reappear because it uses the same ID.</p>
        <button
          onClick={() => {
            errorNotifications.notifyError({
              id: "demo-persistent-error",
              message: `Connection failed (attempt ${attempt++})`,
            });
          }}
        >
          Retry Connection
        </button>
        <DismissableErrorIndicators />
      </div>
    );
  });

  registry.add("long message", () => {
    return (
      <div>
        <button
          onClick={() =>
            errorNotifications.notifyError({
              id: "demo-long-error",
              message:
                "Failed to connect to the remote server. Please check your network connection and try again. If the problem persists, contact support.",
            })
          }
        >
          Notify Long Error
        </button>
        <DismissableErrorIndicators />
      </div>
    );
  });
}

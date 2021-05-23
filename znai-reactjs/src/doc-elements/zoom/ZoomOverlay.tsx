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

import React, { useEffect, useState } from "react";

import "./ZoomOverlay.css";
import { zoom } from "./Zoom";

export function ZoomOverlay() {
  const [zoomedIn, setZoomedIn] = useState<JSX.Element | null>(null);

  useEffect(() => {
    zoom.addListener(zoomRendered);
    document.addEventListener("keydown", keysHandler);

    return () => {
      zoom.removeListener(zoomRendered);
      document.removeEventListener("keydown", keysHandler);
    };

    function zoomRendered(rendered: JSX.Element) {
      setZoomedIn(rendered);
    }

    function keysHandler(e: KeyboardEvent) {
      if (e.key === "Escape") {
        clearZoom();
      }
    }
  }, []);

  if (!zoomedIn) {
    return null;
  }

  return (
    <div className="znai-zoom-overlay" onClick={clearZoom}>
      <div className="znai-zoom-container">{zoomedIn}</div>
    </div>
  );

  function clearZoom() {
    setZoomedIn(null);
  }
}

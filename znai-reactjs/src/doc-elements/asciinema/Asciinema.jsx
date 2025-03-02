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

import React, { useEffect, useRef } from "react";
import * as AsciinemaPlayer from 'asciinema-player';
import "asciinema-player/dist/bundle/asciinema-player.css";
import "./Asciinema.css"

export function Asciinema({src}) {
  const containerRef = useRef(null);
  const playerRef = useRef(null);

  useEffect(() => {
    if (containerRef.current) {
      playerRef.current = AsciinemaPlayer.create(src, containerRef.current, {preload: true, fit: false});
    }

    return () => {
      if (playerRef.current && playerRef.current.destroy) {
        playerRef.current.destroy();
        playerRef.current = null;
      }
    };
  }, []);

  return <div className="content-block znai-asciinema" ref={containerRef} />;
}
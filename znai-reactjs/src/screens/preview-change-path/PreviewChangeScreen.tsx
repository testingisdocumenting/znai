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

import * as React from "react";
import { useEffect, useState } from "react";
import { socketUrl } from "../../utils/socket";
import { Line, PreviewConsoleOutput } from "./PreviewConsoleOutput";

interface Props {
  srcRoot: string;
  previewPageLink: string;
}

export function PreviewChangeScreen({ srcRoot, previewPageLink }: Props) {
  const [lines, setLines] = useState([]);

  useEffect(() => {
    const ws = new WebSocket(socketUrl("_preview-update"));

    ws.onopen = () => {
      ws.send(JSON.stringify({ srcRoot }));
    };

    ws.onclose = () => {};

    ws.onmessage = (message) => {
      const data = JSON.parse(message.data);
      if (data.type === "done") {
        window.location.replace("/preview/" + previewPageLink);
      } else {
        const line: Line = JSON.parse(message.data);
        //@ts-ignore
        setLines((prev) => [...prev, line]);
      }
    };

    return () => {
      ws.close();
    };
  }, []);

  return <PreviewConsoleOutput lines={lines} />;
}

/*
 * Copyright 2025 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

import React, { useRef, useState } from "react";
import { documentationTracking } from "../tracking/DocumentationTracking";
import { useHighlightOfHiddenElement } from "../text-selection/componentsHighlightUtils";
import "./Spoiler.css";

interface SpoilerProps {
  title: string;
  content: any;
  elementsLibrary: any;
}

export function Spoiler({ title, content, elementsLibrary }: SpoilerProps) {
  const [active, setActive] = useState(true);
  const containerRef = useRef<HTMLDivElement | null>(null);
  const hasHiddenHighlightedElement = useHighlightOfHiddenElement(containerRef, containerRef, !active);

  const DocElement = elementsLibrary.DocElement;

  const reveal = () => {
    setActive(false);
    documentationTracking.onInteraction("spoiler", title);
  };

  if (active) {
    // TODO function to augment class name with highlight?
    const titleClassName = "znai-spoiler-title" + (hasHiddenHighlightedElement ? " znai-highlight single" : "");
    return (
      <div className="znai-spoiler content-block" onClick={reveal} ref={containerRef}>
        <div className={titleClassName}>{title}</div>
        <div className="znai-spoiler-content">
          <DocElement content={content} elementsLibrary={elementsLibrary} />
        </div>
      </div>
    );
  }

  return <DocElement content={content} elementsLibrary={elementsLibrary} />;
}

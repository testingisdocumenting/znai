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

import React, { useState } from "react";

import { DocElementContent, ElementsLibraryMap } from "../default-elements/DocElement";
import { Icon } from "../icons/Icon";
import "./ReadMore.css";

interface Props {
  title: string;
  content: DocElementContent;
  elementsLibrary: ElementsLibraryMap;
}

export function ReadMore({ title, content, elementsLibrary }: Props) {
  const [expanded, setExpanded] = useState(false);
  const expandedClassName = expanded ? "expanded" : "collapsed";
  const topClassName = "znai-read-more content-block " + expandedClassName;
  const summaryClassName = "znai-read-more-title-block content-block " + expandedClassName;
  const summary = (
    <div className={summaryClassName} onClick={() => setExpanded((prev) => !prev)}>
      <Icon id="chevron-right" className="znai-read-more-icon" />
      <span className="znai-read-more-title">{title}</span>
    </div>
  );
  return (
    <div className={topClassName}>
      {expanded ? (
        <>
          {summary}
          <div className="znai-read-more-content content-block">
            <elementsLibrary.DocElement content={content} elementsLibrary={elementsLibrary} />
          </div>
        </>
      ) : (
        summary
      )}
    </div>
  );
}

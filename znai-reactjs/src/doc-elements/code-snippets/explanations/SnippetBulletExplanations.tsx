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

import React, { useState } from "react";

import { SnippetCircleBadge } from "./SnippetCircleBadge";

import { DocElementContent, ElementsLibraryMap } from "../../default-elements/DocElement";

interface Props {
  callouts: Record<number, DocElementContent>;
  elementsLibrary: ElementsLibraryMap;
  spoiler?: boolean;
}

export function SnippetBulletExplanations({ callouts, spoiler, elementsLibrary }: Props) {
  const [hidden, setHidden] = useState(spoiler);

  const className = "content-block code-bullets" + (hidden ? " hidden-explanation" : "");
  const spoilerMessage = hidden ? <div className="spoiler-message">Press to reveal</div> : null;

  return (
    <div className={className} onClick={onSpoilerClick}>
      {spoilerMessage}

      {Object.values(callouts).map((callout, idx) => (
        <Bullet key={idx} docElementContent={callout} idx={idx + 1} elementsLibrary={elementsLibrary} />
      ))}
    </div>
  );

  function onSpoilerClick() {
    setHidden(false);
  }
}

function Bullet({
  docElementContent,
  idx,
  elementsLibrary,
}: {
  docElementContent: DocElementContent;
  idx: number;
  elementsLibrary: ElementsLibraryMap;
}) {
  return (
    <div className="code-bullet-and-comment">
      <SnippetCircleBadge idx={idx} />
      <span className="code-bullet-comment">
        <elementsLibrary.DocElement content={[docElementContent]} elementsLibrary={elementsLibrary} />
      </span>
    </div>
  );
}

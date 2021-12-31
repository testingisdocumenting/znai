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

import { trimComment } from "../codeUtils";

interface Props {
  comments: { content: string }[];
  spoiler?: boolean;
}

export function SnippetBulletExplanations({ comments, spoiler }: Props) {
  const [hidden, setHidden] = useState(spoiler);

  const className = "content-block code-bullets" + (hidden ? " hidden-explanation" : "");
  const spoilerMessage = hidden ? <div className="spoiler-message">Press to reveal</div> : null;

  return (
    <div className={className} onClick={onSpoilerClick}>
      {spoilerMessage}

      {comments.map((t, idx) => (
        <Bullet key={idx} comment={t.content} idx={idx + 1} />
      ))}
    </div>
  );

  function onSpoilerClick() {
    setHidden(false);
  }
}

function Bullet({ comment, idx }: { comment: string; idx: number }) {
  return (
    <div className="code-bullet-and-comment">
      <SnippetCircleBadge idx={idx} />
      <span className="code-bullet-comment">{trimComment(comment)}</span>
    </div>
  );
}

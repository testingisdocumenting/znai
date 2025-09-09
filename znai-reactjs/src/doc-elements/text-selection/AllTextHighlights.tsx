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

import { HighlightUrlText } from "./HighlightUrlText";
import { SlackActiveQuestions } from "./SlackActiveQuestions";
import React, { useEffect, useState } from "react";
import { TocItem } from "../../structure/TocItem";

export function reapplyTextHighlights() {
  listeners.forEach((listener) => listener());
}

export type ReapplyTextHighlightListener = () => void;

const listeners: ReapplyTextHighlightListener[] = [];

export function addReapplyTextHighlightsListener(listener: ReapplyTextHighlightListener) {
  listeners.push(listener);
}

export function removeReapplyTextHighlightsListener(listener: ReapplyTextHighlightListener) {
  listeners.splice(listeners.indexOf(listener), 1);
}

export function AllTextHighlights({ containerNode, tocItem }: { containerNode: HTMLDivElement; tocItem: TocItem }) {
  const [keyToForceReHighlight, setKeyToForceReHighlight] = useState(0);
  // TODO move load questions from slack here

  useEffect(() => {
    function forceReapply() {
      console.log("@@ force");
      setKeyToForceReHighlight((prev) => prev + 1);
    }

    addReapplyTextHighlightsListener(forceReapply);
    return () => removeReapplyTextHighlightsListener(forceReapply);
  }, []);

  return (
    <React.Fragment key={keyToForceReHighlight}>
      <HighlightUrlText containerNode={containerNode} />
      <SlackActiveQuestions containerNode={containerNode} tocItem={tocItem} />
    </React.Fragment>
  );
}

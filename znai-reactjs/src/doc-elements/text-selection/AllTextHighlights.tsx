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

import { HighlightUrlText } from "./HighlightUrlText";
import { SlackActiveQuestions, SlackQuestion } from "./SlackActiveQuestions";
import { extractHighlightParams } from "./highlightUrl";

import { TocItem } from "../../structure/TocItem";
import { currentPageIdWithDocId, pageIdFromTocItem } from "../../structure/DocumentationNavigation";
import { getDocMeta } from "../../structure/docMeta";
import { fetchWithCredentials } from "../../utils/fetchWithCredentials";
import { errorNotifications } from "../../components/DismissableErrorIndicators";

const SLACK_ERROR_ID = "slack-connection-error";

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

export function AllTextHighlights({ containerNode, tocItem }: { containerNode: HTMLElement; tocItem: TocItem }) {
  const [keyToForceReHighlight, setKeyToForceReHighlight] = useState(0);
  const [activeQuestions, setActiveQuestions] = useState<SlackQuestion[]>([]);
  const pageId = pageIdFromTocItem(tocItem);

  useEffect(() => {
    function forceReapply() {
      setKeyToForceReHighlight((prev) => prev + 1);
    }

    addReapplyTextHighlightsListener(forceReapply);
    return () => removeReapplyTextHighlightsListener(forceReapply);
  }, []);

  useEffect(() => {
    void fetchActiveQuestions();
  }, [pageId]);

  async function fetchActiveQuestions() {
    try {
      const baseUrl = getDocMeta().slackActiveQuestionsUrl;
      if (!baseUrl) {
        return;
      }

      const url = `${baseUrl}?pageId=${encodeURIComponent(currentPageIdWithDocId())}`;
      const response = await fetchWithCredentials(url);

      if (response.ok) {
        const data = await response.json();
        setActiveQuestions(
          data.map((item: any) => ({
            id: item.id,
            selectedText: item.selectedText,
            selectedPrefix: item.selectedPrefix,
            selectedSuffix: item.selectedSuffix,
            question: item.question,
            context: item.context,
            slackLink: item.slackLink,
            slackMessageTs: item.slackMessageTs,
            queryParams: item.queryParams || {},
            resolved: item.resolved,
          }))
        );
      } else {
        handleFetchError(`Failed to fetch slack questions: ${response.status} ${response.statusText}`);
        setActiveQuestions([]);
      }
    } catch (err) {
      handleFetchError(`Failed to fetch slack questions: ${err}`);
    }
  }

  function handleFetchError(errorMessage: string) {
    console.error(errorMessage);
    errorNotifications.notifyError({
      id: SLACK_ERROR_ID,
      message: "Slack conversations are offline",
    });
  }

  function handleResolved(slackMessageTs: string) {
    setActiveQuestions((prev) => prev.filter((q) => q.slackMessageTs !== slackMessageTs));
  }

  const urlHighlight = extractHighlightParams();
  const matchedActive = urlHighlight
    ? activeQuestions.find(
        (q) =>
          q.selectedText === urlHighlight.selection &&
          q.selectedPrefix === urlHighlight.prefix &&
          q.selectedSuffix === urlHighlight.suffix
      )
    : undefined;

  return (
    <React.Fragment key={keyToForceReHighlight}>
      {!matchedActive && <HighlightUrlText containerNode={containerNode} />}
      <SlackActiveQuestions
        containerNode={containerNode}
        questions={activeQuestions}
        matchedQuestionTs={matchedActive?.slackMessageTs}
        onResolved={handleResolved}
      />
    </React.Fragment>
  );
}

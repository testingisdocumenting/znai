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
import { currentPageIdWithDocId, pageIdFromTocItem } from "../../structure/DocumentationNavigation";
import { getDocMeta } from "../../structure/docMeta";
import { Notification } from "../../components/Notification";
import { HighlightedText } from "./HighlightedText";
import { TocItem } from "../../structure/TocItem";
import { errorNotifications } from "../../components/DismissableErrorIndicators";
import { fetchWithCredentials } from "../../utils/fetchWithCredentials";

import { ResolveQuestionButton } from "./ResolveQuestionButton";
import { removeTrailingSlashFromQueryParam } from "./queryParamUtils";

import "./SlackActiveQuestions.css";

interface Question {
  id: string;
  selectedText: string;
  selectedPrefix: string;
  selectedSuffix: string;
  question: string;
  context: string;
  slackLink: string;
  slackMessageTs: string;
  resolved: boolean;
}

const SLACK_ERROR_ID = "slack-connection-error";

export function SlackActiveQuestions({ containerNode, tocItem }: { containerNode: HTMLElement; tocItem: TocItem }) {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [notification, setNotification] = useState<{ type: "success" | "error"; message: string } | null>(null);

  const params = new URLSearchParams(window.location.search);
  const questionId = params.get("questionId") || "";

  const pageId = pageIdFromTocItem(tocItem);

  useEffect(() => {
    void fetchActiveQuestions();
  }, [pageId]);

  async function fetchActiveQuestions() {
    try {
      const pageId = currentPageIdWithDocId();
      const baseUrl = getDocMeta().slackActiveQuestionsUrl;
      if (!baseUrl) {
        return;
      }

      const url = `${baseUrl}?pageId=${encodeURIComponent(pageId)}&questionId=${questionId}`;

      const response = await fetchWithCredentials(url);

      if (response.ok) {
        const data = await response.json();
        const questions = data.map((item: any) => ({
          selectedText: item.selectedText,
          selectedPrefix: item.selectedPrefix,
          selectedSuffix: item.selectedSuffix,
          question: item.question,
          context: item.context,
          slackLink: item.slackLink,
          slackMessageTs: item.slackMessageTs,
          id: item.id,
          resolved: item.resolved,
        }));
        setQuestions(questions);
      } else {
        handleFetchError(`Failed to fetch slack questions: ${response.status} ${response.statusText}`);
        setQuestions([]);
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

  async function resolveQuestionPost(question: Question) {
    try {
      const response = await fetchWithCredentials(
        getDocMeta().resolveSlackQuestionUrl! + "/" + question.slackMessageTs,
        {
          method: "POST",
        }
      );

      if (response.ok) {
        setNotification({ type: "success", message: "Resolved slack question" });
        setQuestions(questions.filter((q) => question.slackMessageTs !== q.slackMessageTs));
      } else {
        setNotification({ type: "error", message: `Failed to resolve question: ${response.statusText}` });
      }
    } catch (error) {
      setNotification({ type: "error", message: "Network error: Unable to connect to server" });
    }
  }

  const renderedQuestions = questions.map((question) => {
    function maybeResolveButton() {
      if (!getDocMeta().resolveSlackQuestionUrl) {
        return null;
      }

      return question.resolved ? (
        <div className="znai-highlight-bubble-resolve-wrapper">resolved</div>
      ) : (
        <div className="znai-highlight-bubble-resolve-wrapper">
          <ResolveQuestionButton onClick={() => resolveQuestionPost(question)} />
        </div>
      );
    }

    const additionalView = (
      <div className="znai-highlight-bubble-resolve-and-link">
        {maybeResolveButton()}
        <a className="znai-highlight-bubble-link" href={question.slackLink} target="_blank" rel="noopener noreferrer">
          open thread
        </a>
      </div>
    );

    const cleanedUpQuestionId = removeTrailingSlashFromQueryParam(questionId);

    return (
      <HighlightedText
        key={question.slackMessageTs}
        containerNode={containerNode}
        selectedText={question.selectedText}
        selectedPrefix={question.selectedPrefix}
        selectedSuffix={question.selectedSuffix}
        question={question.question}
        context={question.context}
        additionalView={additionalView}
        displayBubbleAndScrollIntoView={!!cleanedUpQuestionId && question.id === cleanedUpQuestionId}
      />
    );
  });

  return (
    <>
      {renderedQuestions}
      {notification && (
        <Notification type={notification.type} message={notification.message} onClose={() => setNotification(null)} />
      )}
    </>
  );
}

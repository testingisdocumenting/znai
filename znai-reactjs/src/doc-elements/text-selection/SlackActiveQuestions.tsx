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
import { currentPageId, pageIdFromTocItem } from "../../structure/DocumentationNavigation";
import { getDocMeta } from "../../structure/docMeta";
import { Notification } from "../../components/Notification";
import { HighlightedText } from "./HighlightedText";
import { TocItem } from "../../structure/TocItem";

import { ResolveQuestionButton } from "./ResolveQuestionButton";
import "./SlackActiveQuestions.css";
import { removeTrailingSlashFromQueryParam } from "./queryParamUtils";

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

export function SlackActiveQuestions({ containerNode, tocItem }: { containerNode: HTMLDivElement; tocItem: TocItem }) {
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
      const pageId = currentPageId();
      const baseUrl = getDocMeta().slackActiveQuestionsUrl;
      if (!baseUrl) {
        return;
      }

      const url = `${baseUrl}?pageId=${encodeURIComponent(pageId)}&questionId=${questionId}`;

      const response = await fetch(url, {
        method: "GET",
        credentials: "include",
      });

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
        setNotification({ type: "error", message: `Failed to fetch slack questions: ${response.statusText}` });
        setQuestions([]);
      }
    } catch (err) {
      setNotification({ type: "error", message: `Failed to fetch slack questions: ${err}` });
    }
  }

  async function resolveQuestionPost(question: Question) {
    try {
      const response = await fetch(getDocMeta().resolveSlackQuestionUrl! + "/" + question.slackMessageTs, {
        method: "POST",
        credentials: "include",
      });

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

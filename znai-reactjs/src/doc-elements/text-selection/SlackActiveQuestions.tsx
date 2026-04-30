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
import { getDocMeta } from "../../structure/docMeta";
import { Notification } from "../../components/Notification";
import { HighlightedText } from "./HighlightedText";
import { fetchWithCredentials } from "../../utils/fetchWithCredentials";

import { ResolveQuestionButton } from "./ResolveQuestionButton";

import "./SlackActiveQuestions.css";

export interface SlackQuestion {
  id: string;
  selectedText: string;
  selectedPrefix: string;
  selectedSuffix: string;
  question: string;
  context: string;
  slackLink: string;
  slackMessageTs: string;
  queryParams: Record<string, string>;
  resolved: boolean;
}

interface Props {
  containerNode: HTMLElement;
  questions: SlackQuestion[];
  matchedQuestionTs?: string;
  onResolved: (slackMessageTs: string) => void;
}

export function SlackActiveQuestions({ containerNode, questions, matchedQuestionTs, onResolved }: Props) {
  const [notification, setNotification] = useState<{ type: "success" | "error"; message: string } | null>(null);

  async function resolveQuestionPost(question: SlackQuestion) {
    try {
      const response = await fetchWithCredentials(
        getDocMeta().resolveSlackQuestionUrl! + "/" + question.slackMessageTs,
        {
          method: "POST",
        }
      );

      if (response.ok) {
        setNotification({ type: "success", message: "Resolved slack question" });
        onResolved(question.slackMessageTs);
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
        displayBubbleAndScrollIntoView={question.slackMessageTs === matchedQuestionTs}
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

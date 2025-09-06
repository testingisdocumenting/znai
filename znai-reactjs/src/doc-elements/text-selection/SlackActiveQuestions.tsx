import React, { useEffect, useState } from "react";
import { currentPageId, pageIdFromTocItem } from "../../structure/DocumentationNavigation";
import { getDocMeta } from "../../structure/docMeta";
import { Notification } from "../../components/Notification";
import { HighlightedText } from "./HighlightedText";
import { TocItem } from "../../structure/TocItem";

import { ResolveQuestionButton } from "./ResolveQuestionButton";
import "./SlackActiveQuestions.css";

interface Question {
  selectedText: string;
  prefix: string;
  suffix: string;
  question: string;
  slackLink: string;
  slackMessageTs: string;
}

export function SlackActiveQuestions({ containerNode, tocItem }: { containerNode: HTMLDivElement; tocItem: TocItem }) {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [notification, setNotification] = useState<{ type: "success" | "error"; message: string } | null>(null);

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

      const url = `${baseUrl}?pageId=${encodeURIComponent(pageId)}`;

      const response = await fetch(url, {
        method: "GET",
        credentials: "include",
      });

      if (response.ok) {
        const data = await response.json();
        const questions = data.map((item: any) => ({
          selectedText: item.selectedText,
          prefix: item.selectedPrefix,
          suffix: item.selectedSuffix,
          question: item.question,
          slackLink: item.slackLink,
          slackMessageTs: item.slackMessageTs,
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

      return (
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
        textSelection={question.selectedText}
        prefix={question.prefix}
        suffix={question.suffix}
        question={question.question}
        additionalView={additionalView}
        displayBubbleAndScrollIntoView={false}
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

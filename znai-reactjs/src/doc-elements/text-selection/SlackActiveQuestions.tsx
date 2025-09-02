import React, { useEffect, useState } from "react";
import { currentPageId } from "../../structure/DocumentationNavigation";
import { getDocMeta } from "../../structure/docMeta";
import { Notification } from "../../components/Notification";
import { HighlightedText } from "./HighlightedText";

interface Question {
  selectedText: string;
  prefix: string;
  suffix: string;
  question: string;
}

export function SlackActiveQuestions({ containerNode }: { containerNode: HTMLDivElement }) {
  const [questions, setQuestions] = useState<Question[]>([]);
  const [notification, setNotification] = useState<{ type: "success" | "error"; message: string } | null>(null);

  useEffect(() => {
    void fetchActiveQuestions();
  }, []);

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
        const questions = data.questions.map((item: any) => ({
          selectedText: item.selected_text,
          prefix: item.selected_prefix,
          suffix: item.selected_suffix,
          question: item.question,
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

  const renderedQuestions = questions.map((question, idx) => (
    <HighlightedText
      key={idx}
      containerNode={containerNode}
      textSelection={question.selectedText}
      prefix={question.prefix}
      suffix={question.suffix}
      question={question.question}
      displayBubbleAndScrollIntoView={false}
    />
  ));

  return (
    <>
      {renderedQuestions}
      {notification && (
        <Notification type={notification.type} message={notification.message} onClose={() => setNotification(null)} />
      )}
    </>
  );
}

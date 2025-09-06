import React, { useEffect, useRef } from "react";
import { TextHighlighter } from "./textHighlighter";

import "./HighlightedText.css";

interface Props {
  containerNode: HTMLDivElement;
  selectedText: string;
  selectedPrefix: string;
  selectedSuffix: string;
  question: string;
  displayBubbleAndScrollIntoView: boolean;
  additionalView: React.ReactNode;
}

export function HighlightedText({
  containerNode,
  selectedText,
  selectedPrefix,
  selectedSuffix,
  question,
  displayBubbleAndScrollIntoView,
  additionalView,
}: Props) {
  const bubbleRef = useRef<HTMLDivElement>(null);

  const bubbleText = question.endsWith("/") ? question.substring(0, question.length - 1) : question;

  // TODO re-calculate bubble position on window size change

  useEffect(() => {
    if (!bubbleRef.current) {
      return;
    }

    const bubble = bubbleRef.current as HTMLDivElement;

    function updateBubblePosition() {
      const containerRect = containerNode.getBoundingClientRect();
      const range = document.createRange();
      range.setStart(firstHighlightedElement, 0);
      range.setEnd(highlights[highlights.length - 1], highlights[highlights.length - 1].childNodes.length);
      const selectionRect = range.getBoundingClientRect();

      const bubbleRect = bubble.getBoundingClientRect();
      const top = selectionRect.top - containerRect.top + containerNode.scrollTop - bubbleRect.height - 10;
      const selectionCenter = selectionRect.left + selectionRect.width / 2.0;
      const left = selectionCenter - bubbleRect.width / 2.0 - containerRect.left;

      bubble.style.top = `${top}px`;
      bubble.style.left = `${left}px`;
    }

    function showBubbleAndCalcPositionIfHasQuestion() {
      if (!question) {
        return;
      }
      bubble.style.display = "block";
      updateBubblePosition();
    }

    function hideBubble() {
      bubble.style.display = "none";
    }

    function toggleBubble() {
      const bubble = bubbleRef.current as HTMLDivElement;
      if (bubble.style.display === "block") {
        hideBubble();
      } else {
        showBubbleAndCalcPositionIfHasQuestion();
      }
    }

    const highlighter = new TextHighlighter(containerNode);
    const highlights = highlighter.highlight(
      selectedText,
      selectedPrefix,
      selectedSuffix,
      question ? toggleBubble : null
    );
    const firstHighlightedElement = highlights[0];
    if (!firstHighlightedElement) {
      return;
    }

    if (question && bubbleRef.current && displayBubbleAndScrollIntoView) {
      showBubbleAndCalcPositionIfHasQuestion();
    }

    if (displayBubbleAndScrollIntoView) {
      setTimeout(() => {
        firstHighlightedElement.scrollIntoView({ behavior: "smooth", block: "center" });
      }, 100);
    }

    return () => {
      highlighter.clearHighlights();
      hideBubble();
    };
  }, []);

  return (
    <div ref={bubbleRef} className="znai-highlight-question-bubble">
      <div className="znai-highlight-question-bubble-text">{bubbleText}</div>
      {additionalView}
    </div>
  );
}

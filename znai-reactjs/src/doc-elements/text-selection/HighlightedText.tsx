import { useEffect, useRef } from "react";
import { TextHighlighter } from "./textHighlighter";

import "./HighlightedText.css";

interface Props {
  containerNode: HTMLDivElement;
  textSelection: string;
  prefix: string;
  suffix: string;
  question: string;
  displayBubbleAndScrollIntoView: boolean;
}

export function HighlightedText({
  containerNode,
  textSelection,
  prefix,
  suffix,
  question,
  displayBubbleAndScrollIntoView,
}: Props) {
  const bubbleRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (!bubbleRef.current) {
      return;
    }

    const bubble = bubbleRef.current as HTMLDivElement;

    function showBubbleIfHasQuestion() {
      if (!question) {
        return;
      }
      bubble.style.display = "block";
    }

    function hideBubble() {
      bubble.style.display = "none";
    }

    function toggleBubble() {
      const bubble = bubbleRef.current as HTMLDivElement;
      if (bubble.style.display === "block") {
        hideBubble();
      } else {
        showBubbleIfHasQuestion();
      }
    }

    const highlighter = new TextHighlighter(containerNode);
    const highlights = highlighter.highlight(textSelection, prefix, suffix, question ? toggleBubble : null);
    const firstHighlightedElement = highlights[0];
    if (!firstHighlightedElement) {
      return;
    }

    if (question && bubbleRef.current) {
      const containerRect = containerNode.getBoundingClientRect();

      if (displayBubbleAndScrollIntoView) {
        showBubbleIfHasQuestion();
      }

      const range = document.createRange();
      range.setStart(firstHighlightedElement, 0);
      range.setEnd(highlights[highlights.length - 1], highlights[highlights.length - 1].childNodes.length);
      const selectionRect = range.getBoundingClientRect();

      bubble.innerText = question.endsWith("/") ? question.substring(0, question.length - 1) : question;

      const bubbleRect = bubble.getBoundingClientRect();
      const top = selectionRect.top - containerRect.top + containerNode.scrollTop - bubbleRect.height - 10;
      const selectionCenter = selectionRect.left + selectionRect.width / 2.0;
      const left = selectionCenter - bubbleRect.width / 2.0 - containerRect.left;

      bubble.style.top = `${top}px`;
      bubble.style.left = `${left}px`;
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

  return <div ref={bubbleRef} className="znai-highlight-question-bubble" />;
}

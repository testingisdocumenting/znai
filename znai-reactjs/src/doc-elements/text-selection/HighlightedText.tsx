import React, { useEffect, useRef, useState } from "react";
import { TextHighlighter } from "./textHighlighter";

import { addTextMenuListener, removeTextMenuListener, TextMenuListener } from "./TextSelectionMenu";
import { createPortal } from "react-dom";

import { afterTitleId } from "../../layout/classNamesAndIds";

import "./HighlightedText.css";

interface Props {
  containerNode: HTMLDivElement;
  selectedText: string;
  selectedPrefix: string;
  selectedSuffix: string;
  question: string;
  context: string;
  displayBubbleAndScrollIntoView: boolean;
  additionalView: React.ReactNode;
}

export function HighlightedText({
  containerNode,
  selectedText,
  selectedPrefix,
  selectedSuffix,
  question,
  context,
  displayBubbleAndScrollIntoView,
  additionalView,
}: Props) {
  const cleanedUpQuestion = question.endsWith("/") ? question.substring(0, question.length - 1) : question;

  const bubbleRef = useRef<HTMLDivElement>(null);
  const detachedQuestionRef = useRef<HTMLDivElement>(null);
  const [bubbleContent, setBubbleContent] = useState(<>{cleanedUpQuestion}</>);

  let [maybeDetachedHighlightedText, setMaybeDetachedHighlightedText] = useState<any>(null);

  function updateBubblePosition(firstElement: any, lastElement: any) {
    // TODO re-calculate bubble position on window size change
    if (!bubbleRef.current) {
      return;
    }
    const bubble = bubbleRef.current as HTMLDivElement;

    const containerRect = containerNode.getBoundingClientRect();
    const range = document.createRange();
    range.setStart(firstElement, 0);
    range.setEnd(lastElement, lastElement.childNodes.length);
    const selectionRect = range.getBoundingClientRect();

    const bubbleRect = bubble.getBoundingClientRect();
    const top = selectionRect.top - containerRect.top + containerNode.scrollTop - bubbleRect.height - 10;
    const selectionCenter = selectionRect.left + selectionRect.width / 2.0;
    const left = selectionCenter - bubbleRect.width / 2.0 - containerRect.left;

    bubble.style.top = `${top}px`;
    bubble.style.left = `${left}px`;
  }

  function showBubbleAndCalcPositionIfHasQuestion(firstElement: any, lastElement: any) {
    if (!question || !bubbleRef.current) {
      return;
    }
    const bubble = bubbleRef.current as HTMLDivElement;
    bubble.style.display = "block";
    updateBubblePosition(firstElement, lastElement);
  }

  function hideBubble() {
    if (!bubbleRef.current) {
      return;
    }
    const bubble = bubbleRef.current as HTMLDivElement;
    bubble.style.display = "none";
  }

  function toggleBubble(firstElement: any, lastElement: any) {
    if (!bubbleRef.current) {
      return;
    }

    const bubble = bubbleRef.current as HTMLDivElement;
    if (bubble.style.display === "block") {
      hideBubble();
    } else {
      showBubbleAndCalcPositionIfHasQuestion(firstElement, lastElement);
    }
  }

  function scrollToBubbleIfRequired(elementToScrollTo: any) {
    if (displayBubbleAndScrollIntoView) {
      setTimeout(() => {
        elementToScrollTo.scrollIntoView({ behavior: "smooth", block: "center" });
      }, 100);
    }
  }

  useEffect(() => {
    if (!bubbleRef.current) {
      return;
    }

    const textMenuListener: TextMenuListener = {
      onHide() {},
      onShow() {
        hideBubble();
      },
    };

    const highlighter = new TextHighlighter(containerNode);
    const highlights = highlighter.highlight(
      selectedText,
      selectedPrefix,
      selectedSuffix,
      question ? () => toggleBubble(firstHighlightedElement, highlights[highlights.length - 1]) : null
    );
    const firstHighlightedElement = highlights[0];
    if (!firstHighlightedElement) {
      const afterTitlePlaceholder = document.getElementById(afterTitleId);
      if (afterTitlePlaceholder) {
        setMaybeDetachedHighlightedText(
          createPortal(
            <div
              className="znai-highlight-question-detached content-block"
              ref={detachedQuestionRef}
              onClick={() => {
                return question ? toggleBubble(detachedQuestionRef.current, detachedQuestionRef.current) : null;
              }}
            >
              <div className="znai-highlight-question-content">{cleanedUpQuestion}</div>
            </div>,
            afterTitlePlaceholder
          )
        );
      } else {
        console.warn("can't find element with id: " + afterTitleId);
      }
    }

    if (firstHighlightedElement) {
      if (question && bubbleRef.current && displayBubbleAndScrollIntoView) {
        showBubbleAndCalcPositionIfHasQuestion(firstHighlightedElement, highlights[highlights.length - 1]);
      }

      scrollToBubbleIfRequired(firstHighlightedElement);
    }

    addTextMenuListener(textMenuListener);

    return () => {
      highlighter.clearHighlights();
      hideBubble();
      removeTextMenuListener(textMenuListener);
    };
  }, []);

  useEffect(() => {
    if (detachedQuestionRef.current) {
      setBubbleContent(
        <div className="znai-highlight-detached-question-content">
          <div className="znai-highlight-detached-question-info">
            This question is no longer attached to the content. Available context:
          </div>
          <div className="znai-highlight-detached-question-context">{context}</div>
        </div>
      );
    }
  }, [maybeDetachedHighlightedText]);

  // attach bubble to a detached question
  useEffect(() => {
    if (!detachedQuestionRef.current || !question || !bubbleRef.current) {
      return;
    }

    const detachedQuestion = detachedQuestionRef.current as HTMLDivElement;
    if (displayBubbleAndScrollIntoView) {
      showBubbleAndCalcPositionIfHasQuestion(detachedQuestion, detachedQuestion);
      scrollToBubbleIfRequired(detachedQuestion);
    }
  }, [bubbleContent]);

  return (
    <>
      <div ref={bubbleRef} className="znai-highlight-question-bubble">
        <div className="znai-highlight-question-bubble-text">{bubbleContent}</div>
        {additionalView}
      </div>
      {maybeDetachedHighlightedText}
    </>
  );
}

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

import React, { useEffect, useRef, useState } from "react";
import { TextHighlighter } from "./textHighlihter";
import { findPrefixSuffixAndMatch } from "./textSelectionBuilder";

import { buildHighlightUrl } from "./highlightUrl";

import { getDocMeta } from "../../structure/docMeta";

import { buildContext } from "./markdownContextBuilder";
import "./TextSelectionMenu.css";

export function TextSelectionMenu({ containerNode }: { containerNode: HTMLDivElement }) {
  const menuRef = useRef<HTMLDivElement>(null);
  const sendToSlackPanelRef = useRef<HTMLDivElement>(null);
  const [expanded, setExpanded] = useState(false);
  const [question, setQuestion] = useState("");
  const [currentContext, setCurrentContext] = useState("");

  useEffect(() => {
    document.addEventListener("selectionchange", detectSelectionReset);
    document.addEventListener("mouseup", onMouseUp);

    return () => {
      document.removeEventListener("selectionchange", detectSelectionReset);
      document.removeEventListener("mouseup", onMouseUp);
    };
  }, [expanded]);

  return (
    <div
      ref={menuRef}
      className={`znai-text-selection-menu ${expanded ? "expanded" : ""}`}
      onClick={(e) => e.stopPropagation()}
    >
      <div
        className={`znai-text-selection-menu-item ${expanded ? "fading-out" : ""}`}
        onClick={!expanded ? handleAskInSlack : undefined}
        onMouseDown={preventDefault}
      >
        Ask in Slack
      </div>
      
      <div className={`znai-text-selection-panel-content ${expanded ? "fading-in" : ""}`} ref={sendToSlackPanelRef}>
        <div className="znai-text-selection-panel-preview">
          <div className="znai-text-selection-panel-preview-title">Context:</div>
          <pre className="znai-text-selection-panel-preview-content">{currentContext}</pre>
        </div>
        <div className="znai-text-selection-panel-input">
          <input
            type="text"
            placeholder="Enter your question..."
            value={question}
            onChange={(e) => setQuestion(e.target.value)}
            className="znai-text-selection-question-input"
            onClick={(e) => e.stopPropagation()}
            onFocus={(e) => e.stopPropagation()}
          />
          <button onClick={handleSend} className="znai-text-selection-send-button">
            Send to {getDocMeta().slackChannel || "Slack"}
          </button>
        </div>
      </div>
    </div>
  );

  function preventDefault(e: React.MouseEvent<HTMLDivElement>) {
    e.preventDefault();
  }

  function handleAskInSlack() {
    console.log("handleAskInSlack called");
    const context = buildContext();
    setCurrentContext(context);
    setExpanded(true);
    console.log("Set expanded to true");

    const result = findPrefixSuffixAndMatch(containerNode);
    const highlighter = new TextHighlighter(containerNode);
    highlighter.highlight(result.selection, result.prefix, result.suffix);
  }

  async function handleSend() {
    const result = findPrefixSuffixAndMatch(containerNode);
    const pageUrl = buildHighlightUrl(location.toString(), result);

    const body = {
      selectedText: result.selection,
      pageUrl: pageUrl,
      username: "web-user",
      slackChannel: getDocMeta().slackChannel,
      question: question,
      context: currentContext,
    };

    let response = await fetch("http://localhost:5111/ask-in-slack", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (response.ok) {
      console.log("Successfully sent to Slack");
      hidePopover();
    } else {
      console.error("Failed to send to Slack:", response.statusText);
    }
  }

  function showMenu(top: number, left: number) {
    if (!menuRef.current) {
      return;
    }

    const docMeta = getDocMeta();
    console.log("DocMeta for Slack:", { sendToSlackUrl: docMeta.sendToSlackUrl, slackChannel: docMeta.slackChannel });

    if (!docMeta.sendToSlackUrl || !docMeta.slackChannel) {
      console.log("Slack not configured, menu will not show");
      return;
    }

    const menu = menuRef.current;
    menu.style.top = `${top}px`;
    menu.style.left = `${left}px`;
    menuRef.current.style.display = "block";
  }

  function hidePopover() {
    console.log("hidePopover called, expanded:", expanded);
    if (menuRef.current) {
      menuRef.current.style.display = "none";
    }
    setExpanded(false);
    setQuestion("");
    setCurrentContext("");
  }

  function onMouseUp(event: MouseEvent) {
    if (expanded) {
      if (sendToSlackPanelRef.current && event.target && !sendToSlackPanelRef.current.contains(event.target as Node)) {
        hidePopover();
      }
      return;
    }

    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      hidePopover();
      return;
    }

    const range = selection.getRangeAt(0);

    const selectionRect = range.getBoundingClientRect();
    if (!containerNode.contains(range.startContainer)) {
      return;
    }

    const containerRect = containerNode.getBoundingClientRect();

    const top = selectionRect.top - containerRect.top + containerNode.scrollTop - 30;
    const selectionCenter = selectionRect.left + selectionRect.width / 2.0 - 72;
    const left = selectionCenter - containerRect.left;
    showMenu(top, left);
  }

  function detectSelectionReset() {
    if (expanded) {
      return; // Don't hide when panel is expanded
    }

    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      hidePopover();
      return;
    }
  }
}

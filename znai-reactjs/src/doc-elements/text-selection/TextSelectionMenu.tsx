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
import { findPrefixSuffixAndMatch } from "./textSelectionBuilder";

import { buildHighlightUrl } from "./highlightUrl";

import { getDocMeta } from "../../structure/docMeta";

import { buildContext } from "./markdownContextBuilder";
import { Notification } from "../../components/Notification";
import "./TextSelectionMenu.css";

export function TextSelectionMenu({ containerNode }: { containerNode: HTMLDivElement }) {
  const menuRef = useRef<HTMLDivElement>(null);
  const sendToSlackPanelRef = useRef<HTMLDivElement>(null);
  const inputRef = useRef<HTMLTextAreaElement>(null);
  const [panelData, setPanelData] = useState<{
    context: string;
    prefixSuffixMatch: ReturnType<typeof findPrefixSuffixAndMatch>;
  } | null>(null);
  const [hasText, setHasText] = useState(false);
  const [notification, setNotification] = useState<{ type: "success" | "error"; message: string } | null>(null);

  useEffect(() => {
    document.addEventListener("selectionchange", detectSelectionReset);
    document.addEventListener("mouseup", onMouseUp);
    document.addEventListener("keydown", handleKeyDown);

    return () => {
      document.removeEventListener("selectionchange", detectSelectionReset);
      document.removeEventListener("mouseup", onMouseUp);
      document.removeEventListener("keydown", handleKeyDown);
    };
  }, [panelData]);

  return (
    <>
      {notification && (
        <Notification type={notification.type} message={notification.message} onClose={() => setNotification(null)} />
      )}
      <div
        ref={menuRef}
        className={`znai-text-selection-menu ${panelData ? "expanded" : ""}`}
        onClick={(e) => e.stopPropagation()}
      >
        <div
          className={`znai-text-selection-menu-item ${panelData ? "fading-out" : ""}`}
          onClick={!panelData ? handleAskInSlack : undefined}
          onMouseDown={preventDefault}
        >
          Ask in Slack
        </div>

        <div className={`znai-text-selection-panel-content ${panelData ? "fading-in" : ""}`} ref={sendToSlackPanelRef}>
          <div className="znai-text-selection-panel-preview">
            <pre className="znai-text-selection-panel-preview-content">{panelData?.context || ""}</pre>
          </div>
          <div className="znai-text-selection-panel-input">
            <textarea
              ref={inputRef}
              placeholder="Enter your question..."
              className="znai-text-selection-question-input"
              rows={3}
              onKeyDown={handleTextareaKeyDown}
              onChange={handleTextChange}
            />
          </div>
          <div className="znai-text-selection-panel-footer">
            <button onClick={sendToSlack} className="znai-text-selection-send-button" disabled={!hasText}>
              Send to {getDocMeta().slackChannel || "Slack"}
            </button>
          </div>
        </div>
      </div>
    </>
  );

  function preventDefault(e: React.MouseEvent<HTMLDivElement>) {
    e.preventDefault();
  }

  function handleTextChange(e: React.ChangeEvent<HTMLTextAreaElement>) {
    setHasText(e.target.value.trim().length > 0);
  }

  function handleTextareaKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      if (hasText) {
        void sendToSlack();
      }
    }
  }

  function handleKeyDown(e: KeyboardEvent) {
    if (e.key === "Escape") {
      hidePopover();
    }
  }

  function handleAskInSlack() {
    const context = buildContext();
    const prefixSuffixMatch = findPrefixSuffixAndMatch(containerNode);
    setPanelData({ context, prefixSuffixMatch });
    inputRef.current?.focus();
  }

  async function sendToSlack() {
    const question = inputRef.current?.value?.trim();
    if (!question) {
      return;
    }

    const pageUrl = buildHighlightUrl(location.toString(), panelData!.prefixSuffixMatch, question);

    const body = {
      selectedText: panelData!.prefixSuffixMatch.selection,
      pageUrl: pageUrl,
      username: "web-user",
      slackChannel: getDocMeta().slackChannel,
      question: question,
      context: panelData!.context,
    };

    try {
      console.log(JSON.stringify(body));
      let response = await fetch("http://localhost:5111/ask-in-slack", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
      });

      if (response.ok) {
        setNotification({ type: "success", message: "Successfully sent to Slack!" });
        hidePopover();
      } else {
        setNotification({ type: "error", message: `Failed to send to Slack: ${response.statusText}` });
      }
    } catch (error) {
      setNotification({ type: "error", message: "Network error: Unable to connect to server" });
    }
  }

  function showMenu(top: number, left: number) {
    if (!menuRef.current) {
      return;
    }

    const docMeta = getDocMeta();

    if (!docMeta.sendToSlackUrl || !docMeta.slackChannel) {
      return;
    }

    const menu = menuRef.current;
    menu.style.top = `${top}px`;
    menu.style.left = `${left}px`;
    menuRef.current.style.display = "block";
  }

  function hidePopover() {
    if (menuRef.current) {
      menuRef.current.style.display = "none";
    }
    setPanelData(null);
    if (inputRef.current) {
      inputRef.current.value = "";
    }
    setHasText(false);
  }

  function onMouseUp(event: MouseEvent) {
    if (panelData) {
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
    if (panelData) {
      return;
    }

    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      hidePopover();
      return;
    }
  }
}

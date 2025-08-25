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
  const expandedPanelRef = useRef<HTMLDivElement>(null);
  const slackQuestionInputRef = useRef<HTMLTextAreaElement>(null);
  const linkCommentInputRef = useRef<HTMLTextAreaElement>(null);
  const [panelData, setPanelData] = useState<{
    type: "slack" | "linkgen";
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

  function menuItems() {
    if (panelData) {
      return null;
    }

    const menuItems = [{ label: "Generate Link", action: handleGenerateLink }];
    const docMeta = getDocMeta();

    if (docMeta.sendToSlackUrl && docMeta.slackChannel) {
      menuItems.push({ label: "Ask In Slack", action: handleAskInSlack });
    }

    const renderedMenuItems = menuItems.map((item) => {
      return (
        <div
          key={item.label}
          className="znai-text-selection-menu-item"
          onClick={item.action}
          onMouseDown={preventDefault}
        >
          {item.label}
        </div>
      );
    });

    return <div className="znai-text-selection-menu-item-list">{renderedMenuItems}</div>;
  }

  function expandedPanel() {
    if (!panelData) {
      return null;
    }

    if (panelData.type === "linkgen") {
      return renderGenLinkExpanded();
    }

    if (panelData.type === "slack") {
      return renderAskInSlackExpanded();
    }
  }

  const menuClassName = "znai-text-selection-menu" + (panelData ? " expanded " + panelData.type : "");
  return (
    <>
      {notification && (
        <Notification type={notification.type} message={notification.message} onClose={() => setNotification(null)} />
      )}
      <div ref={menuRef} className={menuClassName} onClick={(e) => e.stopPropagation()}>
        {menuItems()}
        {expandedPanel()}
      </div>
    </>
  );

  function renderGenLinkExpanded() {
    return (
      <div className={`znai-text-selection-panel-content ${panelData ? "fading-in" : ""}`} ref={expandedPanelRef}>
        <div className="znai-text-selection-panel-input">
          <textarea
            ref={linkCommentInputRef}
            autoFocus={true}
            placeholder="Selected text optional annotation"
            className="znai-text-selection-question-input"
            rows={3}
            onKeyDown={handleGenerateLinkCommentKeyDown}
            onChange={handleTextChange}
          />
        </div>
        <div className="znai-text-selection-panel-footer">
          <button onClick={generateLink} className="znai-text-selection-send-button">
            Generate Link
          </button>
        </div>
      </div>
    );
  }

  function renderAskInSlackExpanded() {
    return (
      <div className={`znai-text-selection-panel-content ${panelData ? "fading-in" : ""}`} ref={expandedPanelRef}>
        <div className="znai-text-selection-panel-preview">
          <pre className="znai-text-selection-panel-preview-content">{panelData?.context || ""}</pre>
        </div>
        <div className="znai-text-selection-panel-input">
          <textarea
            autoFocus={true}
            ref={slackQuestionInputRef}
            placeholder="Enter your question..."
            className="znai-text-selection-question-input"
            rows={3}
            onKeyDown={handleSlackQuestionKeyDown}
            onChange={handleTextChange}
          />
        </div>
        <div className="znai-text-selection-panel-footer">
          <button onClick={sendToSlack} className="znai-text-selection-send-button" disabled={!hasText}>
            Send to {getDocMeta().slackChannel || "Slack"}
          </button>
        </div>
      </div>
    );
  }

  function preventDefault(e: React.MouseEvent<HTMLDivElement>) {
    e.preventDefault();
  }

  function handleTextChange(e: React.ChangeEvent<HTMLTextAreaElement>) {
    setHasText(e.target.value.trim().length > 0);
  }

  function handleSlackQuestionKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      if (hasText) {
        void sendToSlack();
      }
    }
  }

  function handleGenerateLinkCommentKeyDown(e: React.KeyboardEvent<HTMLTextAreaElement>) {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      void generateLink();
    }
  }

  function handleKeyDown(e: KeyboardEvent) {
    if (e.key === "Escape") {
      hidePopover();
    }
  }

  function handleGenerateLink() {
    const prefixSuffixMatch = findPrefixSuffixAndMatch(containerNode);
    setPanelData({ type: "linkgen", context: "", prefixSuffixMatch });
  }

  function handleAskInSlack() {
    const context = buildContext();
    const prefixSuffixMatch = findPrefixSuffixAndMatch(containerNode);
    setPanelData({ type: "slack", context, prefixSuffixMatch });
  }

  async function generateLink() {
    const comment = linkCommentInputRef.current?.value?.trim();
    const pageUrl = buildHighlightUrl(location.toString(), panelData!.prefixSuffixMatch, comment);
    try {
      await navigator.clipboard.writeText(pageUrl);
      setNotification({ type: "success", message: "Link is generated and copied to clipboard" });
      hidePopover();
    } catch (err) {
      setNotification({ type: "error", message: `Failed to generate link: ${err}` });
    }
  }

  async function sendToSlack() {
    const question = slackQuestionInputRef.current?.value?.trim();
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
      const response = await fetch(getDocMeta().sendToSlackUrl!, {
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

    const menu = menuRef.current;
    menu.style.top = `${top}px`;
    menu.style.left = `${left}px`;
    menu.style.visibility = "visible";
  }

  function hidePopover() {
    if (menuRef.current) {
      menuRef.current.style.visibility = "hidden";
    }
    setPanelData(null);
    if (slackQuestionInputRef.current) {
      slackQuestionInputRef.current.value = "";
    }
    setHasText(false);
  }

  function onMouseUp(event: MouseEvent) {
    if (panelData) {
      if (expandedPanelRef.current && event.target && !expandedPanelRef.current.contains(event.target as Node)) {
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

    const menuWidth = menuRef.current ? menuRef.current.getBoundingClientRect().width : 0;

    const top = selectionRect.top - containerRect.top + containerNode.scrollTop - 40;
    const selectionCenter = selectionRect.left + selectionRect.width / 2.0 - menuWidth / 2.0;
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

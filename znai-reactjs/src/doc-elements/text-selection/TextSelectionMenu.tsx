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

import React, { useEffect, useRef } from "react";
import { TextHighlighter } from "./textHighlihter";
import { findPrefixSuffixAndMatch } from "./textSelectionBuilder";

import { buildHighlightUrl } from "./highlightUrl";

import { getDocMeta } from "../../structure/docMeta";

import "./TextSelectionMenu.css";
import { buildContext } from "./markdownContextBuilder";

export function TextSelectionMenu({ containerNode }: { containerNode: HTMLDivElement }) {
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    document.addEventListener("selectionchange", detectSelectionReset);
    document.addEventListener("mouseup", onMouseUp);
    hidePopover();
    return () => {
      document.removeEventListener("selectionchange", detectSelectionReset);
      document.removeEventListener("mouseup", onMouseUp);
    };
  }, []);

  return (
    <div ref={menuRef} className="znai-text-selection-menu">
      <div className="znai-text-selection-menu-item" onClick={askInSlack} onMouseDown={preventDefault}>
        Ask in Slack
      </div>
    </div>
  );

  function preventDefault(e: React.MouseEvent<HTMLDivElement>) {
    e.preventDefault();
  }

  async function askInSlack() {
    const selection = getSelection();
    if (!selection || selection.rangeCount === 0 || selection.isCollapsed) {
      return;
    }

    const markdownContext = buildContext();
    console.log("-------------");
    console.log(markdownContext);
    console.log("-------------");

    const result = findPrefixSuffixAndMatch(containerNode);
    const highlighter = new TextHighlighter(containerNode);
    highlighter.highlight(result.selection, result.prefix, result.suffix);

    selection.removeAllRanges();

    const pageUrl = buildHighlightUrl(location.toString(), result);

    hidePopover();

    const body = {
      selectedText: result.selection,
      pageUrl: pageUrl,
      username: "web-user",
      slackChannel: getDocMeta().slackChannel,
    };

    let response = await fetch("http://localhost:5111/ask-in-slack", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    // TODO notification of sent or failed

    if (response.ok) {
      console.log("Successfully sent to Slack");
    } else {
      console.error("Failed to send to Slack:", response.statusText);
    }
  }

  function showMenu(top: number, left: number) {
    if (!menuRef.current) {
      return;
    }

    if (!getDocMeta().sendToSlackUrl || !getDocMeta().slackChannel) {
      return;
    }

    getDocMeta();

    const menu = menuRef.current;
    menu.style.top = `${top}px`;
    menu.style.left = `${left}px`;
    menuRef.current.style.display = "block";
  }

  function hidePopover() {
    if (menuRef.current) {
      menuRef.current.style.display = "none";
    }
  }

  function onMouseUp() {
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

    const top = selectionRect.top - containerRect.top + containerNode.scrollTop - 48;
    const selectionCenter = selectionRect.left + selectionRect.width / 2.0 - 72;
    const left = selectionCenter - containerRect.left;
    showMenu(top, left);
  }

  function detectSelectionReset() {
    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      hidePopover();
      return;
    }
  }
}

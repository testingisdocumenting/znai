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
import { generateFragment } from "text-fragments-polyfill/dist/fragment-generation-utils.js";
import "./TextSelectionMenu.css";

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

    try {
      const selectedText = selection.toString();
      const range = selection.getRangeAt(0);
      
      let pageUrl = `${window.location.origin}${window.location.pathname}${window.location.search}`;
      let result;

      // Try to generate fragment with the original selection
      try {
        result = generateFragment(selection);
      } catch (error) {
        console.error("Fragment generation error:", error);
      }

      // Build URL based on result
      if (result && result.status === 0 && result.fragment) {
        const fragment = result.fragment;
        const prefix = fragment.prefix ? `${encodeURIComponent(fragment.prefix)}-,` : '';
        const suffix = fragment.suffix ? `,-${encodeURIComponent(fragment.suffix)}` : '';
        const start = encodeURIComponent(fragment.textStart);
        const end = fragment.textEnd ? `,${encodeURIComponent(fragment.textEnd)}` : '';
        pageUrl += `#:~:text=${prefix}${start}${end}${suffix}`;
      } else {
        // For partial word selections or when fragment generation fails,
        // expand to word boundaries and use that
        const expandedRange = range.cloneRange();
        expandWordSelection(expandedRange);
        const expandedText = expandedRange.toString();
        
        // If we expanded the selection, use the expanded text
        if (expandedText !== selectedText && expandedText.length > 0) {
          pageUrl += `#:~:text=${encodeURIComponent(expandedText)}`;
        } else {
          // Use the original selected text
          pageUrl += `#:~:text=${encodeURIComponent(selectedText)}`;
        }
      }

      const formData = new FormData();
      formData.append("selectedText", selectedText);
      formData.append("pageUrl", pageUrl);
      formData.append("username", "web-user");

      let response = await fetch("http://localhost:5111/ask-in-slack", {
        method: "POST",
        body: formData,
      });

      hidePopover();

      if (response.ok) {
        console.log("Successfully sent to Slack");
      } else {
        console.error("Failed to send to Slack:", response.statusText);
      }
    } catch (error) {
      console.error("Error sending to Slack:", error);
    }
  }

  function expandWordSelection(range: Range) {
    const startContainer = range.startContainer;
    const endContainer = range.endContainer;
    
    if (startContainer.nodeType === Node.TEXT_NODE) {
      const textContent = startContainer.textContent || "";
      let startOffset = range.startOffset;
      
      // Expand backwards to word boundary
      while (startOffset > 0 && /\w/.test(textContent[startOffset - 1])) {
        startOffset--;
      }
      range.setStart(startContainer, startOffset);
    }
    
    if (endContainer.nodeType === Node.TEXT_NODE) {
      const textContent = endContainer.textContent || "";
      let endOffset = range.endOffset;
      
      // Expand forwards to word boundary
      while (endOffset < textContent.length && /\w/.test(textContent[endOffset])) {
        endOffset++;
      }
      range.setEnd(endContainer, endOffset);
    }
  }

  function showMenu(top: number, left: number) {
    if (!menuRef.current) {
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
  }

  function onMouseUp(e: MouseEvent) {
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

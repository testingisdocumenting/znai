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
import { toPng } from "html-to-image";
import "./TextSelectionMenu.css";

export function TextSelectionMenu({ containerNode }: { containerNode: HTMLDivElement }) {
  const menuRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    document.addEventListener("selectionchange", detectSelectionReset);
    document.addEventListener("mouseup", onMouseUp);
    return () => {
      document.removeEventListener("selectionchange", detectSelectionReset);
      document.removeEventListener("mouseup", onMouseUp);
    };
  }, []);

  return (
    <div ref={menuRef} className="znai-text-selection-menu">
      <div className="znai-text-selection-menu-item" onClick={clickMenu} onMouseDown={preventDefault}>
        Ask in Slack
      </div>
    </div>
  );

  function preventDefault(e: React.MouseEvent<HTMLDivElement>) {
    e.preventDefault();
  }

  async function clickMenu() {
    const selection = getSelection();
    if (!selection || selection.rangeCount === 0 || selection.isCollapsed) {
      return;
    }

    try {
      const selectedText = selection.toString();
      const range = selection.getRangeAt(0);

      // Get context before and after the selection for unique text fragment
      const textContent = range.commonAncestorContainer.textContent || "";
      const fullText = textContent;
      const selectedStart = textContent.indexOf(selectedText);
      
      // Get prefix and suffix for context
      const prefixStart = Math.max(0, selectedStart - 20);
      const prefixText = textContent.substring(prefixStart, selectedStart).trim();
      const suffixEnd = Math.min(textContent.length, selectedStart + selectedText.length + 20);
      const suffixText = textContent.substring(selectedStart + selectedText.length, suffixEnd).trim();

      const contextElement = getContextElement(range);

      const imageDataUrl = await toPng(contextElement, {
        backgroundColor: "white",
        quality: 0.8,
        pixelRatio: 1,
      });

      // Debug: Log the image data URL
      console.log("Generated image data URL:", imageDataUrl);
      console.log("Context element:", contextElement);
      console.log("Context element dimensions:", {
        width: contextElement.offsetWidth,
        height: contextElement.offsetHeight,
        className: contextElement.className,
      });

      // Debug: Create a link to view the image
      const debugLink = document.createElement("a");
      debugLink.href = imageDataUrl;
      debugLink.download = "debug-screenshot.png";
      debugLink.textContent = "View generated image";
      console.log("Click to view image:", debugLink);

      // Debug: Open image in new tab
      const debugWindow = window.open("", "_blank");
      if (debugWindow) {
        debugWindow.document.write(`<img src="${imageDataUrl}" style="max-width: 100%;" />`);
        debugWindow.document.title = "Context Screenshot Debug";
      }

      const imageBlob = await (await fetch(imageDataUrl)).blob();
      console.log("Image blob size:", imageBlob.size, "bytes");

      const formData = new FormData();
      formData.append("selectedText", selectedText);
      
      // Create text fragment with prefix and suffix for uniqueness
      let textFragment = "";
      if (prefixText && suffixText) {
        textFragment = `${encodeURIComponent(prefixText)}-,${encodeURIComponent(selectedText)},-${encodeURIComponent(suffixText)}`;
      } else if (prefixText) {
        textFragment = `${encodeURIComponent(prefixText)}-,${encodeURIComponent(selectedText)}`;
      } else if (suffixText) {
        textFragment = `${encodeURIComponent(selectedText)},-${encodeURIComponent(suffixText)}`;
      } else {
        textFragment = encodeURIComponent(selectedText);
      }
      
      formData.append("pageUrl", `${window.location.href}#:~:text=${textFragment}`);
      formData.append("username", "web-user");

      // Try to send with image first
      formData.append("image", imageBlob, "context-screenshot.png");

      let response = await fetch("http://localhost:5111/ask-in-slack-with-image", {
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

  function getContextElement(range: Range): HTMLElement {
    let element = range.commonAncestorContainer;

    if (element.nodeType === Node.TEXT_NODE) {
      element = element.parentNode!;
    }

    let contextElement = element as HTMLElement;
    while (contextElement && !contextElement.classList.contains("znai-documentation-page")) {
      if (
        contextElement.tagName === "SECTION" ||
        (contextElement.tagName === "DIV" && contextElement.className.includes("content"))
      ) {
        break;
      }
      contextElement = contextElement.parentElement!;
    }

    return contextElement || containerNode;
  }
}

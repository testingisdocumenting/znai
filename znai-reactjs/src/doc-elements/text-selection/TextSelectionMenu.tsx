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

      let contextElement = getContextElement(range);
      
      // If context element is too small (height <= 50px), expand context to include surrounding elements
      if (contextElement.offsetHeight <= 50 && contextElement.parentElement) {
        // Try to get parent element for more context
        const parent = contextElement.parentElement;
        if (!parent.classList.contains("znai-documentation-page")) {
          contextElement = parent;
        }
        
        // Or get a few siblings if available
        const expandedContext = document.createElement("div");
        expandedContext.style.cssText = "margin: 0; padding: 0;";
        
        // Get up to 2 previous siblings
        let prevElement = contextElement.previousElementSibling;
        const prevElements = [];
        for (let i = 0; i < 2 && prevElement; i++) {
          if (!prevElement.classList.contains("znai-text-selection-menu")) {
            prevElements.unshift(prevElement.cloneNode(true));
          }
          prevElement = prevElement.previousElementSibling;
        }
        prevElements.forEach(el => expandedContext.appendChild(el));
        
        // Add current element
        expandedContext.appendChild(contextElement.cloneNode(true));
        
        // Get up to 2 next siblings
        let nextElement = contextElement.nextElementSibling;
        for (let i = 0; i < 2 && nextElement; i++) {
          if (!nextElement.classList.contains("znai-text-selection-menu")) {
            expandedContext.appendChild(nextElement.cloneNode(true));
          }
          nextElement = nextElement.nextElementSibling;
        }
        
        // Use the expanded context if it has multiple children
        if (expandedContext.children.length > 1 && contextElement.parentElement) {
          // Insert expanded context temporarily
          contextElement.parentElement.insertBefore(expandedContext, contextElement);
          contextElement = expandedContext;
        }
      }

      // Clone the context for screenshot without modifying the DOM
      const screenshotElement = contextElement.cloneNode(true) as HTMLElement;
      
      // Apply highlight to the cloned element
      const textToHighlight = selectedText;
      const walker = document.createTreeWalker(
        screenshotElement,
        NodeFilter.SHOW_TEXT,
        null
      );
      
      let highlightedSpan: HTMLSpanElement | null = null;
      let node;
      while (node = walker.nextNode()) {
        const textNode = node as Text;
        const index = textNode.nodeValue?.indexOf(textToHighlight);
        if (index !== undefined && index >= 0 && textNode.nodeValue) {
          const span = document.createElement("span");
          span.style.backgroundColor = "yellow";
          span.style.color = "black";
          span.className = "znai-screenshot-highlight";
          highlightedSpan = span;
          
          const before = textNode.nodeValue.substring(0, index);
          const highlighted = textNode.nodeValue.substring(index, index + textToHighlight.length);
          const after = textNode.nodeValue.substring(index + textToHighlight.length);
          
          const parent = textNode.parentNode;
          if (parent) {
            if (before) parent.insertBefore(document.createTextNode(before), textNode);
            span.appendChild(document.createTextNode(highlighted));
            parent.insertBefore(span, textNode);
            if (after) parent.insertBefore(document.createTextNode(after), textNode);
            parent.removeChild(textNode);
            break; // Only highlight first occurrence
          }
        }
      }
      
      // Temporarily add the screenshot element to DOM
      const isTemporaryContext = contextElement.style.cssText.includes("margin: 0");
      const originalParent = contextElement.parentElement;
      if (originalParent) {
        originalParent.insertBefore(screenshotElement, contextElement.nextSibling);
        contextElement.style.display = "none";
      }

      // Calculate dimensions with max height of 600px
      const maxHeight = 600;
      
      // Get the position of highlighted text to center it in the screenshot
      let highlightY = 0;
      let highlightHeight = 30; // Default height if we can't find the span
      
      // After adding to DOM, find the highlighted span to get its position
      const highlightInDOM = screenshotElement.querySelector('.znai-screenshot-highlight') as HTMLElement;
      if (highlightInDOM) {
        // Get the position relative to the screenshot element
        const screenshotRect = screenshotElement.getBoundingClientRect();
        const highlightRect = highlightInDOM.getBoundingClientRect();
        
        highlightY = highlightRect.top - screenshotRect.top;
        highlightHeight = highlightRect.height;
        
        console.log("Found highlight position:", {
          screenshotTop: screenshotRect.top,
          highlightTop: highlightRect.top,
          calculatedY: highlightY
        });
      }
      
      // Calculate the crop area to center the highlighted text
      const elementHeight = screenshotElement.scrollHeight;
      let cropTop = 0;
      let actualHeight = Math.min(elementHeight, maxHeight);
      
      console.log("Screenshot positioning:", {
        highlightY,
        highlightHeight,
        elementHeight,
        maxHeight,
        highlightedSpan: !!highlightedSpan
      });
      
      // Create a wrapper for cropping
      let wrapperElement: HTMLDivElement | null = null;
      let captureElement = screenshotElement;
      
      if (elementHeight > maxHeight) {
        // Center the highlight in the viewport
        const highlightCenter = highlightY + (highlightHeight / 2);
        const viewportCenter = maxHeight / 2;
        
        // Calculate how much to crop from the top
        cropTop = Math.max(0, highlightCenter - viewportCenter);
        
        // Make sure we don't crop beyond the bottom
        if (cropTop + maxHeight > elementHeight) {
          cropTop = elementHeight - maxHeight;
        }
        
        console.log("Crop calculation:", {
          highlightCenter,
          viewportCenter,
          cropTop,
          finalHeight: maxHeight,
          fullHeight: elementHeight
        });
        
        // Create a wrapper div with fixed height and overflow hidden
        wrapperElement = document.createElement('div');
        wrapperElement.style.cssText = `
          position: relative;
          width: ${screenshotElement.scrollWidth}px;
          height: ${maxHeight}px;
          overflow: hidden;
          margin: 0;
          padding: 0;
        `;
        
        // Position the screenshot element inside the wrapper
        screenshotElement.style.position = 'absolute';
        screenshotElement.style.top = `-${cropTop}px`;
        screenshotElement.style.left = '0';
        
        // Insert wrapper and move screenshot element inside it
        if (originalParent) {
          originalParent.insertBefore(wrapperElement, screenshotElement);
          wrapperElement.appendChild(screenshotElement);
          captureElement = wrapperElement;
        }
        
        actualHeight = maxHeight;
      }
      
      const imageDataUrl = await toPng(captureElement, {
        backgroundColor: "white",
        quality: 0.8,
        pixelRatio: 1,
        style: {
          margin: "0",
          padding: "0",
          maxWidth: "none",
          width: `${captureElement.scrollWidth}px`,
          height: `${actualHeight}px`
        },
        width: captureElement.scrollWidth,
        height: actualHeight
      });

      // Clean up - remove wrapper/screenshot element and restore original
      if (wrapperElement && wrapperElement.parentNode) {
        wrapperElement.parentNode.removeChild(wrapperElement);
      } else if (originalParent && screenshotElement.parentNode) {
        screenshotElement.parentNode.removeChild(screenshotElement);
      }
      
      if (contextElement.style.display === "none") {
        contextElement.style.display = "";
      }
      
      // Remove temporary expanded context if it was created
      if (isTemporaryContext && originalParent) {
        originalParent.removeChild(contextElement);
      }

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

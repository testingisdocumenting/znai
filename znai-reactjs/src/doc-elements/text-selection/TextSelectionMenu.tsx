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
import "./TextSelectionMenu.css";

export function TextSelectionMenu({ contentNode }: { contentNode: HTMLDivElement }) {
  const anchorRef = useRef<Node | null>(null);
  const dialogRef = useRef<HTMLDialogElement>(null);

  useEffect(() => {
    document.addEventListener("selectionchange", detectSelectionReset);
    document.addEventListener("mouseup", onMouseUp);
    contentNode.addEventListener("scroll", updatePopoverCoords);
    return () => {
      document.removeEventListener("selectionchange", detectSelectionReset);
      document.removeEventListener("mouseup", onMouseUp);
      contentNode.removeEventListener("scroll", updatePopoverCoords);
    };
  }, []);

  return (
    <dialog ref={dialogRef} popover="auto" className="znai-text-selection-menu">
      <div className="znai-text-selection-menu-item" onClick={clickMenu} onMouseDown={preventDefault}>
        Ask in Slack
      </div>
    </dialog>
  );

  function preventDefault(e: React.MouseEvent<HTMLDivElement>) {
    console.log("preventDefault");
    e.preventDefault();
  }

  function clickMenu() {
    console.log("clickMenu");
  }

  function showMenu(left: number, top: number) {
    if (!dialogRef.current) {
      return;
    }

    console.log("showMenu", left, top);

    const dialog = dialogRef.current;
    dialog.style.left = `${left}px`;
    dialog.style.top = `${top}px`;
    dialog.showPopover();
  }

  function updatePopoverCoords() {
    if (!anchorRef.current || !dialogRef.current) {
      return;
    }

    console.log("updatePopoverCoords");
    const coordinates = elementCoordinates(contentNode, anchorRef.current);
    if (coordinates) {
      console.log("new coords", coordinates);
      const dialog = dialogRef.current;
      dialog.style.left = `${coordinates.left}px`;
      dialog.style.top = `${coordinates.top}px`;
    }
  }

  function hidePopover() {
    console.log("hidePopover");
    dialogRef.current?.hidePopover();
  }

  function onMouseUp(e: MouseEvent) {
    console.log("onMouseUp", e);

    console.log("wrapper", contentNode);
    console.log("offset", contentNode.scrollTop);

    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      console.log("no selection");
      hidePopover();
      return;
    }

    const range = selection.getRangeAt(0);
    console.log("selection", range);

    anchorRef.current = range.startContainer as HTMLElement;

    const coordinates = elementCoordinates(contentNode, range.startContainer);
    console.log("coords", coordinates);
    if (coordinates) {
      showMenu(coordinates.left, coordinates.top);
    }
  }

  function detectSelectionReset() {
    console.log("detectSelectionReset");
    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      console.log("no selection");
      hidePopover();
      return;
    }
  }
}

function elementCoordinates(contentNode: HTMLDivElement, startNode: Node) {
  function findContentBlockNode(n: Node) {
    let it: Node | null = n;
    let result: HTMLElement | null = null;
    while (it !== null) {
      if (it instanceof HTMLElement && it.classList.contains("content-block")) {
        result = it;
      }
      it = it.parentNode;
    }

    return result;
  }

  // TODO pre cache anchor and search once especially on scroll event
  function coords(htmlElement?: HTMLElement) {
    if (!htmlElement) {
      return { top: 0, left: 0 };
    }

    const contentBlockNode = findContentBlockNode(htmlElement);
    function left() {
      if (!contentBlockNode) {
        return 0;
      }

      const clientRect = contentBlockNode.getBoundingClientRect();
      console.log("clientRect", clientRect);
      return clientRect.left + clientRect.width;
    }

    console.log("contentNode.scrollTop", contentNode.scrollTop);

    return {
      top: htmlElement.getBoundingClientRect().top /*+ contentNode.scrollTop*/,
      left: left(),
    };
  }

  if (startNode.nodeType === Node.TEXT_NODE) {
    return coords(startNode.parentElement as HTMLElement);
  } else if (startNode.nodeType === Node.ELEMENT_NODE) {
    return coords(startNode as HTMLElement);
  }
}

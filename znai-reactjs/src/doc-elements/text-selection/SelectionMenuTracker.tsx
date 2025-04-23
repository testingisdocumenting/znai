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

import React, { ReactNode, useRef } from "react";
import "./SelectionMenuTracker.css";

export function SelectionMenuTracker({ children }: { children: ReactNode }) {
  const dialogRef = useRef<HTMLDialogElement>(null);
  return (
    <>
      <div onMouseDown={onMouseDown} onMouseUp={onMouseUp}>
        {children}
      </div>
      <dialog ref={dialogRef} popover="auto" className="znai-selection-menu">
        <div>test</div>
      </dialog>
    </>
  );

  function onMouseDown(e: React.MouseEvent<HTMLDivElement>) {
    console.log("onMouseDown", e);
  }

  function showMenu(left: number, top: number) {
    if (!dialogRef.current) {
      return;
    }

    const dialog = dialogRef.current;
    dialog.style.left = `${left}px`;
    dialog.style.top = `${top}px`;
    dialog.showPopover();
  }

  function onMouseUp(e: React.MouseEvent<HTMLDivElement>) {
    function hidePopover() {
      dialogRef.current?.close();
    }

    console.log("onMouseUp", e);

    const selection = getSelection();
    if (selection === null || selection.rangeCount === 0 || selection.isCollapsed) {
      console.log("no selection");
      hidePopover();
      return;
    }

    const range = selection.getRangeAt(0);
    console.log("selection", range);

    const coordinates = elementCoordinates(range.startContainer);
    console.log("coords", coordinates);
    if (coordinates) {
      showMenu(coordinates.left, coordinates.top);
    }
  }
}

function elementCoordinates(startNode: Node) {
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

    return {
      top: htmlElement.getBoundingClientRect().top,
      left: left(),
    };
  }

  if (startNode.nodeType === Node.TEXT_NODE) {
    return coords(startNode.parentElement as HTMLElement);
  } else if (startNode.nodeType === Node.ELEMENT_NODE) {
    return coords(startNode as HTMLElement);
  }
}

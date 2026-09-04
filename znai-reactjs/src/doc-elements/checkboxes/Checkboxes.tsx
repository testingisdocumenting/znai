/*
 * Copyright 2026 znai maintainers
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

import React, { useState } from "react";

import { DocElementContent, WithElementsLibrary } from "../default-elements/DocElement";
import { currentPageIdWithDocId } from "../../structure/DocumentationNavigation";
import { isPreviewEnabled } from "../../structure/docMeta";

import "./Checkboxes.css";

interface CheckboxItem {
  id: string;
  content: DocElementContent;
}

interface Props extends WithElementsLibrary {
  blockId: string;
  checkboxItems: CheckboxItem[];
}

export function Checkboxes({ blockId, checkboxItems, elementsLibrary }: Props) {
  const [checkedById, setCheckedById] = useState<Record<string, boolean>>(() => loadCheckedState(blockId));

  return (
    <div className="znai-checkboxes content-block">
      {checkboxItems.map((item) => {
        const isChecked = !!checkedById[item.id];
        const itemClassName = "znai-checkboxes-item" + (isChecked ? " checked" : "");

        return (
          <div key={item.id} className={itemClassName}>
            <div className="znai-checkboxes-tick-cell">
              <input
                type="checkbox"
                className="znai-checkboxes-tick"
                checked={isChecked}
                onChange={() => toggle(item.id)}
              />
            </div>
            <div className="znai-checkboxes-item-content">
              <elementsLibrary.DocElement content={item.content} elementsLibrary={elementsLibrary} />
            </div>
          </div>
        );
      })}
    </div>
  );

  function toggle(id: string) {
    const newCheckedById = { ...checkedById, [id]: !checkedById[id] };
    setCheckedById(newCheckedById);
    saveCheckedState(blockId, newCheckedById);
  }
}

// blockId is derived from the block content, so each block owns its storage entry
// and editing a checklist resets its state
function storageKey(blockId: string) {
  return "znai-checkboxes-state:" + currentPageIdWithDocId() + ":" + blockId;
}

function loadCheckedState(blockId: string): Record<string, boolean> {
  // content changes often during preview, no point in persisting state across reloads
  if (isPreviewEnabled()) {
    return {};
  }

  try {
    const stored = localStorage.getItem(storageKey(blockId));
    if (stored) {
      const parsed = JSON.parse(stored);
      if (parsed && typeof parsed === "object" && !Array.isArray(parsed)) {
        return parsed;
      }
    }
  } catch (e) {
    console.warn("failed to load checkboxes state", e);
  }

  return {};
}

function saveCheckedState(blockId: string, checkedById: Record<string, boolean>) {
  if (isPreviewEnabled()) {
    return;
  }

  try {
    localStorage.setItem(storageKey(blockId), JSON.stringify(checkedById));
  } catch (e) {
    console.warn("failed to save checkboxes state", e);
  }
}

/*
 * Copyright 2026 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

interface TabSwitchEvent {
  tabName: string;
  triggeredNode: HTMLElement | null;
}

type TabSwitchListener = (event: TabSwitchEvent) => void;

const STORAGE_KEY = "znai-tabs-selection-history";
const MAX_HISTORY = 50;

class TabsRegistration {
  private listeners: TabSwitchListener[] = [];
  private tabsSelectionHistory: string[] = loadHistory();

  addTabSwitchListener(listener: TabSwitchListener) {
    this.listeners.push(listener);
  }

  removeTabSwitchListener(listener: TabSwitchListener) {
    removeFromArray(this.listeners, listener);
  }

  firstMatchFromHistory(names: string[]): string {
    return this.tabsSelectionHistory.find((n) => names.indexOf(n) >= 0) ?? names[0];
  }

  notifyNewTab({ tabName, triggeredNode }: TabSwitchEvent) {
    removeFromArray(this.tabsSelectionHistory, tabName);
    this.tabsSelectionHistory.unshift(tabName);

    if (this.tabsSelectionHistory.length > MAX_HISTORY) {
      this.tabsSelectionHistory.length = MAX_HISTORY;
    }

    saveHistory(this.tabsSelectionHistory);

    this.listeners.forEach((l) => l({ tabName, triggeredNode }));
  }
}

function loadHistory(): string[] {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);
    if (stored) {
      const parsed = JSON.parse(stored);
      if (Array.isArray(parsed)) {
        return parsed.filter((item: unknown) => typeof item === "string");
      }
    }
  } catch (e) {
    console.warn("failed to load tabs selection history", e);
  }

  return [];
}

function saveHistory(history: string[]) {
  try {
    localStorage.setItem(STORAGE_KEY, JSON.stringify(history));
  } catch (e) {
    console.warn("failed to save tabs selection history", e);
  }
}

function removeFromArray<T>(array: T[], value: T) {
  const idx = array.indexOf(value);
  if (idx !== -1) {
    array.splice(idx, 1);
  }
}

const tabsRegistration = new TabsRegistration();

export { tabsRegistration };
export type { TabSwitchEvent };

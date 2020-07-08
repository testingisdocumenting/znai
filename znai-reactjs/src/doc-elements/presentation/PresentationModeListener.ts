/*
 * Copyright 2020 znai maintainers
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

export interface PresentationModeListener {
  onPresentationEnter(pageSectionId: string): void;
}

class PresentationModeListeners {
  private listeners: PresentationModeListener[] = [];

  addListener(listener: PresentationModeListener) {
    this.listeners.push(listener);
  }

  removeListener(listener: PresentationModeListener) {
    this.listeners = this.listeners.filter(l => l !== listener);
  }

  notifyPresentationEnter(pageSectionId: string): void {
    this.listeners.forEach(l => l.onPresentationEnter(pageSectionId));
  }
}

export const presentationModeListeners = new PresentationModeListeners();

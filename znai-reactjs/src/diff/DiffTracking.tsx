/*
 * Copyright 2021 znai maintainers
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

import * as React from "react";
import { HtmlNodeDiff } from "./HtmlNodeDiff";

import "./DiffTracking.css";
import { mainPanelClassName } from "../layout/classNamesAndIds";

let enabled = false;
let autoDisable = false;

interface Props {
  children: React.ReactNode;
}

export class DiffTracking extends React.Component<Props, {}> {
  rootNode: HTMLElement | null = null;
  scrollNode: HTMLElement | null = null;

  render() {
    const { children } = this.props;

    return (
      <div className="znai-diff-highlight" ref={this.saveRootNode}>
        {children}
      </div>
    );
  }

  saveRootNode = (node: HTMLDivElement) => {
    if (!node) {
      // TODO investigate why
      return;
    }

    this.rootNode = node.querySelector(".page-content");
    this.scrollNode = node.querySelector("." + mainPanelClassName);
  };

  getSnapshotBeforeUpdate(_prevProps: Props, _prevState: {}) {
    if (!enabled) {
      return null;
    }

    return {
      beforeNode: this.rootNode!.cloneNode(true),
    };
  }

  componentDidUpdate(_prevProps: Props, _prevState: {}, snapshot: { beforeNode: HTMLElement }) {
    if (!snapshot) {
      return;
    }

    try {
      const diffNode = new HtmlNodeDiff(snapshot.beforeNode, this.rootNode!);
      diffNode.scrollAddedIntoView(this.scrollNode!);
      diffNode.animateAdded();
    } finally {
      if (autoDisable) {
        enabled = false;
        autoDisable = false;
      }
    }
  }
}

export function enableDiffTracking() {
  enabled = true;
}

export function disableDiffTracking() {
  enabled = false;
}

export function enableDiffTrackingForOneDomChangeTransaction() {
  enabled = true;
  autoDisable = true;
}

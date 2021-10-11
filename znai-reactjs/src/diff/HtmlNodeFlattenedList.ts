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

import { getNodeClassName, getNodeTagName } from "../utils/domNodes";

const containerClassNames = createContainerClassesList();

type WalkedNodes = (Node & ParentNode) | ChildNode;

interface HtmlNodeWithMeta {
  idx: number;
  node: Element;
  value: any;
  container: Element | null;
}

export class HtmlNodeFlattenedList {
  list: HtmlNodeWithMeta[];
  idx: number;
  currentContainerNode: Element | null;

  constructor(node: WalkedNodes) {
    this.list = [];
    this.idx = 0;
    this.currentContainerNode = null;

    this.walkNodes(node);
  }

  walkNodes(node: WalkedNodes) {
    if (node === null) {
      return;
    }

    const shouldStop = this.processNode(node);
    if (shouldStop) {
      return;
    }

    const childNodesCount = node.childNodes.length;
    for (let idx = 0; idx < childNodesCount; idx++) {
      const element = node as Element;
      if (isContainerNode(element)) {
        this.currentContainerNode = element;
      }

      this.walkNodes(node.childNodes[idx]);

      this.currentContainerNode = null;
    }
  }

  processNode(node: WalkedNodes) {
    const element = node as Element;

    if (ignoreNode(element)) {
      return true;
    }

    if (isTextNode(element)) {
      this.registerTextNode(node);
    } else if (isVisualNode(element)) {
      this.registerVisualNode(node);
    }

    return false;
  }

  registerTextNode(node: WalkedNodes) {
    this.registerNode(node.parentNode!, node.textContent);
  }

  registerVisualNode(node: WalkedNodes) {
    this.registerNode(node, attributesAsText(node as Element));
  }

  registerNode(node: WalkedNodes, value: any) {
    this.list.push({
      idx: this.idx++,
      node: node as Element,
      value: value,
      container: this.currentContainerNode,
    });
  }
}

function isContainerNode(node: Element) {
  if (!node.className) {
    return false;
  }

  const nodeClass = getNodeClassName(node);
  return containerClassNames.some(
    (className) => nodeClass.indexOf(className) !== -1
  );
}

function isTextNode(node: Element) {
  return node.nodeType === Node.TEXT_NODE && !!node.textContent;
}

function isVisualNode(node: Element) {
  const tagName = getNodeTagName(node);
  return tagName === "img";
}

function ignoreNode(node: Element) {
  return getNodeClassName(node) === "znai-page-last-update-time";
}

function attributesAsText(node: Element) {
  const parts = [];

  for (let idx = 0; idx < node.attributes.length; idx++) {
    const attr = node.attributes[idx];
    if (attr.name === "id" || attr.name === "class") {
      continue;
    }

    parts.push(`${attr.name}=${attr.value}`);
  }

  return parts.join(" ");
}

function createContainerClassesList() {
  return ["content-block", "wide-screen"];
}

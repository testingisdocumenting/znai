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

export function createNode({
  tagName,
  className,
  text,
  id,
  attrs = {},
  children = [],
}: any) {
  const node = document.createElement(tagName);

  if (text) {
    const textNode = document.createTextNode(text);
    node.appendChild(textNode);
  }

  if (id) {
    node.id = id;
  }

  if (className) {
    node.className = className;
  }

  Object.keys(attrs).forEach((key) => {
    node.setAttribute(key, attrs[key]);
  });

  children.forEach((child: ChildNode) => {
    node.appendChild(createNode(child));
  });

  return node;
}

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

import { DocElementContent, DocElementPayload } from "./DocElement";

export type ContentWalkControl = "continue" | "skip-children" | "stop";

export type ContentNodeVisitor = (node: DocElementPayload, ancestors: DocElementPayload[]) => ContentWalkControl | void;

/**
 * depth-first walk over doc element nodes, recursing through nested <code>content</code> arrays.
 * visitor receives each node plus its ancestors chain (walk root first) and can return
 * "skip-children" to avoid descending into a node or "stop" to end the whole walk early.
 * returns true when the walk was stopped early by the visitor.
 */
export function walkContentNodes(content: DocElementContent | undefined, visit: ContentNodeVisitor): boolean {
  if (!content) {
    return false;
  }

  // single ancestors array is mutated in place to avoid allocations per node,
  // visitors must copy it if they need to keep it after returning
  return walk(content, []);

  function walk(nodes: DocElementContent, ancestors: DocElementPayload[]): boolean {
    for (const node of nodes) {
      const control = visit(node, ancestors);
      if (control === "stop") {
        return true;
      }

      if (control !== "skip-children" && Array.isArray(node.content)) {
        ancestors.push(node);
        const stopped = walk(node.content, ancestors);
        ancestors.pop();

        if (stopped) {
          return true;
        }
      }
    }

    return false;
  }
}

/**
 * early-exit scan over every string in the tree: strings inside arrays and nested objects under any prop,
 * not just <code>content</code>. returns true as soon as the predicate matches a string value.
 */
export function someTextValue(value: unknown, predicate: (text: string) => boolean): boolean {
  if (typeof value === "string") {
    return predicate(value);
  }

  if (Array.isArray(value)) {
    return value.some((entry) => someTextValue(entry, predicate));
  }

  if (typeof value === "object" && value !== null) {
    for (const key in value) {
      // type holds doc element names like Snippet and is not a visible text
      if (key !== "type" && someTextValue((value as Record<string, unknown>)[key], predicate)) {
        return true;
      }
    }
  }

  return false;
}

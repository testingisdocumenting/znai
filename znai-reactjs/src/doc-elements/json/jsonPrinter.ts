/*
 * Copyright 2023 znai maintainers
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

import { TokensPrinter } from "../code-snippets/TokensPrinter";

interface JsonPrinterConfig {
  pathsToHighlight: string[];
  highlightKeys: string[];
  collapsedPaths: string[];
  previouslyCollapsedPaths: string[];
  onPathCollapse?(path: string): void;
  onPathUncollapse?(path: string): void;
}

interface CollapsedNode {
  path: string;
}

class JsonPrinter {
  printer = new TokensPrinter();
  _pathsToHighlight: Record<string, boolean> = {};
  _keysToHighlight: Record<string, boolean> = {};
  _previouslyCollapsedPaths: Record<string, boolean> = {};
  _collapsedPaths: Record<string, boolean> = {};
  onPathCollapse: (path: string) => void;
  onPathUncollapse: (path: string) => void;

  constructor({
    pathsToHighlight,
    highlightKeys,
    collapsedPaths,
    previouslyCollapsedPaths,
    onPathUncollapse,
    onPathCollapse,
  }: JsonPrinterConfig) {
    pathsToHighlight.forEach((p) => (this._pathsToHighlight[p] = true));
    highlightKeys.forEach((p) => (this._keysToHighlight[p] = true));
    collapsedPaths.forEach((p) => (this._collapsedPaths[p] = true));
    previouslyCollapsedPaths.forEach((p) => (this._previouslyCollapsedPaths[p] = true));

    this.onPathCollapse = onPathCollapse || (() => {});
    this.onPathUncollapse = onPathUncollapse || (() => {});
  }

  printKey(path: string, key: string) {
    const additionalTokenType = this.isKeyHighlighted(path) ? " highlighted" : "";
    this.printer.print("key" + additionalTokenType, '"' + key + '"');
    this.printer.printDelimiter(": ");
  }

  printValue(path: string, value: any, skipIndent: boolean) {
    if (value === null) {
      this.printSingleValue(path, null);
    } else if (Array.isArray(value)) {
      this.printArray(path, value, skipIndent);
    } else if (typeof value === "object") {
      this.printObject(path, value, skipIndent);
    } else {
      if (!skipIndent) {
        this.printer.printIndentation();
      }
      this.printSingleValue(path, value);
    }
  }

  printSingleValue(path: string, value: any) {
    const additionalTokenType = this.isHighlightedPath(path) ? " highlighted" : "";
    this.printer.print(tokenType() + additionalTokenType, valueToPrint());

    function tokenType() {
      if (value === null) {
        return "keyword";
      }

      if (value === "string") {
        return "string";
      }

      return "number";
    }

    function valueToPrint() {
      if (value === null) {
        return "null";
      }

      if (typeof value === "string") {
        return '"' + escapeQuote(value) + '"';
      }

      return value;
    }
  }

  printArray(path: string, values: any[], skipIndent: boolean) {
    if (values.length === 0) {
      this.printEmptyArray(skipIndent);
    } else if (this.isCollapsedPath(path)) {
      this.printCollapsedArray(path);
    } else {
      this.printNonEmptyArray(path, values, skipIndent);
    }
  }

  printEmptyArray(skipIndent: boolean) {
    if (!skipIndent) {
      this.printer.printIndentation();
    }

    this.printer.printDelimiter("[");
    this.printer.printDelimiter("]");
  }

  printCollapsedArray(path: string) {
    this.printer.printDelimiter("[");
    this.printCollapsed(path);
    this.printer.printDelimiter("]");
  }

  printNonEmptyArray(path: string, values: any[], skipIndent: boolean) {
    const collapsedNode = this.isPreviouslyCollapsedPath(path) ? { path: path } : null;
    this.openScope("[", skipIndent, collapsedNode);

    values.forEach((v, idx) => {
      const isLast = idx === values.length - 1;

      this.printValue(path + "[" + idx + "]", v, false);

      if (!isLast) {
        this.printer.printDelimiter(",");
        this.printer.println();
      }
    });

    this.closeScope("]");
  }

  printObject(path: string, json: any, skipIndent: boolean) {
    if (Object.keys(json).length === 0) {
      this.printEmptyObject(skipIndent);
    } else if (this.isCollapsedPath(path)) {
      this.printCollapsedObject(path);
    } else {
      this.printNonEmptyObject(path, json, skipIndent);
    }
  }

  printEmptyObject(skipIndent: boolean) {
    if (!skipIndent) {
      this.printer.printIndentation();
    }

    this.printer.printDelimiter("{");
    this.printer.printDelimiter("}");
  }

  printCollapsedObject(path: string) {
    this.printer.printDelimiter("{");
    this.printCollapsed(path);
    this.printer.printDelimiter("}");
  }

  printNonEmptyObject(path: string, json: any, skipIndent: boolean) {
    const collapsedNode: CollapsedNode | null = this.isPreviouslyCollapsedPath(path) ? { path: path } : null;
    this.openScope("{", skipIndent, collapsedNode);

    const keys = Object.keys(json);
    keys.forEach((key, idx) => {
      const isLast = idx === keys.length - 1;

      const childPath = path + "." + key;

      this.printer.printIndentation();
      this.printKey(childPath, key);
      this.printValue(childPath, json[key], true);

      if (!isLast) {
        this.printer.printDelimiter(",");
        this.printer.println();
      }
    });

    this.closeScope("}");
  }

  printCollapsed(path: string) {
    this.printer.printCollapsed("...", () => this.onPathUncollapse(path));
  }

  printCollapse(path: string) {
    this.printer.printCollapse("-", () => this.onPathCollapse(path));
  }

  openScope(delimiter: string, skipIndent: boolean, collapsedNode: CollapsedNode | null) {
    if (!skipIndent) {
      this.printer.printIndentation();
    }

    this.printer.printDelimiter(delimiter);
    if (collapsedNode) {
      this.printCollapse(collapsedNode.path);
    }

    this.printer.println();
    this.printer.indentRight();
  }

  closeScope(delimiter: string) {
    this.printer.println();
    this.printer.indentLeft();
    this.printer.printIndentation();
    this.printer.printDelimiter(delimiter);
  }

  isKeyHighlighted(path: string) {
    return this._keysToHighlight.hasOwnProperty(path);
  }

  isHighlightedPath(path: string) {
    return this._pathsToHighlight.hasOwnProperty(path);
  }

  isCollapsedPath(path: string) {
    return this._collapsedPaths.hasOwnProperty(path);
  }

  isPreviouslyCollapsedPath(path: string) {
    return this._previouslyCollapsedPaths.hasOwnProperty(path);
  }
}

interface PrintJsonArg extends Partial<JsonPrinterConfig> {
  data: any;
  rootPath: string;
}

export function printJson({
  rootPath,
  data,
  pathsToHighlight,
  highlightKeys,
  previouslyCollapsedPaths,
  collapsedPaths,
  onPathUncollapse,
  onPathCollapse,
}: PrintJsonArg) {
  const jsonPrinter = new JsonPrinter({
    pathsToHighlight: pathsToHighlight || [],
    highlightKeys: highlightKeys || [],
    previouslyCollapsedPaths: previouslyCollapsedPaths || [],
    collapsedPaths: collapsedPaths || [],
    onPathUncollapse,
    onPathCollapse,
  });

  jsonPrinter.printValue(rootPath, data, false);

  return jsonPrinter.printer.linesOfTokens;
}

function escapeQuote(text: string) {
  return text.replace(/"/g, '\\"');
}

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

import AnsiUp from "ansi_up";
import { TokensPrinter } from '../code-snippets/TokensPrinter';

export function convertAnsiToTokenLines(lines: string[]): any[] {
  const ansiUp = createAnsiUp();

  const html = ansiUp.ansi_to_html(lines.join('\n'));
  const domParser = new DOMParser();

  const printer = new TokensPrinter();

  // TODO create PR https://github.com/drudru/ansi_up to generate intermediate tokens data instead of HTML
  const doc = domParser.parseFromString(html, 'text/html');
  doc.body.childNodes.forEach((node) => {
    handleNode(node);
  })

  return printer.linesOfTokens;

  function handleNode(node: any) {
    const type = typeFromNode(node);
    const content = contentFromNode(node);

    handleNewLine(content);

    function handleNewLine(content: string) {
      const parts = content.split('\n')

      let len = parts.length;
      for (let idx = 0; idx < len; idx++) {
        const part = parts[idx];
        if (part.length !== 0) {
          printer.print(type, part);
        }

        if (idx !== len - 1) {
          printer.flushLine();
        }
      }
    }
  }

  function typeFromNode(node: any) {
    const types = [];
    if (node.style && node.style.getPropertyValue('font-weight') === 'bold') {
      types.push('znai-ansi-bold');
    }

    if (node.className) {
      types.push('znai-' + node.className);
    }

    return types.length === 0 ? 'znai-ansi-regular' : types.join(' ');
  }

  function contentFromNode(node: any) {
    return node.nodeType === 3 ?
      node.nodeValue:
      node.childNodes[0].nodeValue;
  }
}

function createAnsiUp() {
  const ansiUp = new AnsiUp();
  ansiUp.use_classes = true;

  return ansiUp;
}

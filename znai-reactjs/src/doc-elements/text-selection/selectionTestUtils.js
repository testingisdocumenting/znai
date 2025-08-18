import { JSDOM } from "jsdom";

export function setupDOM(htmlContent) {
  const dom = new JSDOM(
    `
      <!DOCTYPE html>
      <html lang="en">
        <body>
          ${htmlContent}
        </body>
      </html>
    `,
    { pretendToBeVisual: true }
  );

  const document = dom.window.document;
  const window = dom.window;

  global.window = window;
  global.document = document;
  global.Node = window.Node;
  global.NodeFilter = window.NodeFilter;

  const container = document.body;

  return { document, window, container };
}

export function selectText(startNode, startOffset, endNode, endOffset) {
  const range = global.document.createRange();
  range.setStart(startNode, startOffset);
  range.setEnd(endNode, endOffset);

  const selection = global.window.getSelection();
  selection.removeAllRanges();
  selection.addRange(range);
}

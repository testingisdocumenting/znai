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

import { afterEach, describe, expect, it } from "vitest";
import { removeSearchHighlight, startSearchHighlightSession } from "./searchResultHighlighter";

let root: HTMLElement;
let disposeSession: (() => void) | undefined;

function startSession(snippets: string[], html = "<p>use cancel_trade to abort</p>") {
  root = document.createElement("div");
  root.innerHTML = html;
  document.body.appendChild(root);

  disposeSession = startSearchHighlightSession(root, snippets);
}

function markedTexts() {
  return Array.from(root.querySelectorAll("mark")).map((mark) => mark.textContent);
}

function mountLateContent() {
  const lateMounted = document.createElement("div");
  lateMounted.innerHTML = "<p>late mounted cancel_trade details</p>";
  root.appendChild(lateMounted);
  return lateMounted;
}

// MutationObserver delivers records in a microtask
function observerDelivery() {
  return new Promise((resolve) => setTimeout(resolve, 0));
}

afterEach(() => {
  disposeSession?.();
  root.remove();
});

describe("startSearchHighlightSession", () => {
  it("highlights snippets present at session start", () => {
    startSession(["cancel_trade"]);

    expect(root.querySelectorAll("mark").length).toBe(1);
  });

  it("highlights content mounted after session start", async () => {
    startSession(["cancel_trade"]);
    const lateMounted = mountLateContent();

    await observerDelivery();

    expect(lateMounted.querySelectorAll("mark").length).toBe(1);
    // marks added by the session itself must not re-trigger another marking pass
    expect(root.querySelectorAll("mark").length).toBe(2);
  });

  it("stops highlighting late mounted content after dispose", async () => {
    startSession(["cancel_trade"]);
    disposeSession!();

    const lateMounted = mountLateContent();
    await observerDelivery();

    expect(lateMounted.querySelector("mark")).toBeNull();
  });

  it("removeSearchHighlight removes session marks", () => {
    startSession(["cancel_trade"]);
    removeSearchHighlight(root);

    expect(root.querySelector("mark")).toBeNull();
  });
});

// highlights landing inside hidden="until-found" content (e.g. collapsed read more) trigger
// the same beforematch event browser find-in-page uses, owning components reveal through one path
describe("hidden content reveal", () => {
  function trackReveal(container: Element) {
    const revealed = { count: 0 };
    container.addEventListener("beforematch", () => revealed.count++);
    return revealed;
  }

  function createRoot(html: string) {
    root = document.createElement("div");
    root.innerHTML = html;
    document.body.appendChild(root);
  }

  it("fires beforematch on a hidden container that received a highlight", () => {
    createRoot('<div hidden="until-found"><p>use cancel_trade to abort</p></div>');
    const revealed = trackReveal(root.querySelector("[hidden]")!);

    disposeSession = startSearchHighlightSession(root, ["cancel_trade"]);

    expect(revealed.count).toBe(1);
  });

  it("does not fire beforematch when hidden content has no highlight", () => {
    createRoot('<div hidden="until-found"><p>unrelated text</p></div>');
    const revealed = trackReveal(root.querySelector("[hidden]")!);

    disposeSession = startSearchHighlightSession(root, ["cancel_trade"]);

    expect(revealed.count).toBe(0);
  });

  it("fires beforematch on every hidden ancestor of a highlight, like nested until-found regions", () => {
    createRoot('<div hidden="until-found" class="outer"><div hidden="until-found"><p>cancel_trade</p></div></div>');
    const outerRevealed = trackReveal(root.querySelector(".outer")!);
    const innerRevealed = trackReveal(root.querySelector(".outer [hidden]")!);

    disposeSession = startSearchHighlightSession(root, ["cancel_trade"]);

    expect(innerRevealed.count).toBe(1);
    // outer count includes the inner event only if it bubbled, beforematch does not bubble
    expect(outerRevealed.count).toBe(1);
  });

  it("fires beforematch for hidden content mounted after session start", async () => {
    createRoot("<p>visible cancel_trade</p>");
    disposeSession = startSearchHighlightSession(root, ["cancel_trade"]);

    const lateMounted = document.createElement("div");
    lateMounted.innerHTML = '<div hidden="until-found"><p>late cancel_trade</p></div>';
    const revealed = trackReveal(lateMounted.querySelector("[hidden]")!);
    root.appendChild(lateMounted);

    await observerDelivery();

    expect(revealed.count).toBe(1);
  });
});

// underscore is part of indexed symbols (see flexSearch.ts encoder), so the highlighter
// treats it as a literal character of the snippet, not as ignorable punctuation
describe("underscore as part of searched symbols", () => {
  it("highlights underscore identifier surrounded by text and punctuation", () => {
    startSession(["my_func"], "<p>call my_func() now</p>");

    expect(markedTexts()).toEqual(["my_func"]);
  });

  it("highlights prefix snippet occurrences including the underscore", () => {
    // accuracy "partially" matches the snippet anywhere, so a forward tokenization prefix
    // like bu_id also highlights the start of a longer identifier
    startSession(["bu_id"], "<p>use bu_id here and bu_id_extra there</p>");

    expect(markedTexts()).toEqual(["bu_id", "bu_id"]);
  });

  it("does not match text with underscore when snippet has none", () => {
    // canceltrade and cancel_trade are different symbols now that underscore is not ignorable
    startSession(["canceltrade"], "<p>use cancel_trade to abort</p>");

    expect(root.querySelector("mark")).toBeNull();
  });
});

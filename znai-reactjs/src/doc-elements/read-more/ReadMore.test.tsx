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

import { describe, it, expect } from "vitest";
import { render, fireEvent } from "@testing-library/react";
import React from "react";

import { ReadMore } from "./ReadMore";
import { DocElement, DocElementContent } from "../default-elements/DocElement";

// jsdom implements onbeforematch on a prototype, remove it there to mimic browsers without support
function withoutBeforematchSupport(test: () => void) {
  let owner: any = document.body;
  while (owner && !Object.getOwnPropertyDescriptor(owner, "onbeforematch")) {
    owner = Object.getPrototypeOf(owner);
  }
  const descriptor = owner && Object.getOwnPropertyDescriptor(owner, "onbeforematch");
  if (owner) {
    delete owner.onbeforematch;
  }

  try {
    test();
  } finally {
    if (owner && descriptor) {
      Object.defineProperty(owner, "onbeforematch", descriptor);
    }
  }
}

const elementsLibrary: any = {
  DocElement,
  TestText: ({ text }: { text: string }) => <div className="test-text">{text}</div>,
};

const content: DocElementContent = [{ type: "TestText", text: "hidden cancel_trade details" } as any];

function renderReadMore(extraProps: object = {}) {
  return render(
    <ReadMore title="details" content={content} elementsLibrary={elementsLibrary} {...(extraProps as any)} />
  );
}

describe("ReadMore", () => {
  it("keeps collapsed content mounted on a regular page for the highlight engine", () => {
    const { container } = renderReadMore();

    expect(container.querySelector(".znai-read-more")).toHaveClass("collapsed");
    expect(container.querySelector(".test-text")).not.toBeNull();
  });

  it("hides collapsed content with until-found so browser find-in-page can match it", () => {
    const { container } = renderReadMore();

    expect(container.querySelector(".znai-read-more-content")).toHaveAttribute("hidden", "until-found");
  });

  it("falls back to plain hidden when the browser has no beforematch support", () => {
    // hiddenContent.css reverts the normalize display none reset for until-found,
    // so non supporting browsers must not receive that attribute value or content would show
    withoutBeforematchSupport(() => {
      const { container } = renderReadMore();

      expect(container.querySelector(".znai-read-more-content")).toHaveAttribute("hidden", "");
    });
  });

  it("expands when browser find-in-page reveals hidden content", () => {
    const { container } = renderReadMore();

    fireEvent(container.querySelector(".znai-read-more-content")!, new Event("beforematch"));

    expect(container.querySelector(".znai-read-more")).toHaveClass("expanded");
    expect(container.querySelector(".znai-read-more-content")).not.toHaveAttribute("hidden");
  });

  it("reveals content during search when it contains matched terms", () => {
    const { container } = renderReadMore({ searchSnippets: ["cancel_trade"] });

    expect(container.querySelector(".znai-read-more")).toHaveClass("expanded");
    expect(container.querySelector(".test-text")).not.toBeNull();
  });

  it("keeps content mounted when snippets arrive after mount, highlighter reveals it via beforematch", () => {
    const { container, rerender } = renderReadMore();

    rerender(
      <ReadMore
        title="details"
        content={content}
        elementsLibrary={elementsLibrary}
        {...({ searchSnippets: ["cancel_trade"] } as any)}
      />
    );

    // still mounted and collapsed, waiting for the highlight session to dispatch beforematch
    expect(container.querySelector(".znai-read-more")).toHaveClass("collapsed");
    expect(container.querySelector(".test-text")).not.toBeNull();

    fireEvent(container.querySelector(".znai-read-more-content")!, new Event("beforematch"));

    expect(container.querySelector(".znai-read-more")).toHaveClass("expanded");
  });

  it("does not mount content during search when no terms match", () => {
    const { container } = renderReadMore({ searchSnippets: ["deploy"] });

    expect(container.querySelector(".znai-read-more")).toHaveClass("collapsed");
    expect(container.querySelector(".test-text")).toBeNull();
  });

  it("mounts content on manual reveal during search", () => {
    const { container } = renderReadMore({ searchSnippets: ["deploy"] });

    expect(container.querySelector(".test-text")).toBeNull();

    fireEvent.click(container.querySelector(".znai-read-more-title-block")!);

    expect(container.querySelector(".test-text")).not.toBeNull();
  });
});

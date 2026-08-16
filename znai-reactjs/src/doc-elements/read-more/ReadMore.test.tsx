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

  it("reveals content during search and highlights terms when it contains matched terms", () => {
    const { container } = renderReadMore({ searchSnippets: ["cancel_trade"] });

    expect(container.querySelector(".znai-read-more")).toHaveClass("expanded");
    expect(container.querySelector(".test-text")).not.toBeNull();
    expect(container.querySelector("mark")).not.toBeNull();
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

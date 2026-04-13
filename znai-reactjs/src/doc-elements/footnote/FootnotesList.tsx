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

import React from "react";
import { DocElementContent, ElementsLibraryMap } from "../default-elements/DocElement";
import { footnoteRefAnchorId, footnoteEntryAnchorId } from "./footnoteAnchors";

import "./FootnotesList.css";

export interface Footnote {
  label: string;
  content: DocElementContent;
  refCount: number;
}

interface Props {
  footnotes: Footnote[];
  elementsLibrary: ElementsLibraryMap;
}

export function FootnotesList({ footnotes, elementsLibrary }: Props) {
  if (!footnotes || footnotes.length === 0) {
    return null;
  }

  return (
    <div className="znai-footnotes-list">
      <div className="znai-footnotes-list-content content-block">
        {footnotes.map((footnote) => (
          <FootnotesListEntry key={footnote.label} footnote={footnote} elementsLibrary={elementsLibrary} />
        ))}
      </div>
    </div>
  );
}

interface EntryProps {
  footnote: Footnote;
  elementsLibrary: ElementsLibraryMap;
}

function FootnotesListEntry({ footnote, elementsLibrary }: EntryProps) {
  const contentWithBackLinks = appendBackLinksToContent(footnote);

  return (
    <div id={footnoteEntryAnchorId(footnote.label)} className="znai-footnotes-list-entry">
      <div className="znai-footnotes-list-entry-label">{footnote.label}.</div>
      <div className="znai-footnotes-list-entry-content">
        <elementsLibrary.DocElement elementsLibrary={elementsLibrary} content={contentWithBackLinks} />
      </div>
    </div>
  );
}

function appendBackLinksToContent(footnote: Footnote): DocElementContent {
  const backLinkElement = {
    type: "FootnoteBackLinks",
    label: footnote.label,
    refCount: footnote.refCount,
  };

  if (!footnote.content || footnote.content.length === 0) {
    return [backLinkElement];
  }

  const content = [...footnote.content];
  const lastElement = content[content.length - 1];

  if (lastElement.type === "Paragraph" && lastElement.content) {
    content[content.length - 1] = {
      ...lastElement,
      content: [...lastElement.content, backLinkElement],
    };
  } else {
    content.push(backLinkElement);
  }

  return content;
}

interface BackLinksProps {
  label: string;
  refCount: number;
}

export function FootnoteBackLinks({ label, refCount }: BackLinksProps) {
  if (refCount === 1) {
    return (
      <span className="znai-footnote-back-links">
        <a href={"#" + footnoteRefAnchorId(label, 1)} className="znai-footnote-back-link">
          ↩
        </a>
      </span>
    );
  }

  const links = [];
  for (let i = 1; i <= refCount; i++) {
    links.push(
      <a key={i} href={"#" + footnoteRefAnchorId(label, i)} className="znai-footnote-back-link">
        ↩<sup>{String.fromCharCode(96 + i)}</sup>
      </a>
    );
  }

  return <span className="znai-footnote-back-links">{links}</span>;
}

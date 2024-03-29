/*
 * Copyright 2021 znai maintainers
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

import * as React from "react";

import { mergeWithGlobalDocReferences } from "../references/globalDocReferences";
import { ReferenceLinkWrapper } from "../references/ReferenceLinkWrapper";
import { SnippetDocReferences } from "../references/SnippetDocReferences";

import "./InlinedCode.css";

interface Props {
  code: string;
  references?: SnippetDocReferences;
}

export function InlinedCode({ code, references }: Props) {
  const mergedReferences: SnippetDocReferences = mergeWithGlobalDocReferences(references);
  const reference = mergedReferences[code];

  const renderedCode = <code className="znai-inlined-code">{code}</code>;

  if (reference) {
    return <ReferenceLinkWrapper referenceUrl={reference.pageUrl}>{renderedCode}</ReferenceLinkWrapper>;
  }

  return renderedCode;
}

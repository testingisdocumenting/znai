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

import React from "react";

import { PresentationHeading } from "./PresentationHeading";

import { TextBadge } from "../badge/TextBadge";
import { Icon } from "../icons/Icon";

import { DocElementContent, ElementsLibraryMap } from "./DocElement";
import { HeadingContent } from "./HeadingContent";

import "./SubHeading.css";
import "./HeadingStyles.css";

interface Props {
  level: number;
  id: string;
  headingContent?: DocElementContent;
  additionalIds?: string[];
  title: string;
  badge?: string;
  style?: string;
  elementsLibrary: ElementsLibraryMap;
}

export function SubHeading({ level, title, headingContent, id, additionalIds, badge, style, elementsLibrary }: Props) {
  const Element = `h${level}`;
  const className = "content-block znai-heading" + (style ? " " + style : "");

  return (
    // @ts-ignore
    <Element className={className} id={id}>
      <span className="znai-heading-title">
        <HeadingContent title={title} headingContent={headingContent} elementsLibrary={elementsLibrary} />
      </span>
      {badge && <TextBadge text={badge} useExtraLeftMargin={true} />}
      <a href={"#" + id}>
        <Icon id="link" />
      </a>
      {(additionalIds || []).map((id) => (
        <span id={id} />
      ))}
    </Element>
  );
}

export const presentationSubHeading = { component: PresentationHeading, numberOfSlides: () => 1 };

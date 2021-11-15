/*
 * Copyright 2020 znai maintainers
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

import { Icon } from "../icons/Icon";
import { presentationModeListeners } from "../presentation/PresentationModeListener";

import { TextBadge } from "../badge/TextBadge";

import "./SectionTitle.css";
import "./HeadingStyles.css";

interface Props {
  id: string;
  title: string;
  badge?: string;
  style?: string;
}

export function SectionTitle({ id, title, badge, style }: Props) {
  const className = "content-block znai-section-title znai-heading" + (style ? " " + style : "");
  return id ? (
    // @ts-ignore
    <h1 className={className} id={id}>
      {title}
      {badge && <TextBadge text={badge} />}
      <div className="znai-section-title-actions">
        <a href={"#" + id}>
          <Icon id="link" />
        </a>
        <Icon id="maximize" className="znai-section-title-presentation" onClick={openPresentation} />
      </div>
    </h1>
  ) : (
    // @ts-ignore
    // eslint-disable-next-line jsx-a11y/heading-has-content
    <h1 className="empty-section-title" id="implicit-section" />
  );

  function openPresentation() {
    presentationModeListeners.notifyPresentationEnter(id);
  }
}

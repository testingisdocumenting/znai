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

import React, { useEffect, useRef, useState } from "react";

import { ApiParameterProps } from "./ApiParameters";
import ApiParameters from "./ApiParameters";
import { ReferenceLinkWrapper } from "../references/ReferenceLinkWrapper";

import { Icon } from "../icons/Icon";
import { ApiLinkedTextBlock } from "./ApiLinkedTextBlock";

import { WithElementsLibrary } from "../default-elements/DocElement";

import "./ApiParameter.css";

interface Props extends WithElementsLibrary, ApiParameterProps {
  nestedLevel: number;
  isExpanded?: boolean;
  references?: any;
}

export function ApiParameter({
  anchorId,
  name,
  type,
  description,
  children,
  nestedLevel,
  isExpanded,
  references,
  elementsLibrary,
}: Props) {
  const [userDrivenIsExpanded, setUserDrivenIsExpanded] = useState(isExpanded);
  const [width, setWidth] = useState(0);
  const nameAndTypeNodeRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (nameAndTypeNodeRef.current) {
      const divWidth = nameAndTypeNodeRef.current.getBoundingClientRect().width;
      setWidth(divWidth);
    }
  }, [name, type]);

  const reference = findReference();

  const commonClassName =
    " znai-api-param-cell" +
    (userDrivenIsExpanded ? " expanded" : "") +
    (children ? " expandable" : "") +
    (reference ? " with-reference" : "") +
    (nestedLevel > 0 ? " nested-" + nestedLevel : "");

  const nameTypeClassName = "znai-api-param-name-type-toggle-cell" + commonClassName;
  const descriptionClassName = "znai-api-param-description-cell" + commonClassName;

  const toggleOnClick = children ? toggleExpand : undefined;

  const expandToggle = children && <div className="expand-toggle">{userDrivenIsExpanded ? "-" : "+"}</div>;

  const renderedChildren =
    children && userDrivenIsExpanded ? (
      <React.Fragment>
        <div />
        <ApiParameters
          parameters={children}
          nestedLevel={nestedLevel + 1}
          parentWidth={width}
          elementsLibrary={elementsLibrary}
          references={references}
        />
      </React.Fragment>
    ) : null;

  const referenceUrl = reference ? reference.pageUrl : null;

  return (
    <React.Fragment>
      <div className={nameTypeClassName}>
        <div className="znai-api-param-name-type-toggle" ref={nameAndTypeNodeRef}>
          <div className="znai-api-param-name">
            <ReferenceLinkWrapper referenceUrl={referenceUrl}>{name}</ReferenceLinkWrapper>
          </div>
          <div className="znai-api-param-type-and-toggle" onClick={toggleOnClick}>
            {expandToggle}
            <div className="znai-api-param-type">
              {typeof type === "string" ? type : <ApiLinkedTextBlock linkedText={type} />}
            </div>
          </div>
        </div>
        <div className="znai-api-param-anchor" id={anchorId}>
          {anchorId && (
            <a href={"#" + anchorId}>
              <Icon id="link" />
            </a>
          )}
        </div>
      </div>
      <div className={descriptionClassName}>
        <elementsLibrary.DocElement content={description} elementsLibrary={elementsLibrary} />
      </div>
      {renderedChildren}
    </React.Fragment>
  );

  function toggleExpand() {
    setUserDrivenIsExpanded((prev) => !prev);
  }

  function findReference() {
    if (!references) {
      return null;
    }

    const byName = references[name];
    if (byName) {
      return byName;
    }

    if (typeof type !== "string") {
      if (type.length !== 1 || type[0].url) {
        return null;
      }

      const rawType = type[0].text;
      const byType = references[rawType];
      if (byType) {
        return byType;
      }
    } else {
      const byType = references[type];
      if (byType) {
        return byType;
      }
    }
  }
}

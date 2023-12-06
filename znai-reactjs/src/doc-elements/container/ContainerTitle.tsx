/*
 * Copyright 2022 znai maintainers
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

import React, { useEffect, useState } from "react";

import { Icon } from "../icons/Icon";

import "./ContainerTitle.css";

export interface ContainerTitleCommonProps {
  title?: string;
  collapsed?: boolean;
  anchorId?: string;
}

interface Props extends ContainerTitleCommonProps {
  additionalContainerClassNames?: string;
  additionalTitleClassNames?: string;

  onCollapseToggle?(): void;
}

/**
 * title strip for snippets, api parameters, tables, images, etc.
 * with collapse option, noGap and anchor
 */
export function ContainerTitle({
  title,
  additionalContainerClassNames,
  additionalTitleClassNames,
  collapsed,
  anchorId,
  onCollapseToggle,
}: Props) {
  const collapsible = collapsed !== undefined;

  const containerClassName =
    "znai-container-title-wrapper" +
    (collapsible ? " collapsible" : "") +
    (collapsed ? " collapsed" : " expanded") +
    (additionalContainerClassNames ? " " + additionalContainerClassNames : "");

  const titleClassName =
    "znai-container-title" +
    (collapsible ? " collapsible" : "") +
    (additionalTitleClassNames ? " " + additionalTitleClassNames : "");

  return (
    <div className={containerClassName} id={anchorId}>
      {collapsible && (
        <div className="znai-container-title-collapse-toggle" onClick={onCollapseToggle}>
          {collapsed ? "+" : "-"}
        </div>
      )}

      <div className={titleClassName}>
        <div className="znai-container-title-label">{title}</div>
        {anchorId && (
          <div className="znai-container-title-anchor">
            <a href={"#" + anchorId}>
              <Icon id="link" />
            </a>
          </div>
        )}
      </div>
    </div>
  );
}

export function useIsUserDrivenCollapsed(originalCollapsed?: boolean) {
  const [userDrivenCollapsed, setUserDrivenCollapsed] = useState(originalCollapsed);

  useEffect(() => {
    setUserDrivenCollapsed(originalCollapsed);
  }, [originalCollapsed]);

  return {
    userDrivenCollapsed,
    collapseToggle,
  };

  function collapseToggle() {
    setUserDrivenCollapsed((prev) => !prev);
  }
}

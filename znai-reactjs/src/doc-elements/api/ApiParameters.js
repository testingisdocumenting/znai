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

import React, { useState } from "react";
import ApiParameter from "./ApiParameter";

import "./ApiParameters.css";

export default function ApiParameters({
                                        parameters,
                                        title,
                                        references,
                                        nestedLevel,
                                        small,
                                        noWrap,
                                        wide,
                                        collapsible,
                                        collapsed,
                                        parentWidth = 0,
                                        next,
                                        prev,
                                        elementsLibrary
                                      }) {
  const [userDrivenCollapsed, setUserDrivenCollapsed] = useState(collapsed);

  const isExpanded = !nestedLevel && parameters.length === 1 && parameters[0].children;

  const renderedParameters = userDrivenCollapsed ? null :
    (parameters || []).map(p => <ApiParameter key={p.name}
                                              anchorId={p.anchorId}
                                              name={p.name}
                                              type={p.type}
                                              isExpanded={isExpanded}
                                              children={p.children}
                                              description={p.description}
                                              nestedLevel={nestedLevel}
                                              references={references}
                                              elementsLibrary={elementsLibrary} />);

  const isNested = nestedLevel > 0;
  const className = "znai-api-parameters" +
    (isNested ? " nested" : "") +
    (small ? " small" : "") +
    (noWrap ? " no-wrap" : "");

  const style = { marginLeft: -parentWidth };

  const rendered = (
    <div className={className} style={style}>
      <ApiParametersTitle title={title} nestedLevel={nestedLevel} collapsible={collapsible} collapsed={userDrivenCollapsed} collapseToggle={collapseToggle}/>
      {renderedParameters}
    </div>
  );

  if (!isNested) {
    const containerClass = "znai-api-parameters-wrapper " +
      (wide ? "wide" : "content-block") +
      (next?.type === "ApiParameters" ? " no-bottom-margin" : "") +
      (prev?.type === "ApiParameters" ? " no-top-margin" : "")

    return (
      <div className={containerClass}>
        {rendered}
      </div>
    )
  }

  return rendered

  function collapseToggle() {
    setUserDrivenCollapsed(prev => !prev)
  }
}

function ApiParametersTitle({ title, nestedLevel, collapsible, collapsed, collapseToggle}) {
  if (!title || nestedLevel > 0) {
    return null;
  }

  return (
    <div className="znai-api-parameters-title-cell">
      {collapsible && (<div className="znai-api-parameters-collapse-toggle"
                            onClick={collapseToggle}>{collapsed ? "+" : "-"}</div>)}

      <div className="znai-api-parameters-title">
        {title}
      </div>
    </div>
  );
}

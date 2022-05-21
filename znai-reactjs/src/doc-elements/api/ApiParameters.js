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
import ApiParameter from "./ApiParameter";

import "./ApiParameters.css";

export default function ApiParameters({
                                        parameters,
                                        title,
                                        references,
                                        nestedLevel,
                                        small,
                                        noWrap,
                                        parentWidth = 0,
                                        elementsLibrary
                                      }) {
  const renderedParameters = (parameters || []).map(p => <ApiParameter key={p.name}
                                                                       anchorId={p.anchorId}
                                                                       name={p.name}
                                                                       type={p.type}
                                                                       isExpanded={false}
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

  return (
    <div className={className} style={style}>
      <ApiParametersTitle title={title} nestedLevel={nestedLevel} />
      {renderedParameters}
    </div>
  );
}

function ApiParametersTitle({ title, nestedLevel }) {
  if (!title || nestedLevel > 0) {
    return null;
  }

  return (
    <div className="znai-api-parameters-title-cell">
      <div className="znai-api-parameters-title">
        {title}
      </div>
    </div>
  );
}

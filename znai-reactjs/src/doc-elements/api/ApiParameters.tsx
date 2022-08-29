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

import React, { useEffect, useState } from "react";

import { DocElementContent, DocElementProps } from "../default-elements/DocElement";
import { ApiLinkedText } from "./ApiLinkedText";
import { ApiParameter } from "./ApiParameter";

import { Snippet } from "../code-snippets/Snippet";

import { ContainerTitle } from "../title/ContainerTitle";

import "./ApiParameters.css";

export interface ApiParameterProps {
  name: string;
  anchorId?: string;
  type: ApiLinkedText | string;
  description: DocElementContent;
  children?: ApiParameterProps[];
}

interface Props extends DocElementProps {
  title?: string;
  anchorId?: string;
  parameters: ApiParameterProps[];
  example?: string;
  nestedLevel?: number;
  small?: boolean;
  noWrap?: boolean;
  wide?: boolean;
  collapsed?: boolean;
  parentWidth?: number;
  references?: any;
}

export default function ApiParameters({
  title,
  anchorId,
  parameters,
  example,
  references,
  nestedLevel = 0,
  small,
  noWrap,
  wide,
  collapsed,
  parentWidth = 0,
  next,
  prev,
  elementsLibrary,
}: Props) {
  const [userDrivenCollapsed, setUserDrivenCollapsed] = useState(collapsed);

  useEffect(() => {
    setUserDrivenCollapsed(collapsed);
  }, [collapsed]);

  const isExpanded = !nestedLevel && parameters.length === 1 && !!parameters[0].children;

  const renderedParameters = userDrivenCollapsed
    ? null
    : (parameters || []).map((p, idx) => (
        <ApiParameter
          key={p.name || idx}
          anchorId={p.anchorId}
          name={p.name}
          type={p.type}
          isExpanded={isExpanded}
          children={p.children}
          description={p.description}
          nestedLevel={nestedLevel}
          references={references}
          elementsLibrary={elementsLibrary}
        />
      ));

  const isNested = nestedLevel > 0;
  const className =
    "znai-api-parameters" + (isNested ? " nested" : "") + (small ? " small" : "") + (noWrap ? " no-wrap" : "");

  const style = { marginLeft: -parentWidth };

  const rendered = (
    <div className={className} style={style}>
      <ApiParametersTitle
        title={title}
        anchorId={anchorId}
        example={example}
        nestedLevel={nestedLevel}
        collapsed={userDrivenCollapsed}
        collapseToggle={collapseToggle}
      />
      {!userDrivenCollapsed && <ApiParametersExample example={example} isNested={isNested} />}
      {renderedParameters}
    </div>
  );

  if (!isNested) {
    const containerClass =
      "znai-api-parameters-wrapper " +
      (wide ? "wide" : "content-block") +
      (next?.type === "ApiParameters" ? " no-bottom-margin" : "") +
      (prev?.type === "ApiParameters" ? " no-top-margin" : "");

    return <div className={containerClass}>{rendered}</div>;
  }

  return rendered;

  function collapseToggle() {
    setUserDrivenCollapsed((prev) => !prev);
  }
}

interface TitleProps {
  title?: string;
  anchorId?: string;
  example?: string;
  nestedLevel: number;
  collapsed?: boolean;
  collapseToggle(): void;
}

function ApiParametersTitle({ title, anchorId, example, nestedLevel, collapsed, collapseToggle }: TitleProps) {
  if (!title || nestedLevel > 0) {
    return null;
  }

  const className = "znai-api-parameters-title-cell" + (example ? " with-example" : "");

  return (
    <ContainerTitle
      title={title}
      anchorId={anchorId}
      collapsed={collapsed}
      onCollapseToggle={collapseToggle}
      additionalContainerClassNames={className}
      additionalTitleClassNames="znai-api-parameters-title"
    />
  );
}

interface ExampleProps {
  example?: string;
  isNested: boolean;
}

function ApiParametersExample({ example, isNested }: ExampleProps) {
  if (!example || isNested) {
    return null;
  }

  example = example.trim();

  return (
    <div className="znai-api-parameters-example-cell">
      <Snippet lang={lang()} snippet={example} collapsed={true} title="example" />
    </div>
  );

  function lang() {
    if (!example) {
      return undefined;
    }

    if (example.startsWith("{") || example.startsWith("[")) {
      return "json";
    }

    if (example.startsWith("<")) {
      return "xml";
    }

    return undefined;
  }
}

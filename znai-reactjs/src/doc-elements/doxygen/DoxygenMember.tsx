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

import React from "react";
import type { DoxygenParameter } from "./Doxygen";

import { LinkWrapper } from "../default-elements/LinkWrapper";
import type { ApiLinkedText } from "../api/ApiLinkedText";

import { ApiLinkedTextBlock } from "../api/ApiLinkedTextBlock";

import "./DoxygenMember.css";

interface Props {
  compoundName: string;
  name: string;
  declType: string;
  url?: string;
  isFunction: boolean;
  isStatic: boolean;
  isVirtual: boolean;
  isConst: boolean;
  isNoExcept: boolean;
  returnType: ApiLinkedText;
  parameters?: DoxygenParameter[];
  templateParameters?: DoxygenParameter[];
}

export function DoxygenMember({
  compoundName,
  name,
  declType,
  url,
  returnType,
  isFunction,
  isStatic,
  isVirtual,
  isConst,
  isNoExcept,
  parameters,
  templateParameters,
}: Props) {
  const renderedName = <div className="znai-doxygen-member-name">{name}</div>;
  const nameWrappedInOptionalLink = url ? (
    <LinkWrapper url={url} treatAsLocal={true}>
      {renderedName}
    </LinkWrapper>
  ) : (
    renderedName
  );

  return (
    <div className="znai-doxygen-member content-block">
      {templateParameters && templateParameters.length > 0 && (
        <div className="znai-doxygen-member-template-row">
          <div className="znai-doxygen-template-params">
            <div className="znai-doxygen-template">template&lt;</div>
            {templateParameters.map((templateParam, idx) => {
              const needSeparator = idx !== templateParameters.length - 1;

              return (
                <div key={idx} className="znai-doxygen-template-param">
                  <ApiLinkedTextBlock linkedText={templateParam.type} />
                  {needSeparator && <div className="znai-doxygen-member-params-separator">, </div>}
                </div>
              );
            })}
            <div className="znai-doxygen-template">&gt;</div>
          </div>
        </div>
      )}

      <div className="znai-doxygen-member-signature-row">
        {isStatic && <div className="znai-doxygen-member-classifier">static </div>}
        {isVirtual && <div className="znai-doxygen-member-classifier">virtual </div>}

        <div className="znai-doxygen-member-return">
          <ApiLinkedTextBlock linkedText={returnType} />
        </div>
        <div className="znai-doxygen-member-full-name">
          {compoundName && (
            <>
              <div className="znai-doxygen-member-compound-name">{compoundName}</div>
              <div className="znai-doxygen-member-name-separator">::</div>
            </>
          )}

          {nameWrappedInOptionalLink}
          {parameters && renderParametersAndClassifiers(parameters)}
        </div>
      </div>
    </div>
  );

  function renderParametersAndClassifiers(parameters: DoxygenParameter[]) {
    if (parameters.length === 0) {
      return (
        <div className="znai-doxygen-member-params-and-classifiers">
          <div className="znai-doxygen-member-params-separator">()</div>
        </div>
      );
    }

    if (parameters.length === 1) {
      return (
        <div className="znai-doxygen-member-params-and-classifiers">
          <div className="znai-doxygen-member-params-single">
            <div className="znai-doxygen-member-params-separator">(</div>
            {renderParameter(parameters[0], 0, false)}
            <div className="znai-doxygen-member-params-separator">)</div>
          </div>

          {renderClassifiers()}
        </div>
      );
    }

    return (
      <div className="znai-doxygen-member-params-and-classifiers">
        {isFunction && <div className="znai-doxygen-member-params-separator">(</div>}
        {parameters.map((param, idx) => {
          const needSeparator = idx !== parameters.length - 1;
          return renderParameter(param, idx, needSeparator);
        })}
        {isFunction && <div className="znai-doxygen-member-params-separator">)</div>}
        {renderClassifiers()}
      </div>
    );

    function renderParameter(param: DoxygenParameter, idx: number, needSeparator: boolean) {
      return (
        <div className="znai-doxygen-member-param" key={idx}>
          <div className="znai-doxygen-member-param-type">
            <ApiLinkedTextBlock linkedText={param.type} />
          </div>
          <div className="znai-doxygen-member-param-name">{param.name}</div>
          {needSeparator && <div className="znai-doxygen-member-params-separator">, </div>}
        </div>
      );
    }

    function renderClassifiers() {
      return (
        <>
          {isConst && <div className="znai-doxygen-member-classifier"> const</div>}
          {isNoExcept && <div className="znai-doxygen-member-classifier"> noexcept</div>}
          {declType && <div className="znai-doxygen-member-decltype"> -&gt; {declType}</div>}
        </>
      );
    }
  }
}

export const presentationDoxygenMember = {
  component: DoxygenMember,
  numberOfSlides: () => 1,
};

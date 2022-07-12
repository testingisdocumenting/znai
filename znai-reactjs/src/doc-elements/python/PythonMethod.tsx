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

import React from "react";

import { PythonArg } from "./PythonArg";
import { ApiLinkedTextBlock } from "../api/ApiLinkedTextBlock";
import { LinkWrapper } from "../default-elements/LinkWrapper";

import "./PythonMethod.css";

interface Props {
  qualifiedName: string;
  hideNameQualifier?: boolean;
  removeSelf?: boolean;
  extraBottomMargin?: boolean;
  url?: string;
  args: PythonArg[];
  decorators: string[];
}

export function PythonMethod({
  qualifiedName,
  url,
  args,
  decorators,
  hideNameQualifier,
  removeSelf,
  extraBottomMargin,
}: Props) {
  const { packageName, name } = splitIntoPackageAndName(qualifiedName);

  // remove self arg if present
  if (removeSelf && args.length > 0 && args[0].name === "self") {
    args.shift();
  }

  const positionalArgs = args.filter((arg) => arg.category === "POS_ONLY");
  const restOfArgs = args.filter((arg) => arg.category !== "POS_ONLY");

  const renderedArgs: JSX.Element[] = [];
  positionalArgs.forEach((arg) => renderedArgs.push(<PythonArgBlock key={arg.name} arg={arg} />));
  if (positionalArgs.length > 0) {
    renderedArgs.push(<div className="znai-python-method-arg-separator">/</div>);
  }

  restOfArgs.forEach((arg) => renderedArgs.push(<PythonArgBlock key={arg.name} arg={arg} />));

  const renderedName = <div className="znai-python-method-name">{name}</div>;

  const nameWrappedInOptionalLink = url ? (
    <LinkWrapper url={url} treatAsLocal={true}>
      {renderedName}
    </LinkWrapper>
  ) : (
    renderedName
  );

  const className = "znai-python-method content-block" + (extraBottomMargin ? " extra-bottom-margin" : "");

  const combinedDecorators = combineAndAdjustDecorators();

  return (
    <div className={className}>
      <div className="znai-python-method-full-name">
        {combinedDecorators.length > 0 && <div className="znai-python-method-decorators">{combinedDecorators}</div>}
        {!hideNameQualifier && (
          <>
            <div className="znai-python-method-package-name">{packageName}</div>
            <div className="znai-python-method-separator">.</div>
          </>
        )}
        <div className="znai-python-method-name">{nameWrappedInOptionalLink}</div>
        <div className="znai-python-method-separator">(</div>
        <div className="znai-python-args">
          {renderedArgs.map((rendered, idx) => {
            const needSeparator = idx !== renderedArgs.length - 1;
            return (
              <>
                {rendered}
                {needSeparator && <div className="znai-python-method-arg-separator">, </div>}
              </>
            );
          })}
          <div className="znai-python-method-separator">)</div>
        </div>
      </div>
    </div>
  );

  function combineAndAdjustDecorators() {
    return decorators.map(convertDecorator);

    function convertDecorator(d: string) {
      switch (d) {
        case "staticmethod":
          return "static";

        case "classmethod":
          return "class method";

        default:
          return d;
      }
    }
  }
}

interface PythonArgProps {
  arg: PythonArg;
}

function PythonArgBlock({ arg }: PythonArgProps) {
  const hasType = arg.type.length > 0;
  const defaultValueSeparator = hasType ? " = " : "=";

  const namePrefix = namePrefixBasedOnCategory();

  return (
    <>
      <div className="znai-python-method-arg">
        {namePrefix && <div className="znai-python-method-arg-name-prefix">{namePrefix}</div>}
        <div className="znai-python-method-arg-name">{arg.name}</div>

        {hasType && (
          <>
            <div className="znai-python-method-arg-separator">: </div>
            <div className="znai-python-method-arg-type">
              <ApiLinkedTextBlock linkedText={arg.type} />
            </div>
          </>
        )}

        {arg.defaultValue && (
          <div className="znai-python-method-arg-default-value-area">
            <div className="znai-python-method-arg-separator">{defaultValueSeparator}</div>
            <div className="znai-python-method-arg-default-value">{arg.defaultValue}</div>
          </div>
        )}
      </div>
    </>
  );

  function namePrefixBasedOnCategory() {
    if (arg.category === "ARGS") {
      return "*";
    }

    if (arg.category === "KWARGS") {
      return "**";
    }

    return "";
  }
}

function splitIntoPackageAndName(fullName: string) {
  const lastDotIdx = fullName.lastIndexOf(".");
  const packageName = fullName.substr(0, lastDotIdx);
  const name = fullName.substr(lastDotIdx + 1);

  return {
    packageName,
    name,
  };
}

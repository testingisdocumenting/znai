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

import React, { CSSProperties } from "react";

import { DocElementPayload } from "../default-elements/DocElement";

import "./Container.css";

export interface ContainerCommonProps {
  noGap?: boolean;
  next?: DocElementPayload;
  prev?: DocElementPayload;
}

interface Props extends ContainerCommonProps {
  className: string;
  style?: CSSProperties;
  onClick?(): void;
  children: any;
}

/**
 * handles things like removing gaps based on next/prev doc elements
 *
 * gaps logic
 * C1 noGap
 * C2
 * C3 noGap
 * C4
 *
 * C1 and C2 will stick together
 * C3 and C4 will stick together
 * gap is between C2 and C3
 *
 * @constructor
 */
export function Container({ className, style, onClick, noGap, next, prev, children }: Props) {
  const noBottomMargin = noGap && next;
  const noTopMargin = prev && prev.noGap;
  const fullClassName =
    "znai-container znai-mobile-remove-padding " +
    className +
    (noBottomMargin ? " no-bottom-margin" : "") +
    (noTopMargin ? " no-top-margin" : "");

  return (
    <div className={fullClassName} style={style} onClick={onClick}>
      {children}
    </div>
  );
}

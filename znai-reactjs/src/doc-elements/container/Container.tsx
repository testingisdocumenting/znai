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

import { DocElementPayload } from "../default-elements/DocElement";

import "./Container.css";

interface Props {
  className: string;
  noGap?: boolean;
  next?: DocElementPayload;
  prev?: DocElementPayload;
  children: any;
}

/**
 * handles things like removing gaps based on next/prev doc elements
 * @constructor
 */
export function Container({ className, noGap, next, prev, children }: Props) {
  const noBottomMargin = noGap && next && next.noGap;
  const noTopMargin = noGap && prev && prev.noGap;
  const fullClassName =
    "znai-container " + className + (noBottomMargin ? " no-bottom-margin" : "") + (noTopMargin ? " no-top-margin" : "");

  return <div className={fullClassName}>{children}</div>;
}

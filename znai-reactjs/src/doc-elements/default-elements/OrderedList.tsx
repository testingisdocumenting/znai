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

import { DocElementProps } from "./DocElement";
import React from "react";

interface Props extends DocElementProps {
  className?: string;
  startNumber: number;
}

export function OrderedList({ className, startNumber, ...props }: Props) {
  const fullClassName = "content-block znai-ordered-list" + (className ? " " + className : "");
  return (
    <ol className={fullClassName} start={startNumber}>
      <props.elementsLibrary.DocElement {...props} />
    </ol>
  );
}

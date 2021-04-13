/*
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

import React, { useCallback } from "react";

import * as katex from "katex";
import "katex/dist/katex.min.css";

import "./Latex.css";

interface LatexProps {
  inline?: boolean;
  latex: string;
}

export default function Latex({ inline, latex }: LatexProps) {
  const initLatex = useCallback(
    (ref: HTMLElement | null) => {
      if (latex && ref) {
        katex.render(latex, ref, {
          throwOnError: false,
        });
      }
    },
    [latex]
  );

  return inline ? (
    <span className="latex content-block" ref={initLatex} />
  ) : (
    <div className="latex content-block" ref={initLatex} />
  );
}

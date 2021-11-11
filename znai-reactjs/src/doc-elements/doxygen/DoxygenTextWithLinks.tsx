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

import { DoxygenLink } from "./Doxygen";

import "./DoxygenTextWithLinks.css";

interface Props {
  text: DoxygenLink[];
}

export function DoxygenTextWithLinks({ text }: Props) {
  return (
    <div className="znai-doxygen-text-with-links">
      {text.map((part, idx) => (
        <React.Fragment key={idx}>
          <div className="znai-doxygen-text-with-links-part">{part.text}</div>
        </React.Fragment>
      ))}
    </div>
  );
}

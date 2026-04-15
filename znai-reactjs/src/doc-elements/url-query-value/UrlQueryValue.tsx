/*
 * Copyright 2026 znai maintainers
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

import { resolveQueryParamValue } from "./queryParamTemplate";

import "./UrlQueryValue.css";

interface Props {
  queryParam: string;
  default?: string;
}

export function UrlQueryValue({ queryParam, default: defaultValue }: Props) {
  const resolved = resolveQueryParamValue(queryParam, defaultValue);

  if (resolved.isMissing) {
    return (
      <span className="znai-url-query-value-error">
        query parameter <code>{queryParam}</code> is missing
      </span>
    );
  }

  return <span className="znai-url-query-value">{resolved.value}</span>;
}

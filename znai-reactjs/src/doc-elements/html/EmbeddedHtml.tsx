/*
 * Copyright 2024 znai maintainers
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

interface Props {
  html: string;
  isInlined: boolean;
}

function RawHTML({ html }: { html: string }) {
  const ref = React.useRef<HTMLElement>(null);

  // @ts-ignore
  React.useEffect(() => (ref.current!.outerHTML = html), []);
  return <span ref={ref} />;
}

export function EmbeddedHtml({ html, isInlined }: Props) {
  if (isInlined) {
    // @ts-ignore
    return <RawHTML html={html} />;
  } else {
    return <div className="content-block" dangerouslySetInnerHTML={{ __html: html }} />;
  }
}

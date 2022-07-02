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

import React, { useState } from "react";

import mermaid from "mermaid";

interface Props {
  mermaid: string;
}

export default function Mermaid(props: Props) {
  const [html, setHTML] = React.useState("");
  const [znaiThemeName, setZnaiThemeName] = useState(detectZnaiThemeName());

  React.useEffect(() => {
    // TODO theme integration via context
    // @ts-ignore
    window.znaiTheme.addChangeHandler(onThemeChange);

    initializeMermaid();

    // @ts-ignore
    return () => window.znaiTheme.removeChangeHandler(onThemeChange);

    function initializeMermaid() {
      mermaid.mermaidAPI.initialize({
        startOnLoad: false,
        // @ts-ignore
        theme: mermaidThemeName(),
      });
    }

    function onThemeChange() {
      initializeMermaid();
      setZnaiThemeName(detectZnaiThemeName());
    }

    function mermaidThemeName() {
      // @ts-ignore
      return detectZnaiThemeName() === "znai-dark" ? "dark" : "default";
    }
  }, []);

  React.useEffect(() => {
    mermaid.mermaidAPI.render("mermaid", props.mermaid, (html) => setHTML(html));
  }, [props.mermaid, znaiThemeName]);

  return <div className="znai-mermaid content-block" dangerouslySetInnerHTML={{ __html: html }}></div>;
}

function detectZnaiThemeName(): string {
  // @ts-ignore
  return window.znaiTheme.name;
}

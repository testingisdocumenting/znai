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
// eslint-disable-next-line no-unused-vars
import * as mermaid from "mermaid";

export default function Mermaid(props) {
  const [html, setHTML] = React.useState("");

  React.useEffect(() => {
    mermaid.mermaidAPI.initialize({
      startOnLoad: false,
    });
  }, []);

  React.useEffect(() => {
    mermaid.mermaidAPI.render("mermaid", props.mermaid, (html) =>
      setHTML(html)
    );
  }, [props.mermaid]);

  return (
    <div
      className="znai-mermaid content-block"
      dangerouslySetInnerHTML={{ __html: html }}
    ></div>
  );
}

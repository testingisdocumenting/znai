/*
 * Copyright 2020 znai maintainers
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
import TocPanel from "./TocPanel";
import { testLongToc } from "../structure/toc/toc.test.data";

const docMeta = {
  title: "Znai",
  type: "User Guide",
  viewOn: {
    link: "https://github.com/testingisdocumenting/znai/blob/master/znai-cli/documentation",
    title: "View On GitHub",
  },
};

export function tocPanelDemo(registry) {
  const selectedTocItem = { dirName: "snippets", fileName: "json" };

  registry
    .add("long", () => <TocPanel docMeta={docMeta} toc={testLongToc()} selectedItem={selectedTocItem} />, "")
    .add("short", () => <TocPanel docMeta={docMeta} toc={shortToc()} selectedItem={selectedTocItem} />, "");
}

function shortToc() {
  return [
    {
      sectionTitle: "",
      dirName: "",
      items: [
        {
          sectionTitle: "",
          pageTitle: "Index",
          pageMeta: {},
          fileName: "index",
          dirName: "",
          pageSectionIdTitles: [],
        },
        {
          sectionTitle: "",
          pageTitle: "My Intro",
          pageMeta: {},
          fileName: "my-intro",
          dirName: "",
          pageSectionIdTitles: [],
        },
      ],
    },
    {
      sectionTitle: "Introduction",
      dirName: "introduction",
      items: [
        {
          sectionTitle: "Introduction",
          pageTitle: "Rationale",
          pageMeta: {},
          fileName: "rationale",
          dirName: "introduction",
        },
        {
          sectionTitle: "Introduction",
          pageTitle: "Example",
          pageMeta: {},
          fileName: "example",
          dirName: "introduction",
        },
        {
          sectionTitle: "Introduction",
          pageTitle: "Getting Started",
          pageMeta: {},
          fileName: "getting-started",
          dirName: "introduction",
        },
      ],
    },
    {
      sectionTitle: "Synergy With Testing",
      dirName: "synergy-with-testing",
      items: [
        {
          sectionTitle: "Synergy With Testing",
          pageTitle: "Java",
          pageMeta: {},
          fileName: "java",
          dirName: "synergy-with-testing",
        },
        {
          sectionTitle: "Synergy With Testing",
          pageTitle: "REST",
          pageMeta: {},
          fileName: "REST",
          dirName: "synergy-with-testing",
        },
        {
          sectionTitle: "Synergy With Testing",
          pageTitle: "Web UI",
          pageMeta: {},
          fileName: "web-UI",
          dirName: "synergy-with-testing",
        },
      ],
    },
    {
      sectionTitle: "Configuration",
      dirName: "configuration",
      items: [
        {
          sectionTitle: "Configuration",
          pageTitle: "Basic",
          pageMeta: {},
          fileName: "basic",
          dirName: "configuration",
        },
      ],
    },
  ];
}

/*
 * Copyright 2025 znai maintainers
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
import { Registry } from "react-component-viewer";
import { DocStatsView, PageStats } from "./DocStatsView";
import { TocItem } from "../../structure/TocItem";

const sampleToc: TocItem[] = [
  {
    chapterTitle: "",
    dirName: "",
    fileName: "",
    items: [
      {
        chapterTitle: "",
        pageTitle: "Index",
        dirName: "",
        fileName: "index",
      },
      {
        chapterTitle: "",
        pageTitle: "Getting Started",
        dirName: "",
        fileName: "getting-started",
      },
    ],
  },
  {
    chapterTitle: "Introduction",
    dirName: "introduction",
    fileName: "",
    items: [
      {
        chapterTitle: "Introduction",
        pageTitle: "What Is This",
        dirName: "introduction",
        fileName: "what-is-this",
      },
      {
        chapterTitle: "Introduction",
        pageTitle: "Installation Guide",
        dirName: "introduction",
        fileName: "installation",
      },
      {
        chapterTitle: "Introduction",
        pageTitle: "Quick Start Tutorial",
        dirName: "introduction",
        fileName: "quick-start",
      },
    ],
  },
  {
    chapterTitle: "Core Concepts",
    dirName: "core-concepts",
    fileName: "",
    items: [
      {
        chapterTitle: "Core Concepts",
        pageTitle: "Architecture Overview",
        dirName: "core-concepts",
        fileName: "architecture",
      },
      {
        chapterTitle: "Core Concepts",
        pageTitle: "Configuration",
        dirName: "core-concepts",
        fileName: "configuration",
      },
      {
        chapterTitle: "Core Concepts",
        pageTitle: "Plugins System",
        dirName: "core-concepts",
        fileName: "plugins",
      },
      {
        chapterTitle: "Core Concepts",
        pageTitle: "Theming",
        dirName: "core-concepts",
        fileName: "theming",
      },
    ],
  },
  {
    chapterTitle: "API Reference",
    dirName: "api",
    fileName: "",
    items: [
      {
        chapterTitle: "API Reference",
        pageTitle: "REST Endpoints",
        dirName: "api",
        fileName: "rest-endpoints",
      },
      {
        chapterTitle: "API Reference",
        pageTitle: "Authentication",
        dirName: "api",
        fileName: "authentication",
      },
      {
        chapterTitle: "API Reference",
        pageTitle: "Error Handling",
        dirName: "api",
        fileName: "error-handling",
      },
    ],
  },
  {
    chapterTitle: "Advanced Topics",
    dirName: "advanced",
    fileName: "",
    items: [
      {
        chapterTitle: "Advanced Topics",
        pageTitle: "Performance Optimization",
        dirName: "advanced",
        fileName: "performance",
      },
      {
        chapterTitle: "Advanced Topics",
        pageTitle: "Custom Extensions",
        dirName: "advanced",
        fileName: "extensions",
      },
    ],
  },
];

const samplePageStats: Record<string, PageStats> = {
  "getting-started": { totalViews: 15420, uniqueViews: 8934 },
  "introduction/what-is-this": { totalViews: 12350, uniqueViews: 7210 },
  "introduction/installation": { totalViews: 9876, uniqueViews: 5432 },
  "introduction/quick-start": { totalViews: 8543, uniqueViews: 4321 },
  "core-concepts/architecture": { totalViews: 5678, uniqueViews: 3456 },
  "core-concepts/configuration": { totalViews: 4321, uniqueViews: 2345 },
  "core-concepts/plugins": { totalViews: 3210, uniqueViews: 1987 },
  "core-concepts/theming": { totalViews: 2100, uniqueViews: 1234 },
  "api/rest-endpoints": { totalViews: 7890, uniqueViews: 4567 },
  "api/authentication": { totalViews: 6543, uniqueViews: 3890 },
  "api/error-handling": { totalViews: 2345, uniqueViews: 1456 },
  "advanced/performance": { totalViews: 1890, uniqueViews: 1023 },
  "advanced/extensions": { totalViews: 987, uniqueViews: 654 },
};

const sparsePageStats: Record<string, PageStats> = {
  "getting-started": { totalViews: 1520, uniqueViews: 893 },
  "introduction/what-is-this": { totalViews: 2350, uniqueViews: 1210 },
  "core-concepts/architecture": { totalViews: 567, uniqueViews: 345 },
  "api/rest-endpoints": { totalViews: 789, uniqueViews: 456 },
};

const highVolumeStats: Record<string, PageStats> = {
  "getting-started": { totalViews: 1542000, uniqueViews: 893400 },
  "introduction/what-is-this": { totalViews: 1235000, uniqueViews: 721000 },
  "introduction/installation": { totalViews: 987600, uniqueViews: 543200 },
  "introduction/quick-start": { totalViews: 854300, uniqueViews: 432100 },
  "core-concepts/architecture": { totalViews: 567800, uniqueViews: 345600 },
  "core-concepts/configuration": { totalViews: 432100, uniqueViews: 234500 },
  "api/rest-endpoints": { totalViews: 789000, uniqueViews: 456700 },
  "api/authentication": { totalViews: 654300, uniqueViews: 389000 },
};

export function docStatsViewDemo(registry: Registry) {
  registry
    .add("with full stats", () => <DocStatsView toc={sampleToc} pageStats={samplePageStats} />)
    .add("with sparse stats", () => <DocStatsView toc={sampleToc} pageStats={sparsePageStats} />)
    .add("with high volume stats", () => <DocStatsView toc={sampleToc} pageStats={highVolumeStats} />)
    .add("dark theme", () => (
      <div className="with-theme theme-znai-dark">
        <DocStatsView toc={sampleToc} pageStats={samplePageStats} />
      </div>
    ))
    .add("empty stats", () => <DocStatsView toc={sampleToc} pageStats={{}} />);
}

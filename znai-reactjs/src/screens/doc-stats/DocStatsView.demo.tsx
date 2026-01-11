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
import { Registry, simulateState } from "react-component-viewer";
import { DocStatsView, PageStats, TimePeriod } from "./DocStatsView";
import { TocItem } from "../../structure/TocItem";

const demoToc: TocItem[] = [
  {
    chapterTitle: "",
    dirName: "",
    fileName: "",
    items: [
      { chapterTitle: "", pageTitle: "Index", dirName: "", fileName: "index" },
      { chapterTitle: "", pageTitle: "Getting Started", dirName: "", fileName: "getting-started" },
    ],
  },
  {
    chapterTitle: "Introduction",
    dirName: "introduction",
    fileName: "",
    items: [
      { chapterTitle: "Introduction", pageTitle: "What Is This", dirName: "introduction", fileName: "what-is-this" },
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
      { chapterTitle: "Core Concepts", pageTitle: "Plugins System", dirName: "core-concepts", fileName: "plugins" },
      { chapterTitle: "Core Concepts", pageTitle: "Theming", dirName: "core-concepts", fileName: "theming" },
    ],
  },
  {
    chapterTitle: "API Reference",
    dirName: "api",
    fileName: "",
    items: [
      { chapterTitle: "API Reference", pageTitle: "REST Endpoints", dirName: "api", fileName: "rest-endpoints" },
      { chapterTitle: "API Reference", pageTitle: "Authentication", dirName: "api", fileName: "authentication" },
      { chapterTitle: "API Reference", pageTitle: "Error Handling", dirName: "api", fileName: "error-handling" },
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
      { chapterTitle: "Advanced Topics", pageTitle: "Custom Extensions", dirName: "advanced", fileName: "extensions" },
    ],
  },
];

interface PeriodStats {
  overall: PageStats;
  chapters: Record<string, PageStats>;
  pages: Record<string, PageStats>;
}

const statsByPeriod: Record<TimePeriod, PeriodStats> = {
  week: {
    overall: { totalViews: 395, uniqueViews: 120 },
    chapters: {
      "": { totalViews: 85, uniqueViews: 42 },
      "introduction": { totalViews: 169, uniqueViews: 60 },
      "core-concepts": { totalViews: 0, uniqueViews: 0 },
      "api": { totalViews: 93, uniqueViews: 35 },
      "advanced": { totalViews: 17, uniqueViews: 6 },
    },
    pages: {
      "getting-started": { totalViews: 85, uniqueViews: 42 },
      "introduction/what-is-this": { totalViews: 68, uniqueViews: 35 },
      "introduction/installation": { totalViews: 54, uniqueViews: 28 },
      "introduction/quick-start": { totalViews: 47, uniqueViews: 24 },
      "core-concepts/architecture": { totalViews: 0, uniqueViews: 0 },
      "core-concepts/configuration": { totalViews: 0, uniqueViews: 0 },
      "core-concepts/plugins": { totalViews: 0, uniqueViews: 0 },
      "core-concepts/theming": { totalViews: 0, uniqueViews: 0 },
      "api/rest-endpoints": { totalViews: 43, uniqueViews: 22 },
      "api/authentication": { totalViews: 36, uniqueViews: 19 },
      "api/error-handling": { totalViews: 14, uniqueViews: 7 },
      "advanced/performance": { totalViews: 11, uniqueViews: 5 },
      "advanced/extensions": { totalViews: 6, uniqueViews: 3 },
      "legacy/old-api": { totalViews: 23, uniqueViews: 12 },
      "removed/deprecated-feature": { totalViews: 8, uniqueViews: 4 },
    },
  },
  month: {
    overall: { totalViews: 8112, uniqueViews: 2800 },
    chapters: {
      "": { totalViews: 1542, uniqueViews: 893 },
      "introduction": { totalViews: 3076, uniqueViews: 1100 },
      "core-concepts": { totalViews: 1530, uniqueViews: 650 },
      "api": { totalViews: 1677, uniqueViews: 700 },
      "advanced": { totalViews: 287, uniqueViews: 140 },
    },
    pages: {
      "getting-started": { totalViews: 1542, uniqueViews: 893 },
      "introduction/what-is-this": { totalViews: 1235, uniqueViews: 721 },
      "introduction/installation": { totalViews: 987, uniqueViews: 543 },
      "introduction/quick-start": { totalViews: 854, uniqueViews: 432 },
      "core-concepts/architecture": { totalViews: 567, uniqueViews: 345 },
      "core-concepts/configuration": { totalViews: 432, uniqueViews: 234 },
      "core-concepts/plugins": { totalViews: 321, uniqueViews: 198 },
      "core-concepts/theming": { totalViews: 210, uniqueViews: 123 },
      "api/rest-endpoints": { totalViews: 789, uniqueViews: 456 },
      "api/authentication": { totalViews: 654, uniqueViews: 389 },
      "api/error-handling": { totalViews: 234, uniqueViews: 145 },
      "advanced/performance": { totalViews: 189, uniqueViews: 102 },
      "advanced/extensions": { totalViews: 98, uniqueViews: 65 },
      "legacy/old-api": { totalViews: 156, uniqueViews: 89 },
      "removed/deprecated-feature": { totalViews: 67, uniqueViews: 34 },
    },
  },
  year: {
    overall: { totalViews: 63140, uniqueViews: 22000 },
    chapters: {
      "": { totalViews: 12000, uniqueViews: 7000 },
      "introduction": { totalViews: 23500, uniqueViews: 9000 },
      "core-concepts": { totalViews: 11400, uniqueViews: 5000 },
      "api": { totalViews: 12550, uniqueViews: 5500 },
      "advanced": { totalViews: 2140, uniqueViews: 1000 },
    },
    pages: {
      "getting-started": { totalViews: 12000, uniqueViews: 7000 },
      "introduction/what-is-this": { totalViews: 9500, uniqueViews: 5500 },
      "introduction/installation": { totalViews: 7500, uniqueViews: 4200 },
      "introduction/quick-start": { totalViews: 6500, uniqueViews: 3300 },
      "core-concepts/architecture": { totalViews: 4200, uniqueViews: 2600 },
      "core-concepts/configuration": { totalViews: 3200, uniqueViews: 1800 },
      "core-concepts/plugins": { totalViews: 2400, uniqueViews: 1500 },
      "core-concepts/theming": { totalViews: 1600, uniqueViews: 950 },
      "api/rest-endpoints": { totalViews: 5900, uniqueViews: 3400 },
      "api/authentication": { totalViews: 4900, uniqueViews: 2900 },
      "api/error-handling": { totalViews: 1750, uniqueViews: 1100 },
      "advanced/performance": { totalViews: 1400, uniqueViews: 780 },
      "advanced/extensions": { totalViews: 740, uniqueViews: 490 },
      "legacy/old-api": { totalViews: 1230, uniqueViews: 678 },
      "removed/deprecated-feature": { totalViews: 543, uniqueViews: 276 },
    },
  },
  total: {
    overall: { totalViews: 83420, uniqueViews: 28000 },
    chapters: {
      "": { totalViews: 15420, uniqueViews: 8934 },
      "introduction": { totalViews: 30769, uniqueViews: 12000 },
      "core-concepts": { totalViews: 15309, uniqueViews: 6500 },
      "api": { totalViews: 16778, uniqueViews: 7200 },
      "advanced": { totalViews: 2877, uniqueViews: 1300 },
    },
    pages: {
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
      "legacy/old-api": { totalViews: 1567, uniqueViews: 834 },
      "removed/deprecated-feature": { totalViews: 712, uniqueViews: 389 },
    },
  },
};

const [getSelectedPeriod, setSelectedPeriod] = simulateState<TimePeriod>("total");

export function docStatsViewDemo(registry: Registry) {
  registry.add("default", () => {
    const periodStats = statsByPeriod[getSelectedPeriod()];
    return (
      <DocStatsView
        guideName={"My guide"}
        toc={demoToc}
        overallStats={periodStats.overall}
        chapterStats={periodStats.chapters}
        pageStats={periodStats.pages}
        selectedPeriod={getSelectedPeriod()}
        availablePeriods={["week", "month", "year", "total"]}
        onPeriodChange={setSelectedPeriod}
      />
    );
  });
}

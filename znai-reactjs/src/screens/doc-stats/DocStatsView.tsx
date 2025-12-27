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

import React, { useEffect, useRef } from "react";
import { TocItem } from "../../structure/TocItem";
import { documentationNavigation } from "../../structure/DocumentationNavigation";
import { isTocItemIndex } from "../../structure/toc/TableOfContents";

import "./DocStatsView.css";

export type TimePeriod = "week" | "month" | "year" | "total";

export interface PageStats {
  totalViews: number;
  uniqueViews: number;
}

export interface DocStatsViewProps {
  guideName: string;
  toc: TocItem[];
  pageStats: Record<string, PageStats>;
  selectedPeriod: TimePeriod;
  availablePeriods: TimePeriod[];
  onPeriodChange: (period: TimePeriod) => void;
}

interface PageItemProps {
  item: TocItem;
  stats?: PageStats;
}

const TIME_PERIODS: { key: TimePeriod; label: string }[] = [
  { key: "week", label: "Week" },
  { key: "month", label: "Month" },
  { key: "year", label: "Year" },
  { key: "total", label: "Total" },
];

function buildPageId(dirName: string, fileName: string): string {
  return dirName ? `${dirName}/${fileName}` : fileName;
}

function collectTocPageIds(toc: TocItem[]): Set<string> {
  const pageIds = new Set<string>();
  for (const chapter of toc) {
    for (const item of chapter.items || []) {
      if (!isTocItemIndex(item)) {
        pageIds.add(buildPageId(item.dirName, item.fileName));
      }
    }
  }
  return pageIds;
}

function formatNumber(num: number): string {
  if (num >= 1000000) {
    return (num / 1000000).toFixed(1) + "M";
  }
  if (num >= 1000) {
    return (num / 1000).toFixed(1) + "K";
  }
  return num.toLocaleString();
}

const ZERO_STATS: PageStats = { totalViews: 0, uniqueViews: 0 };

function sumStats(statsArray: PageStats[]): PageStats {
  return statsArray.reduce(
    (acc, stats) => ({
      totalViews: acc.totalViews + stats.totalViews,
      uniqueViews: acc.uniqueViews + stats.uniqueViews,
    }),
    ZERO_STATS
  );
}

function StatsCounters({ stats }: { stats: PageStats }) {
  return (
    <div className="znai-doc-stats-counters">
      <span className="znai-doc-stats-counter znai-doc-stats-total" title="Total views">
        <span className="znai-doc-stats-counter-value">{formatNumber(stats.totalViews)}</span>
        <span className="znai-doc-stats-counter-label">views</span>
      </span>
      <span className="znai-doc-stats-counter znai-doc-stats-unique" title="Unique visitors">
        <span className="znai-doc-stats-counter-value">{formatNumber(stats.uniqueViews)}</span>
        <span className="znai-doc-stats-counter-label">unique</span>
      </span>
    </div>
  );
}

function ChapterSummary({ stats }: { stats: PageStats }) {
  return (
    <div className="znai-doc-stats-chapter-summary">
      <span className="znai-doc-stats-summary-item">{formatNumber(stats.totalViews)} views</span>
      <span className="znai-doc-stats-summary-separator">|</span>
      <span className="znai-doc-stats-summary-item">{formatNumber(stats.uniqueViews)} unique</span>
    </div>
  );
}

function OverallStatCard({ value, label }: { value: number; label: string }) {
  return (
    <div className="znai-doc-stats-overall-stat">
      <span className="znai-doc-stats-overall-value">{formatNumber(value)}</span>
      <span className="znai-doc-stats-overall-label">{label}</span>
    </div>
  );
}

function PageItem({ item, stats }: PageItemProps) {
  const href = documentationNavigation.buildUrl(item);

  return (
    <div className="znai-doc-stats-page-item">
      <a href={href} className="znai-doc-stats-page-link">
        {item.pageTitle}
      </a>
      <StatsCounters stats={stats ?? ZERO_STATS} />
    </div>
  );
}

interface ChapterSectionProps {
  chapter: TocItem;
  pageStats: Record<string, PageStats>;
}

function ChapterSection({ chapter, pageStats }: ChapterSectionProps) {
  const items = (chapter.items || []).filter((item) => !isTocItemIndex(item));

  if (items.length === 0) {
    return null;
  }

  const itemStats = items
    .map((item) => pageStats[buildPageId(item.dirName, item.fileName)])
    .filter((stats): stats is PageStats => !!stats);
  const chapterStats = sumStats(itemStats);

  return (
    <div className="znai-doc-stats-chapter">
      <div className="znai-doc-stats-chapter-header">
        <span className="znai-doc-stats-chapter-title">{chapter.chapterTitle}</span>
        <ChapterSummary stats={chapterStats} />
      </div>
      <div className="znai-doc-stats-pages">
        {items.map((item) => {
          const pageId = buildPageId(item.dirName, item.fileName);
          return <PageItem key={pageId} item={item} stats={pageStats[pageId]} />;
        })}
      </div>
    </div>
  );
}

interface OrphanedPagesSectionProps {
  orphanedPages: [string, PageStats][];
}

function OrphanedPagesSection({ orphanedPages }: OrphanedPagesSectionProps) {
  if (orphanedPages.length === 0) {
    return null;
  }

  const sectionStats = sumStats(orphanedPages.map(([, stats]) => stats));

  return (
    <div className="znai-doc-stats-chapter znai-doc-stats-orphaned">
      <div className="znai-doc-stats-chapter-header">
        <span className="znai-doc-stats-chapter-title">Orphaned Pages</span>
        <ChapterSummary stats={sectionStats} />
      </div>
      <div className="znai-doc-stats-pages">
        {orphanedPages.map(([pageId, stats]) => (
          <div key={pageId} className="znai-doc-stats-page-item">
            <span className="znai-doc-stats-orphaned-page-id">{pageId}</span>
            <StatsCounters stats={stats} />
          </div>
        ))}
      </div>
    </div>
  );
}

interface TimePeriodSwitcherProps {
  selectedPeriod: TimePeriod;
  availablePeriods: TimePeriod[];
  onPeriodChange: (period: TimePeriod) => void;
}

function TimePeriodSwitcher({ selectedPeriod, availablePeriods, onPeriodChange }: TimePeriodSwitcherProps) {
  return (
    <div className="znai-doc-stats-period-switcher">
      {TIME_PERIODS.filter((p) => availablePeriods.includes(p.key)).map((period) => (
        <button
          key={period.key}
          className={`znai-doc-stats-period-button ${selectedPeriod === period.key ? "active" : ""}`}
          onClick={() => onPeriodChange(period.key)}
        >
          {period.label}
        </button>
      ))}
    </div>
  );
}

export function DocStatsView({
  guideName,
  toc,
  pageStats,
  selectedPeriod,
  availablePeriods,
  onPeriodChange,
}: DocStatsViewProps) {
  const contentRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    contentRef.current?.focus();
  }, []);

  const totalStats = sumStats(Object.values(pageStats));

  const tocPageIds = collectTocPageIds(toc);
  const orphanedPages = Object.entries(pageStats).filter(([pageId]) => !tocPageIds.has(pageId));

  return (
    <div className="znai-doc-stats-view">
      <div className="znai-doc-stats-header">
        <h1 className="znai-doc-stats-title">{guideName} analytics</h1>
        {availablePeriods.length > 1 && (
          <TimePeriodSwitcher
            selectedPeriod={selectedPeriod}
            availablePeriods={availablePeriods}
            onPeriodChange={onPeriodChange}
          />
        )}
      </div>
      <div className="znai-doc-stats-content" ref={contentRef} tabIndex={-1}>
        <div className="znai-doc-stats-overall">
          <OverallStatCard value={totalStats.totalViews} label="Total Views" />
          <OverallStatCard value={totalStats.uniqueViews} label="Unique Visitors" />
        </div>
        <div className="znai-doc-stats-chapters">
          {toc.map((chapter, idx) => (
            <ChapterSection key={chapter.chapterTitle || idx} chapter={chapter} pageStats={pageStats} />
          ))}
          <OrphanedPagesSection orphanedPages={orphanedPages} />
        </div>
      </div>
    </div>
  );
}

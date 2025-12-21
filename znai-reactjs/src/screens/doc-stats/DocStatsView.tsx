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
import { TocItem } from "../../structure/TocItem";
import { documentationNavigation } from "../../structure/DocumentationNavigation";
import { isTocItemIndex } from "../../structure/toc/TableOfContents";

import "./DocStatsView.css";

export interface PageStats {
  totalViews: number;
  uniqueViews: number;
}

export interface DocStatsViewProps {
  toc: TocItem[];
  pageStats: Record<string, PageStats>;
}

interface PageItemProps {
  item: TocItem;
  stats?: PageStats;
  onPageClick: (dirName: string, fileName: string) => void;
}

function buildPageId(dirName: string, fileName: string): string {
  return dirName ? `${dirName}/${fileName}` : fileName;
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

const PageItem: React.FC<PageItemProps> = ({ item, stats, onPageClick }) => {
  const href = documentationNavigation.buildUrl(item);

  const handleClick = (e: React.MouseEvent) => {
    e.preventDefault();
    onPageClick(item.dirName, item.fileName);
  };

  return (
    <div className="znai-doc-stats-page-item">
      <a href={href} onClick={handleClick} className="znai-doc-stats-page-link">
        {item.pageTitle}
      </a>
      {stats && (
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
      )}
    </div>
  );
};

interface ChapterSectionProps {
  chapter: TocItem;
  pageStats: Record<string, PageStats>;
  onPageClick: (dirName: string, fileName: string) => void;
}

const ChapterSection: React.FC<ChapterSectionProps> = ({ chapter, pageStats, onPageClick }) => {
  const items = (chapter.items || []).filter((item) => !isTocItemIndex(item));

  if (items.length === 0) {
    return null;
  }

  const chapterStats = items.reduce(
    (acc, item) => {
      const pageId = buildPageId(item.dirName, item.fileName);
      const stats = pageStats[pageId];
      if (stats) {
        acc.totalViews += stats.totalViews;
        acc.uniqueViews += stats.uniqueViews;
      }
      return acc;
    },
    { totalViews: 0, uniqueViews: 0 }
  );

  return (
    <div className="znai-doc-stats-chapter">
      <div className="znai-doc-stats-chapter-header">
        <span className="znai-doc-stats-chapter-title">{chapter.chapterTitle}</span>
        {chapterStats.totalViews > 0 && (
          <div className="znai-doc-stats-chapter-summary">
            <span className="znai-doc-stats-summary-item">{formatNumber(chapterStats.totalViews)} views</span>
            <span className="znai-doc-stats-summary-separator">|</span>
            <span className="znai-doc-stats-summary-item">{formatNumber(chapterStats.uniqueViews)} unique</span>
          </div>
        )}
      </div>
      <div className="znai-doc-stats-pages">
        {items.map((item) => {
          const pageId = buildPageId(item.dirName, item.fileName);
          return <PageItem key={pageId} item={item} stats={pageStats[pageId]} onPageClick={onPageClick} />;
        })}
      </div>
    </div>
  );
};

export const DocStatsView: React.FC<DocStatsViewProps> = ({ toc, pageStats }) => {
  const navigateToPage = (dirName: string, fileName: string) => {
    documentationNavigation.navigateToPage({ dirName, fileName });
  };

  const totalStats = Object.values(pageStats).reduce(
    (acc, stats) => {
      acc.totalViews += stats.totalViews;
      acc.uniqueViews += stats.uniqueViews;
      return acc;
    },
    { totalViews: 0, uniqueViews: 0 }
  );

  return (
    <div className="znai-doc-stats-view">
      <div className="znai-doc-stats-header">
        <h2 className="znai-doc-stats-title">Guide Analytics</h2>
        <div className="znai-doc-stats-overall">
          <div className="znai-doc-stats-overall-stat">
            <span className="znai-doc-stats-overall-value">{formatNumber(totalStats.totalViews)}</span>
            <span className="znai-doc-stats-overall-label">Total Views</span>
          </div>
          <div className="znai-doc-stats-overall-stat">
            <span className="znai-doc-stats-overall-value">{formatNumber(totalStats.uniqueViews)}</span>
            <span className="znai-doc-stats-overall-label">Unique Visitors</span>
          </div>
          <div className="znai-doc-stats-overall-stat">
            <span className="znai-doc-stats-overall-value">{Object.keys(pageStats).length}</span>
            <span className="znai-doc-stats-overall-label">Pages Tracked</span>
          </div>
        </div>
      </div>
      <div className="znai-doc-stats-chapters">
        {toc.map((chapter, idx) => (
          <ChapterSection
            key={chapter.chapterTitle || idx}
            chapter={chapter}
            pageStats={pageStats}
            onPageClick={navigateToPage}
          />
        ))}
      </div>
    </div>
  );
};

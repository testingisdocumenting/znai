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

import React, { useEffect, useState } from "react";
import { TocItem } from "../../structure/TocItem";
import { DocMeta, getDocId, getDocMeta, mergeDocMeta } from "../../structure/docMeta";
import { fetchWithCredentials } from "../../utils/fetchWithCredentials";
import { DocStatsView, PageStats, TimePeriod } from "./DocStatsView";

const AVAILABLE_PERIODS: TimePeriod[] = ["week", "month", "year", "total"];

export interface PeriodStats {
  overall: PageStats;
  chapters: Record<string, PageStats>;
  pages: Record<string, PageStats>;
}

export type DocStatsResponse = Record<TimePeriod, PeriodStats>;

export interface DocStatsScreenProps {
  toc: TocItem[];
  docMeta: DocMeta;
}

async function fetchDocStats(url: string): Promise<DocStatsResponse> {
  const fullUrl = new URL(url);
  fullUrl.searchParams.set("docId", getDocId());
  const response = await fetchWithCredentials(fullUrl.toString(), {});

  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.error || `HTTP ${response.status}`);
  }

  return response.json();
}

export function DocStatsScreen({ toc, docMeta }: DocStatsScreenProps) {
  const [selectedPeriod, setSelectedPeriod] = useState<TimePeriod>("total");
  const [statsByPeriod, setStatsByPeriod] = useState<DocStatsResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  // rest of the code in this file and nested components expect global docMeta presence
  useEffect(() => {
    mergeDocMeta(docMeta);
  }, [docMeta]);

  useEffect(() => {
    const docStatsUrl = getDocMeta().docStatsUrl;
    if (!docStatsUrl) {
      return;
    }

    fetchDocStats(docStatsUrl)
      .then(setStatsByPeriod)
      .catch((err) => {
        setError(err instanceof Error ? err.message : "Failed to load stats");
      });
  }, []);

  if (error) {
    return (
      <div className="znai-doc-stats-error">
        <p>Failed to load analytics: {error}</p>
      </div>
    );
  }

  if (!statsByPeriod) {
    return null;
  }

  const periodStats = statsByPeriod[selectedPeriod] || { overall: { totalViews: 0, uniqueViews: 0 }, chapters: {}, pages: {} };

  return (
    <DocStatsView
      guideName={getDocMeta().title}
      toc={toc}
      overallStats={periodStats.overall}
      chapterStats={periodStats.chapters}
      pageStats={periodStats.pages}
      selectedPeriod={selectedPeriod}
      availablePeriods={AVAILABLE_PERIODS}
      onPeriodChange={setSelectedPeriod}
    />
  );
}

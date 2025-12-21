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
import { getDocMeta } from "../../structure/docMeta";
import { fetchWithCredentials } from "../../utils/fetchWithCredentials";
import { DocStatsView, PageStats, TimePeriod } from "./DocStatsView";

const AVAILABLE_PERIODS: TimePeriod[] = ["week", "month", "year", "total"];

export type DocStatsResponse = Record<TimePeriod, Record<string, PageStats>>;

export interface DocStatsScreenProps {
  toc: TocItem[];
}

async function fetchDocStats(url: string, signal: AbortSignal): Promise<DocStatsResponse> {
  const response = await fetchWithCredentials(url, { signal });

  if (!response.ok) {
    const body = await response.json().catch(() => ({}));
    throw new Error(body.error || `HTTP ${response.status}`);
  }

  return response.json();
}

export function DocStatsScreen({ toc }: DocStatsScreenProps) {
  const [selectedPeriod, setSelectedPeriod] = useState<TimePeriod>("total");
  const [statsByPeriod, setStatsByPeriod] = useState<DocStatsResponse | null>(null);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const docStatsUrl = getDocMeta().docStatsUrl;
    if (!docStatsUrl) {
      return;
    }

    const abortController = new AbortController();

    fetchDocStats(docStatsUrl, abortController.signal)
      .then(setStatsByPeriod)
      .catch((err) => {
        if (!abortController.signal.aborted) {
          setError(err instanceof Error ? err.message : "Failed to load stats");
        }
      });

    return () => abortController.abort();
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

  const pageStats = statsByPeriod[selectedPeriod] || {};

  return (
    <DocStatsView
      guideName={getDocMeta().title}
      toc={toc}
      pageStats={pageStats}
      selectedPeriod={selectedPeriod}
      availablePeriods={AVAILABLE_PERIODS}
      onPeriodChange={setSelectedPeriod}
    />
  );
}

export { DocStatsView } from "./DocStatsView";
export type { DocStatsViewProps, PageStats, TimePeriod } from "./DocStatsView";

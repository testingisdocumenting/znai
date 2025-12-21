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

import { useEffect, useState } from "react";
import { TocItem } from "../../structure/TocItem";
import { getDocMeta } from "../../structure/docMeta";
import { DocStatsView, PageStats, TimePeriod } from "./DocStatsView";

/*
 * REST API Schema:
 *
 * GET {docMeta.docStatsUrl}
 *
 * Response:
 * {
 *   "week": {
 *     "getting-started": { "totalViews": 85, "uniqueViews": 42 },
 *     "introduction/installation": { "totalViews": 54, "uniqueViews": 28 },
 *     ...
 *   },
 *   "month": { ... },
 *   "year": { ... },
 *   "total": { ... }
 * }
 *
 * Error Response (4xx/5xx):
 * {
 *   "error": "Error message"
 * }
 */

const AVAILABLE_PERIODS: TimePeriod[] = ["week", "month", "year", "total"];

export type DocStatsResponse = Record<TimePeriod, Record<string, PageStats>>;

export interface DocStatsScreenProps {
  toc: TocItem[];
}

async function fetchDocStats(apiUrl: string): Promise<DocStatsResponse> {
  const response = await fetch(apiUrl);

  if (!response.ok) {
    const error = await response.json().catch(() => ({ error: "Failed to fetch stats" }));
    throw new Error(error.error || `HTTP ${response.status}`);
  }

  return response.json();
}

export function DocStatsScreen({ toc }: DocStatsScreenProps) {
  const [selectedPeriod, setSelectedPeriod] = useState<TimePeriod>("total");
  const [statsByPeriod, setStatsByPeriod] = useState<DocStatsResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    const docStatsUrl = getDocMeta().docStatsUrl;
    if (!docStatsUrl) {
      setIsLoading(false);
      return;
    }

    let cancelled = false;

    async function loadStats() {
      setIsLoading(true);
      setError(null);

      try {
        const data = await fetchDocStats(docStatsUrl!);

        if (cancelled) return;

        setStatsByPeriod(data);
      } catch (err) {
        if (cancelled) return;
        setError(err instanceof Error ? err.message : "Failed to load stats");
      } finally {
        if (!cancelled) {
          setIsLoading(false);
        }
      }
    }

    loadStats();

    return () => {
      cancelled = true;
    };
  }, []);

  if (error) {
    return (
      <div className="znai-doc-stats-error">
        <p>Failed to load analytics: {error}</p>
      </div>
    );
  }

  if (isLoading) {
    return (
      <div className="znai-doc-stats-loading">
        <p>Loading analytics...</p>
      </div>
    );
  }

  if (!statsByPeriod) {
    return null;
  }

  const pageStats = statsByPeriod[selectedPeriod] || {};

  return (
    <DocStatsView
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

/*
 * Copyright 2024 znai maintainers
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

package org.testingisdocumenting.znai.text;

import java.util.List;

/**
 * finds the shortest block that contains passed lines
 */
public class MultilineIndexFinder {
    public record StartEndIdx(int startIdx, int endIdx) {
        int distance() {
            return endIdx - startIdx + 1;
        }
    }

    public static StartEndIdx findIdxForMultiLinesShortestDistanceBetween(TextLinesAccessor text, List<String> matchLines) {
        int minDistanceIdx = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int idx = 0; idx < text.numberOfLines() - matchLines.size(); idx++) {
            int distance = matchLinesContaining(text, idx, matchLines);
            if (distance != -1 && distance < minDistance) {
                minDistanceIdx = idx;
                minDistance = distance;
            }
        }

        return new StartEndIdx(minDistanceIdx, minDistanceIdx + minDistance - 1);
    }

    // returns total distance between matched lines
    // or -1 when is not found
    static int matchLinesContaining(TextLinesAccessor text, int startIdx, List<String> matchLines) {
        int idx = startIdx;
        int len = text.numberOfLines();
        int matchLen = matchLines.size();

        if (len == 0 || matchLen == 0) {
            return -1;
        }

        if (idx + matchLen > len) {
            return -1;
        }

        if (!text.lineAtIdx(idx).contains(matchLines.get(0))) {
            return -1;
        }

        int numberOfMatched = 1;
        idx++;
        for (int matchIdx = 1; matchIdx < matchLen && idx < len - matchLen; idx++) {
            String matchLine = matchLines.get(matchIdx);
            String line = text.lineAtIdx(idx);

            if (line.contains(matchLine)) {
                matchIdx++;
                numberOfMatched++;
            }
        }

        return numberOfMatched == matchLen ? (idx - startIdx) : -1;
    }
}

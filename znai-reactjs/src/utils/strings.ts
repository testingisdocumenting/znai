/*
 * Copyright 2020 znai maintainers
 * Copyright 2019 TWO SIGMA OPEN SOURCE, LLC
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

export function splitAndTrimEmptyLines(text: string): string[] {
  const lines = text.split("\n");
  let startIdx = 0;
  for (; startIdx < lines.length; startIdx++) {
    if (lines[startIdx].trim() !== "") {
      break;
    }
  }

  let endIdx = lines.length - 1;
  for (; endIdx >= 0; endIdx--) {
    if (lines[endIdx].trim() !== "") {
      break;
    }
  }

  return lines.slice(startIdx, endIdx + 1);
}

export function splitTextIntoLinesUsingThreshold(
  text: string,
  thresholdCharCount: number
): string[] {
  if (text.length < thresholdCharCount) {
    return [text];
  }

  const words = text.split(" ");
  const result = splitParts({
    parts: words,
    lengthFunc: (word) => word.length + 1, // one for space
    thresholdCharCount: thresholdCharCount,
  });

  return result.map((words) => words.join(" "));
}

interface SplitPartsOptions<T> {
  parts: T[];
  lengthFunc: (part: T) => number;
  valueFunc?: (part: T) => string;
  thresholdCharCount: number;
  splitAfterList?: string[];
}

export function splitParts<T>({
  parts,
  lengthFunc,
  valueFunc,
  thresholdCharCount,
  splitAfterList = [],
}: SplitPartsOptions<T>): T[][] {
  const result: T[][] = [];
  let runningLength = 0;
  let runningParts: T[] = [];

  parts.forEach((part) => {
    const len = lengthFunc(part);

    if (runningLength + len >= thresholdCharCount) {
      flush();
    }

    runningLength += len;
    runningParts.push(part);

    const partValue = valueFunc && valueFunc(part);
    if (partValue && splitAfterList.some((splitAfter) => splitAfter === partValue.trim())) {
      flush();
    }
  });

  flush();

  return result;

  function flush() {
    if (runningParts.length) {
      result.push(runningParts);
    }

    runningParts = [];
    runningLength = 0;
  }
}

export function repeatChar(n: number, char: string): string {
  let result = "";
  for (let idx = 0; idx < n; idx++) {
    result += char;
  }

  return result;
}

export function countNumberOfLines(text: string): number {
  let result = 0;

  const len = text.length;
  for (let idx = 0; idx < len; idx++) {
    if (text[idx] === "\n") {
      result++;
    }
  }

  if (result === 0) {
    return 1;
  }

  if (text[text.length - 1] === "\n") {
    return result;
  }

  return result + 1;
}
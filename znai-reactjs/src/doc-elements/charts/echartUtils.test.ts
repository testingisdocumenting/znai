/*
 * Copyright 2022 znai maintainers
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

import { createInvisibleLineSeries, partialDataExcludingDataAfterPoint } from "./echartUtils";

test("invisible line for numbers X", () => {
  const invisible = createInvisibleLineSeries([
    [-1, 0.1, -2, 3],
    [0, -4, -3, 5],
    [2, -2, 7, 1],
  ]);

  expect(invisible).toEqual({
    data: [
      {
        value: [-1, -4],
        itemStyle: {
          color: "none",
        },
      },
      {
        value: [2, 7],
        itemStyle: {
          color: "none",
        },
      },
    ],
    lineStyle: {
      color: "none",
    },
  });
});

test("invisible line for text X", () => {
  const invisible = createInvisibleLineSeries([
    ["A", 0.1, -2, 3],
    ["B", -4, -3, 5],
    ["C", -2, 7, 1],
  ]);

  expect(invisible).toEqual({
    data: [
      {
        value: -4,
        itemStyle: {
          color: "none",
        },
      },
      {
        value: 7,
        itemStyle: {
          color: "none",
        },
      },
    ],
    lineStyle: {
      color: "none",
    },
  });
});

test("partial data with number breakpoint", () => {
  const partial = partialDataExcludingDataAfterPoint(
    [
      [-1, 0.1, -2, 3],
      [0, -4, -3, 5],
      [2, -2, 7, 1],
    ],
    2,
    1
  );

  expect(partial).toEqual([
    [-1, -2],
    [0, -3],
  ]);
});

test("partial data with text breakpoint", () => {
  const partial = partialDataExcludingDataAfterPoint(
    [
      ["A", 0.1, -2, 3],
      ["B", -4, -3, 5],
      ["C", -2, 7, 1],
    ],
    2,
    "B"
  );

  expect(partial).toEqual([-2, -3]);
});

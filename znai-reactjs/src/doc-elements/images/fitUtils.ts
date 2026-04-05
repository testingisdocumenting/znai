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

import { cssVarPixelValue } from "../../utils/cssVars";

export function calcFitScale(
  fit: boolean | undefined,
  width: number,
  scale: number | undefined,
  isMobile: boolean
): number {
  if (!fit) {
    return scale || 1.0;
  }

  const singleColumnWidth = isMobile
    ? window.innerWidth
    : cssVarPixelValue("znai-single-column-full-width");
  return Math.min(1.0, singleColumnWidth / width);
}

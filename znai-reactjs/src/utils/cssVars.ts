/*
 * Copyright 2021 znai maintainers
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

const pxSuffix = "px";
export function cssVarPixelValue(varName: string): number {
  const varValue = getComputedStyle(document.documentElement).getPropertyValue(
    "--" + varName
  );
  return pixelValue(varValue);
}

export function pixelValue(varName: string): number {
  if (!varName.endsWith(pxSuffix)) {
    throw new Error(
      `css var <${varName}> value should have ${pxSuffix} suffix`
    );
  }

  const withoutPx = varName.substr(0, varName.length - pxSuffix.length);
  return Number(withoutPx);
}

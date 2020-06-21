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

/*
 doc elements may receive additional information as meta prop that is provided via
 :include-meta: {key: 'value'}
 */

export interface PresentationStickPlacement {
    left: boolean;
    top: boolean;
    clear: boolean;
    percentage: number;
}

export interface DocElementMeta {
    allAtOnce?: boolean;
    presentationStickPlacement?: string;
}

export function isAllAtOnce(meta: DocElementMeta) {
    return meta && meta.allAtOnce;
}

export function elementMetaValue(element: any, key: string) {
    return element && element.meta ? element.meta[key] : null;
}

/**
 * presentation stick placement makes a slide stick around, while other slide appears
 *
 * :include-meta: {presentationStickPlacement: 'top'}
 * :include-meta: {presentationStickPlacement: 'left 30%'}
 * @param meta
 */
export function presentationStickPlacement(meta: DocElementMeta): PresentationStickPlacement | undefined {
    if (!meta) {
        return undefined;
    }

    const defaultPercentage = 50;

    const placement = meta.presentationStickPlacement;
    if (!placement) {
        return undefined;
    }

    const parts = placement.trim().split(' ');
    return {
        left: parts[0] === 'left',
        top: parts[0] === 'top',
        percentage: parts[1] ? extractPercentage(parts[1].trim()) : defaultPercentage,
        clear: parts[0] === 'clear'
    }

    function extractPercentage(percentage: string): number {
        if (percentage.length === 0) {
            return defaultPercentage;
        }

        if (percentage.endsWith('%')) {
            return extractPercentage(percentage.substr(0, percentage.length - 1))
        }

        return Number(percentage);
    }
}

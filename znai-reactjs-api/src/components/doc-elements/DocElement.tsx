/*
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

import * as React from 'react';

import { DocElementProps } from './DocElementProps';

/**
 * uses given set of components to render DocElements like links, paragraphs, code blocks, etc
 *
 * @param content content to render
 * @param elementsLibrary library of elements to use to render
 */
export function DocElement({content, elementsLibrary}: DocElementProps) {
    return (!content ? null : content.map((item, idx) => {
        const ElementToUse = elementsLibrary[item.type];
        if (!ElementToUse) {
            console.warn('can\'t find component to display', item.type);
            return null;
        } else {
            return (
                <ElementToUse
                    key={idx}
                    elementsLibrary={elementsLibrary}
                    {...item}
                />);
        }
    }));
}

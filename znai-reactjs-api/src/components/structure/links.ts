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

import { getDocId } from './docMeta';
import { documentationNavigation } from './DocumentationNavigation';

function isLocalUrl(url: string) {
    if (!window.document) {
        return false;
    }

    return url.startsWith('/' + getDocId());
}

function onLocalUrlClick(e: React.MouseEvent<HTMLAnchorElement>, url: string) {
    e.preventDefault();
    documentationNavigation.navigateToUrl(url);
}

export { isLocalUrl, onLocalUrlClick };

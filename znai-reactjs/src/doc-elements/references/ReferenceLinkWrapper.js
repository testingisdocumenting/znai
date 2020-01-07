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

import React from 'react'

import {isExternalUrl} from '../structure/links'
import {documentationNavigation} from '../structure/DocumentationNavigation'
import {LinkWrapper} from '../default-elements/LinkWrapper'

export function ReferenceLinkWrapper({referenceUrl, className, children}) {
    if (!referenceUrl) {
        return children
    }

    const isLocalNavigation = !isExternalUrl(referenceUrl);

    const fullUrl = isLocalNavigation ?
        documentationNavigation.fullPageUrl(referenceUrl):
        referenceUrl

    return (
        <LinkWrapper url={fullUrl} treatAsLocal={isLocalNavigation} className={className}>
            {children}
        </LinkWrapper>
    )
}
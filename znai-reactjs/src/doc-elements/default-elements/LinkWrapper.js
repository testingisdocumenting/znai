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

import {onLocalUrlClick} from '../structure/links'
import {documentationTracking} from '../tracking/DocumentationTracking'

export function LinkWrapper({className, url, treatAsLocal, children}) {
    const onClick = treatAsLocal ? handleLocalUrlClick : handleExternalUrlClick
    const targetProp = treatAsLocal ? {} : {target: "_blank"}

    return (
        <a href={url} className={className} onClick={onClick} {...targetProp}>
            {children}
        </a>
    )

    function handleLocalUrlClick(e) {
        documentationTracking.onLinkClick(url)
        onLocalUrlClick(e, url)
    }

    function handleExternalUrlClick() {
        documentationTracking.onLinkClick(url)
    }
}
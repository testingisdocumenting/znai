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

/**
 * Injects css at runtime based on the presence of static/css/custom.css file.
 * Applicable for on-premise mdoc hosting where there is a central hosting of documentation.
 * Various documentations can be built at different times.
 * At the time of their original build there was no custom.css, so they don't refer any customizations.
 * Docs need to be updated now to have custom overrides.
 *
 * It is most likely a temporary solution.
 */
// TODO remove after Landing Page TS release

function injectCustomCssLink() {
    const link = document.createElement('link')
    link.rel = 'stylesheet'
    link.type = 'text/css'
    link.href = '/static/css/custom.css'

    document.head.appendChild(link)
}

export {injectCustomCssLink}
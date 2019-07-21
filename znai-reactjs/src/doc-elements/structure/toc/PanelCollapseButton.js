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

import './PanelCollapseButton.css'

export default function PanelCollapseButton({isCollapsed, onClick}) {
    const className = isCollapsed ?
        'toc-panel-expand-button':
        'toc-panel-collapse-button'

    return (
        <div className={className} onClick={onClick}>
            <svg xmlns="http://www.w3.org/2000/svg" width="9" height="14" viewBox="0 0 9 14">
                <path fillRule="evenodd"
                      d="M6.93 13.23L0 6.61 6.93 0l1.48 1.532L3.305 6.65l5.107 4.977z"/>
            </svg>
        </div>
    )
}

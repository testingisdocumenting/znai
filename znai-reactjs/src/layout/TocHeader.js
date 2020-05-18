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
import PanelCollapseButton from './PanelCollapseButton'

export default function TocHeader({docMeta, collapsed, onCollapseToggle, onHeaderClick}) {
    return (
        <div className="toc-panel-header">
            <div className="toc-panel-header-logo-and-title">
                <div className="znai-documentation-logo"/>
                <div className="toc-panel-header-title"
                     onClick={onHeaderClick}>
                    {docMeta.title + " " + docMeta.type}
                </div>
            </div>
            <PanelCollapseButton isCollapsed={collapsed} onClick={onCollapseToggle}/>
        </div>
    )
}
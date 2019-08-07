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

import React, {Component} from 'react'

import './TocPanelSearch.css'

class TocPanelSearch extends Component {
    render() {
        return (
            <div className="znai-toc-panel-search-area">
                <div className="znai-toc-panel-search" onClick={this.props.onClick} title="hotkey /">
                    <div className="znai-toc-panel-search-icon-and-text">
                        <div className="znai-search-icon glyphicon glyphicon-search"/>
                        <div>Search...</div>
                    </div>
                </div>
            </div>
        )
    }
}

export default TocPanelSearch

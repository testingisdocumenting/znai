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

import React, {Component} from 'react'

import { Icon } from '../doc-elements/icons/Icon'

import './TocPanelSearch.css'

class TocPanelSearch extends Component {
    render() {
        return (
          <div className="znai-toc-panel-search" onClick={this.props.onClick}>
              <div className="znai-toc-panel-search-icon-and-text">
                  <Icon id="search" />
                  <div>Search...</div>
              </div>

              <div className="znai-search-hotkey">/</div>
          </div>
        )
    }
}

export default TocPanelSearch;

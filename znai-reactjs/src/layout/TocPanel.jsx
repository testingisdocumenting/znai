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

import TocMenu from './TocMenu'
import TocHeader from './TocHeader'
import TocPanelSearch from './TocPanelSearch'
import {DarkLightThemeSwitcher} from './DarkLightThemeSwitcher'

import './TocPanel.css'

class TocPanel extends Component {
    state = {
        collapsed: false,
        selected: false,
    }

    render() {
        const {
            docMeta,
            onHeaderClick,
            toc,
            selectedItem,
            onTocItemClick,
            onTocItemPageSectionClick,
            onSearchClick,
        } = this.props

        const {
            collapsed,
            selected,
        } = this.state

        const panelClass = 'toc-panel' +
            (collapsed ? ' collapsed' : '') +
            (selected ? ' selected' : '')

        const displayTocExtra = !docMeta?.useTopHeader

        return (
          <div className={panelClass}>
              {displayTocExtra && <TocHeader docMeta={docMeta}
                                              collapsed={collapsed}
                                              onHeaderClick={onHeaderClick}
                                              onCollapseToggle={this.collapseToggle}/>}

              {displayTocExtra && onSearchClick &&
                <div className="znai-toc-panel-search-area">
                    <TocPanelSearch onClick={onSearchClick}/>
                </div>}

              <TocMenu toc={toc}
                       selected={selectedItem}
                       onTocItemPageSectionClick={onTocItemPageSectionClick}
                       onTocItemClick={onTocItemClick}/>

              {displayTocExtra && <div className="znai-toc-bottom-panel">
                  <DarkLightThemeSwitcher/>
              </div>}
          </div>
        )
    }

    collapseToggle = () => {
        this.setState(prev => ({collapsed: !prev.collapsed}))
    }

    keyDownHandler = (e) => {
        const {selected, collapsed, onNextPage, onPrevPage} = this.props

        if (!selected || collapsed) {
            return
        }

        if (e.key === 'ArrowUp') {
            onPrevPage()
        } else if (e.key === 'ArrowDown') {
            onNextPage()
        }
    }

    componentDidMount() {
        document.addEventListener('keydown', this.keyDownHandler)
    }

    componentWillUnmount() {
        document.removeEventListener('keydown', this.keyDownHandler)
    }
}

export default TocPanel
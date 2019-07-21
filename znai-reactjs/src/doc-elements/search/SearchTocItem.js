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
import scrollIntoView from 'scroll-into-view-if-needed'

export default class SearchTocItem extends Component {
    render() {
        const {pageTitle, pageSection, isSelected, idx, onSelect, onJump} = this.props

        const className = "mdoc-search-toc-item" + (isSelected ? " selected" : "")

        return (
            <div className={className}
                 onClick={() => onSelect(idx)}
                 onDoubleClick={() => onJump(idx)}
                 ref={this.saveRef}>
                <span className="mdoc-search-toc-page-title">{pageTitle}</span>
                <span className="mdoc-search-toc-section-title">{pageSection}</span>
            </div>
        )
    }

    saveRef = (node) => {
        this.node = node
    }

    componentDidMount() {
        this.scrollIfRequired()
    }

    componentDidUpdate() {
        this.scrollIfRequired()
    }

    scrollIfRequired() {
        const {isSelected} = this.props

        if (isSelected) {
            scrollIntoView(this.node, { behavior: 'smooth', scrollMode: 'if-needed' })
        }
    }
}